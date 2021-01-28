package com.yuchen.starssdk.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuchen.starssdk.EbMqttAction;
import com.yuchen.starssdk.MqttAction;
import com.yuchen.starssdk.MqttStatus;
import com.yuchen.starssdk.StarsConfig;
import com.yuchen.starssdk.StarsSdkHelper;
import com.yuchen.starssdk.mqtt.MqttMessageSendCommon;
import com.yuchen.starssdk.mqtt.PubTopic;
import com.yuchen.starssdk.other.SafeHandler;
import com.yuchen.starssdk.other.SafeHandlerMsgCallback;
import com.yuchen.starssdk.utils.DateTimeUtil;
import com.yuchen.starssdk.utils.DeviceUtil;
import com.yuchen.starssdk.utils.LogUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MQTTService extends Service implements SafeHandlerMsgCallback {


    private static final int MSG_ONLINE = 1;
    private static final int MSG_CONNECT_SUCCESS = 2;
    private static final int MSG_RECONNECT_MQTT_FORCE = 3;
//    private static final int MSG_CONNECT_FAIL = 4;
//    private static final int MSG_CONNECT_LOST = 5;


    private static final SafeHandler handler = new SafeHandler();

    public static final String TAG = "MQTTService";

    private String mqttStatus = MqttStatus.free;
    private boolean reconnectMqttFore;

    private static MqttAndroidClient client;
    private MqttConnectOptions conOpt;

    private IMqttActionListener iMqttActionListener;
    private MqttCallback mqttCallback;


    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_ONLINE:
                heartbeat();
                break;
            case MSG_CONNECT_SUCCESS:
                connectSuccess((Boolean) msg.obj);
                break;
//            case MSG_CONNECT_FAIL:
//                connectFail();
//                break;
//            case MSG_CONNECT_LOST:
//                connectLost();
//                break;
            case MSG_RECONNECT_MQTT_FORCE:
                reconnectMqttForce();
                break;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d(TAG, "MQTTService.onCreate()");
        EventBus.getDefault().register(this);
        handler.regist(this);
        reconnectMqttFore = false;
    }

    @Subscribe
    public void a(String s) {

    }

    public static void publish(String topic, Object obj) {
        if (null != obj) {
            publish(topic, JSON.toJSONString(obj));
        }
    }

    public static void publish(String topic, String msg) {
        Integer qos = 2;
        Boolean retained = false;
        try {
            if (client != null && client.isConnected()) {
                client.publish(topic, msg.getBytes(), qos.intValue(), retained.booleanValue());
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private MqttConnectOptions initMqttOptions(String userName, String password, String deviceId) {
        MqttConnectOptions options = new MqttConnectOptions();
        // 自动重连
        options.setAutomaticReconnect(true);
        // 清除缓存
        options.setCleanSession(true);
        // 设置超时时间，单位：秒
        options.setConnectionTimeout(10);
        // 心跳包发送间隔，单位：秒
        options.setKeepAliveInterval(20);
        // 用户名
        options.setUserName(userName);
        // 密码
        options.setPassword(password.toCharArray());     //将字符串转换为字符串数组


        JSONObject lwtJSON = new JSONObject();
        lwtJSON.put("client", "business");
        lwtJSON.put("deviceId", deviceId);

        String topicLWT = "/lwt/android/";
        Integer qos = 2;
        Boolean retained = false;

        // 最后的遗嘱
        // MQTT本身就是为信号不稳定的网络设计的，所以难免一些客户端会无故的和Broker断开连接。
        //当客户端连接到Broker时，可以指定 LWT，Broker会定期检测客户端是否有异常。
        //当客户端异常掉线时，Broker就往连接时指定的topic里推送当时指定的LWT消息。
        try {
            options.setWill(topicLWT, lwtJSON.toJSONString().getBytes(), qos.intValue(), retained.booleanValue());
        } catch (Exception e) {
            Log.i(TAG, "Exception Occured", e);
            return null;
        }
        return options;
    }

    private MqttAndroidClient initMqttClient(String mqttUrl, String clientId) {

        // MQTT是否连接成功
        iMqttActionListener = new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken arg0) {
                handler.removeMessages(MSG_RECONNECT_MQTT_FORCE);
                Log.i(TAG, "onSuccess 连接成功 " + DateTimeUtil.formatDateTimeNow());
                Message msg = Message.obtain();
                msg.what = MSG_CONNECT_SUCCESS;
                msg.obj = false;
                handler.removeMessages(MSG_CONNECT_SUCCESS);
                handler.sendMessageDelayed(msg, 300);

            }

            @Override
            public void onFailure(IMqttToken arg0, Throwable arg1) {
                arg1.printStackTrace();
                Log.i(TAG, "onFailure 连接失败" + DateTimeUtil.formatDateTimeNow());
                connectFail(arg1);
            }
        };

        // MQTT监听并且接受消息
        mqttCallback = new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                handler.removeMessages(MSG_RECONNECT_MQTT_FORCE);
                Log.i(TAG, " connectComplete 链接成功 " + DateTimeUtil.formatDateTimeNow());
                Message msg = Message.obtain();
                msg.what = MSG_CONNECT_SUCCESS;
                msg.obj = reconnect;
                handler.removeMessages(MSG_CONNECT_SUCCESS);
                handler.sendMessageDelayed(msg, 300);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                EventBus.getDefault().post(new EbMqttAction(MqttAction.messageArrived, topic, message));
//                String str1 = new String(message.getPayload());
//            String str2 = topic + ";qos:" + message.getQos() + ";retained:" + message.isRetained();
//                Log.i(TAG, "messageArrived:" + str1);
//            Log.i(TAG, str2);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken arg0) {
                EventBus.getDefault().post(new EbMqttAction(MqttAction.deliveryComplete, arg0));
//            try {
//                Log.i(TAG, "deliveryComplete 消息发送成功："+new String(arg0.getMessage().getPayload()));
//            } catch (MqttException e) {
//                e.printStackTrace();
//            }
            }

            @Override
            public void connectionLost(Throwable arg0) {
                Log.i(TAG, " connectionLost 链接丢失 " + DateTimeUtil.formatDateTimeNow());
                connectLost(arg0);
            }
        };

        // 服务器地址（协议+地址+端口号）
        MqttAndroidClient client = new MqttAndroidClient(this, mqttUrl, clientId);
        // 设置MQTT监听并且接受消息
        client.setCallback(mqttCallback);
        return client;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null && intent.getExtras() != null) {
            String action = intent.getStringExtra("action");
            LogUtils.d(TAG, "action : " + action);
            if ("connect".equals(action)) {
                connectMqtt();
            } else if ("disconnect".equals(action)) {
                disconnectMqtt();
            }
        }

