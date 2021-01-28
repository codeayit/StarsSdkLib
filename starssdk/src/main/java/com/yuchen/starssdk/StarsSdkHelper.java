package com.yuchen.starssdk;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuchen.starssdk.mqtt.MqttMessageSendCommon;
import com.yuchen.starssdk.net.GJsonParser;
import com.yuchen.starssdk.net.NetWork;
import com.yuchen.starssdk.net.NetWorkStringCallBack;
import com.yuchen.starssdk.service.MQTTService;
import com.yuchen.starssdk.utils.DeviceUtil;
import com.yuchen.starssdk.utils.SPManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class StarsSdkHelper {

    private final static String TAG = "StarsSdkHelper";
    private static StarsSdkHelper instence;
    private Context mContext;
    private StarsConfig mStarsConfig;
    private boolean inited;

    private StarsSdkHelper() {
    }

    public synchronized static StarsSdkHelper getInstence() {

        if (instence == null){
            instence = new StarsSdkHelper();
        }
        return instence;
    }

    public StarsConfig getmStarsConfig() {
        return mStarsConfig;
    }

    public synchronized void  init(Context context, StarsConfig config){
        if (!inited){
            SPManager.ini(context);
            mContext = context;
            mStarsConfig = config;
            EventBus.getDefault().register(this);
        }

        inited = true;
    }

    @Subscribe
    public void onMqttAction(EbMqttAction action){
        Log.d(TAG,action.toString());
        if (mqttServiceCallBack!=null){
            if (MqttAction.connected.equals(action.getStatus())){
                mqttServiceCallBack.connectSuccess();
            }else  if (MqttAction.disconnted.equals(action.getStatus())){
                mqttServiceCallBack.disconnected();
            }else if (MqttAction.connectFail.equals(action.getStatus())){
                mqttServiceCallBack.connectFail(action.getE());
            }else if (MqttAction.deliveryComplete.equals(action.getStatus())){
                mqttServiceCallBack.messageDeliveryComplete(action.getDeliveryToken());
            }else if (MqttAction.messageArrived.equals(action.getStatus())){
                mqttServiceCallBack.messageArrived(action.getTopic(),action.getMqttMessage());
            }
        }

    }

    public synchronized void destory(){
        EventBus.getDefault().unregister(this);
        disconnectMqtt();
        instence = null;
    }

    public void connectMqtt(){
        Intent intent = new Intent(mContext, MQTTService.class);
        intent.putExtra("action","connect");
        mContext.startService(intent);
    }

    public void disconnectMqtt(){
        Intent intent = new Intent(mContext, MQTTService.class);
        intent.putExtra("action","disconnect");
        mContext.startService(intent);
    }


//            /sys/platform/bunsmsg/report"+"/"+动态分区号
//            "/sys/platform/bunsmsg/ack"+"/"+动态分区号

//            /sys/platform/bunsmsg/ack/1
//            /sys/platform/bunsmsg/report/1


    public void publish(MqttMessageSendCommon sendCommon){
        if(null !=sendCommon){
            MQTTService.publish(sendCommon.getMsgTopic(),JSON.toJSONString(sendCommon));
        }

    }

//    public void publishReport(String msg){
//        MqttMessageSendCommon sendCommon = MqttMessageSendCommon.createInstance();
//        sendCommon.setMsgTopic("/sys/platform/bunsmsg/report/"+new PartitionNumUtils().getPartitionNum(mStarsConfig.getTenantId()));
//        MQTTService.publish(sendCommon.getMsgTopic(),JSON.toJSONString(sendCommon));
//    }
//
//    public void publishAck(String msg){
//        MqttMessageSendCommon sendCommon = MqttMessageSendCommon.createInstance();
//        sendCommon.setMsgTopic("/sys/platform/bunsmsg/ack/"+new PartitionNumUtils().getPartitionNum(mStarsConfig.getTenantId()));
//        MQTTService.publish(sendCommon.getMsgTopic(),JSON.toJSONString(sendCommon));
//    }


    private MqttServiceCallBack mqttServiceCallBack;

    public void setMqttServiceCallBack(com.yuchen.starssdk.MqttServiceCallBack mqttServiceCallBack) {
        this.mqttServiceCallBack = mqttServiceCallBack;
    }



    public void registOnIotPlatform(final RegistOnIotPlatformCallBack registOnIotPlatformCallBack){
        JSONObject params = new JSONObject();
        params.put("deviceName",mStarsConfig.getDeviceName());
        params.put("deviceAlias",mStarsConfig.getDeviceAlias());
        params.put("deviceSn", DeviceUtil.getDeviceSn());
        params.put("productId", mStarsConfig.getProductId());
        params.put("productKey", mStarsConfig.getProductKey());
        params.put("tenantId", mStarsConfig.getTenantId());
        params.put("bunsName",mStarsConfig.getProductName());

        if(mStarsConfig.getSubProductCount()>0){
            JSONArray array = new JSONArray();
            JSONObject ssubProduct = new JSONObject();
            ssubProduct.put("count", mStarsConfig.getSubProductCount());
            ssubProduct.put("subProductId", mStarsConfig.getSubProductId());
            array.add(ssubProduct);
            params.put("subProduct",array);
        }
        NetWork.getInstance()
                .postString()
                .url(mStarsConfig.getRegistUrl())
                .postStringContent(params.toJSONString())
                .build()
                .execute(new NetWorkStringCallBack() {
                    @Override
                    public void onError(String msg) {
                        if (null !=registOnIotPlatformCallBack){
                            registOnIotPlatformCallBack.onError(msg);
                        }
                    }

                    @Override
                    public void onResponse(String json) {
                        if (null !=registOnIotPlatformCallBack){
                            GJsonParser parser = GJsonParser.parser(json);
                            if (parser.isOk()){
                                mStarsConfig.setDeviceId(parser.parseContent().getString("deviceId"));
                                mStarsConfig.setDeviceToken(parser.parseContent().getString("deviceToken"));
//                                mStarsConfig.doSubscribeTopics();
                                registOnIotPlatformCallBack.onSuccess(parser.parseContent().toJSONString());
                            }else{
                                registOnIotPlatformCallBack.onSuccess(parser.parseMessage());
                            }

                        }
                    }
                });

    }


}
