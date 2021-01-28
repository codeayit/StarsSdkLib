package com.yuchen.starssdkdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuchen.starssdk.MqttServiceCallBack;
import com.yuchen.starssdk.RegistOnIotPlatformCallBack;
import com.yuchen.starssdk.StarsConfig;
import com.yuchen.starssdk.StarsSdkHelper;
import com.yuchen.starssdk.mqtt.MqttMessageReceiveCommon;
import com.yuchen.starssdk.utils.LogUtils;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final StarsConfig config = new StarsConfig(StarsConfig.DeviceType.Direct, IotConfigConstant.tenantId,IotConfigConstant.productId,IotConfigConstant.productKey);
        config.setDebug(false);


        StarsSdkHelper.getInstence().init(this,config);
        StarsSdkHelper.getInstence().registOnIotPlatform(new RegistOnIotPlatformCallBack() {
            @Override
            public void onSuccess(String jsonStr) {
                LogUtils.d(jsonStr);
                JSONObject deviceInfo = JSON.parseObject(jsonStr);
                config.setDeviceToken(deviceInfo.getString("deviceToken"));
                config.setDeviceId(deviceInfo.getString("deviceId"));
//                getDeviceRegistInfo(true);
                StarsSdkHelper.getInstence().connectMqtt();
            }

            @Override
            public void onError(String msg) {
                LogUtils.d(msg);
                RegistOnIotPlatformCallBack callBack = this;
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        StarsSdkHelper.getInstence().registOnIotPlatform(callBack);
//                    }
//                },5000);
            }
        });


        StarsSdkHelper.getInstence().setMqttServiceCallBack(new MqttServiceCallBack() {
            @Override
            public void connectSuccess() {
               LogUtils.d("connectSuccess");
            }

            @Override
            public void connectFail(Throwable e) {
                LogUtils.d("connectFail " + e.getMessage());
            }

            @Override
            public void connectLost(Throwable e) {
                LogUtils.d("connectLost " + e.getMessage());
            }

            @Override
            public void disconnected() {
                LogUtils.d("disconnected ");
            }

            @Override
            public void messageDeliveryComplete(IMqttDeliveryToken arg0) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {

            }
        });



    }
}