//        return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    private void connectMqtt() {
        Log.d(TAG, "connectMqtt");
        if (MqttStatus.free.equals(mqttStatus) || MqttStatus.disconnted.equals(mqttStatus) || MqttStatus.disconnted.equals(mqttStatus)) {
            StarsConfig starsConfig = StarsSdkHelper.getInstence().getmStarsConfig();
            if (null != starsConfig) {
                conOpt = initMqttOptions(starsConfig.getMqttUserName(), starsConfig.getMqttPassword(), starsConfig.getDeviceId());
                client = initMqttClient(starsConfig.getMqttUrl(), starsConfig.getClientId());
                doClientConnection();
            }
        } else {
            Log.d(TAG, "connectMqtt 状态异常");
        }
    }

    private void heartbeat() {
        handler.removeMessages(MSG_ONLINE);
        handler.sendEmptyMessageDelayed(MSG_ONLINE, StarsSdkHelper.getInstence().getmStarsConfig().getHeartBeatDuration());
//        if(!DeviceUtil.isAppInstalled(this,"com.ala.monitor")){
        LogUtils.d(TAG, "heartbeat " + DateTimeUtil.formateDatetime(System.currentTimeMillis()));
        MqttMessageSendCommon sendCommon = MqttMessageSendCommon.createInstance();
        sendCommon.setMsgKey("heartbeat");
        sendCommon.setMsgBody(DateTimeUtil.formateDatetime(System.currentTimeMillis()));
        sendCommon.setMsgTopic(PubTopic.getHeartbeat());
        publish(sendCommon.getMsgTopic(), sendCommon);
//        }
//        sendProductAttr();
    }

//    private void sendProductAttr(){
//        MqttMessageSendCommon sendCommon = MqttMessageSendCommon.createInstance();
//        sendCommon.setMsgTopic(PubTopic.getInform());
//        sendCommon.setMsgKey("directDevProductAttrReport");
//        JSONArray body = new JSONArray();
//        addProductAttrStringTm(body,"door_status",1);
//        addProductAttrStringTm(body,"current_weight",String.valueOf(10)+"KG");
//        addProductAttrStringTm(body,"take_weight",String.valueOf(3.5)+"KG");
//        sendCommon.setMsgBody(body.toJSONString());
//        publish(sendCommon.getMsgTopic(),sendCommon);
//        sendCommon.setMsgTopic("123");
//        publish(sendCommon.getMsgTopic(),sendCommon);
//    }
//
//    public void addProductAttrStringTm(JSONArray bady,String attributeKey, Object attributeValue) {
//        JSONObject msg = new JSONObject();
//        msg.put("attributeKey", attributeKey);
//        msg.put("attributeValue", attributeValue);
//        bady.add(msg);
//    }

    private void disconnectMqtt() {
        LogUtils.d(TAG, "disconnectMqtt");
        this.mqttStatus = MqttStatus.disconnted;
        mqttCallback = null;
        iMqttActionListener = null;
        if (client != null) {
            try {
                client.unregisterResources();
                if (client.isConnected()) {
                    client.setCallback(null);
                    client.disconnect();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            } finally {
                client = null;
            }
        }
        if (!reconnectMqttFore)
            EventBus.getDefault().post(new EbMqttAction(MqttStatus.disconnted));

    }

    @Override
    public void onDestroy() {
        LogUtils.d(TAG, "MQTTService.onDestroy()");
        disconnectMqtt();
        EventBus.getDefault().unregister(this);
        if (handler != null)
            handler.unregist();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 连接MQTT服务器
     */
    private void doClientConnection() {
        LogUtils.d(TAG, "doClientConnection");
        this.mqttStatus = MqttStatus.disconnted;
        if (!client.isConnected()) {
            try {
                client.connect(conOpt, null, iMqttActionListener);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    private void connectSuccess(boolean reconnect) {
        LogUtils.d(TAG, "connectSuccess :reconnect=" + reconnect);
        EventBus.getDefault().post(new EbMqttAction(MqttStatus.connected, reconnect));
        this.mqttStatus = MqttStatus.connected;
        boolean setMqttClientTopices = StarsSdkHelper.getInstence().getmStarsConfig().setMqttClientTopices(client);
        LogUtils.d(TAG, "setMqttClientTopices " + setMqttClientTopices);
        try {
            heartbeat();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connectFail(Throwable e) {
        if (client != null && client.isConnected()) {
            LogUtils.d(TAG, "connectFail " + e.getMessage());
        } else {
            EventBus.getDefault().post(new EbMqttAction(MqttStatus.connectFail, e));
            LogUtils.d(TAG, "connectFail " + e.getMessage());
            this.mqttStatus = MqttStatus.connectFail;
            handler.removeMessages(MSG_ONLINE);
            handler.removeMessages(MSG_RECONNECT_MQTT_FORCE);
            handler.sendEmptyMessageDelayed(MSG_RECONNECT_MQTT_FORCE, 60 * 1000);
        }

    }


    private void connectLost(Throwable e) {
        handler.removeMessages(MSG_ONLINE);
        LogUtils.d(TAG, "connectLost " + e.getMessage());
        EventBus.getDefault().post(new EbMqttAction(MqttStatus.connectLost, e));
        handler.removeMessages(MSG_RECONNECT_MQTT_FORCE);
        handler.sendEmptyMessageDelayed(MSG_RECONNECT_MQTT_FORCE, 60 * 1000);
    }


    private void reconnectMqttForce() {
        LogUtils.d(TAG, "reconnectMqttForce");
        Intent intent = new Intent(getApplicationContext(), MQTTService.class);
        intent.putExtra("action", "connect");
        @SuppressLint("WrongConstant")
        PendingIntent restartIntent = PendingIntent.getService(getApplicationContext(), 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);
        reconnectMqttFore = true;
        stopSelf();
    }

}

