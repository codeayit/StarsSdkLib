package com.yuchen.starssdk;

import android.os.Build;
import android.util.Log;

import com.yuchen.starssdk.net.API;
import com.yuchen.starssdk.utils.DeviceUtil;
import com.yuchen.starssdk.utils.LogUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StarsConfig {

    private static final String TAG = "StarsConfig";
    private boolean isDebug = true;

    private String deviceId;
    private String deviceToken;
    private String osName = "android";
    private String productName = "pname";
    private String tenantId;
    private String mqttUrl;
    private String mqttUserName;
    private String mqttPassword;
    private Map<String,Integer> subscribeTopics;

    private long heartDuration;


    private String registUrl = API.directDevDynamicRegister;
    private String changeUrl = API.directDevDynamicRegister;
    private String deviceName = "设备-"+ Build.MODEL+"-"+DeviceUtil.getDeviceSn();
    private String deviceAlias = "设备-"+ Build.MODEL+"-"+DeviceUtil.getDeviceSn();
    private String productId;
    private String productKey;
    private int subProductCount = -1;
    private String subProductId;

    private DeviceType deviceType;

    private long heartBeatDuration = 30*1000;

    public StarsConfig() {
        init();
    }


    public enum DeviceType {
        Direct("直连设备",1),GatWay("网关设备",2);

        private String name;
        private int value;

        DeviceType(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }


    }

    public StarsConfig(DeviceType deviceType,String deviceId, String deviceToken, String productName, String tenantId) {
        this.deviceType = deviceType;
        this.deviceId = deviceId;
        this.deviceToken = deviceToken;
        this.productName = productName;
        this.tenantId = tenantId;
        init();
    }


    public StarsConfig(DeviceType deviceType,String deviceId, String deviceToken, String productName, String tenantId, String registUrl, String deviceName, String deviceAlias, String productId, String productKey, int subProductCount, String subProductId) {
        this.deviceType = deviceType;
        this.registUrl = registUrl;
        this.deviceName = deviceName;
        this.deviceAlias = deviceAlias;
        this.productId = productId;
        this.productKey = productKey;
        this.subProductCount = subProductCount;
        this.subProductId = subProductId;

        this.deviceId = deviceId;
        this.deviceToken = deviceToken;
        this.productName = productName;
        this.tenantId = tenantId;
        init();

    }

    public StarsConfig(DeviceType deviceType, String tenantId,String productId, String productKey) {
        this.deviceType = deviceType;
        this.tenantId = tenantId;
        this.productId = productId;
        this.productKey = productKey;
        init();
    }

    public StarsConfig( DeviceType deviceType,String tenantId,String productId, String productKey, int subProductCount, String subProductId) {
        this.deviceType = deviceType;
        this.tenantId = tenantId;
        this.productId = productId;
        this.productKey = productKey;
        this.subProductCount = subProductCount;
        this.subProductId = subProductId;
        init();
    }

    public StarsConfig(DeviceType deviceType, String productId, String productKey, int subProductCount, String subProductId) {
        this.deviceType = deviceType;
        this.productId = productId;
        this.productKey = productKey;
        this.subProductCount = subProductCount;
        this.subProductId = subProductId;
        init();
    }

    public void init(){

        subscribeTopics = new HashMap<>();
        this.mqttUrl = "tcp://device.starsaiot.com:1883";//"tcp://39.98.61.38:1883";
//        this.mqttUrl = "tcp://39.98.61.38:1883";
        this.mqttUserName = "hercules";
        this.mqttPassword = "hercules";
        if (isDebug){
            if (DeviceType.Direct.value == this.deviceType.value){
                this.registUrl = API.directDevDynamicRegisterDebug;
            }else if (DeviceType.GatWay.value== this.deviceType.value){
                this.registUrl = API.gatewayDynamicRegisterDebug;
            }
            this.changeUrl = API.gatewayChangeDebug;
        }else{
            if (DeviceType.Direct.value== this.deviceType.value){
                this.registUrl = API.directDevDynamicRegister;
            }else if (DeviceType.GatWay.value== this.deviceType.value){
                this.registUrl = API.gatewayDynamicRegister;
            }
            this.changeUrl = API.gatewayChange;
        }

    }

    public void doSubscribeTopics(){
        subscribeTopics.clear();
        //  /sys/platform/bunsmsg/${tenantId}/${业务系统名}/${deviceId}
//        String topic = "/sys/platform/bunsmsg/"+tenantId+"/"+productName+"/"+deviceId+"/"+new PartitionNumUtils().getPartitionNum(tenantId);
        String topic = "/sys/platform/bunsmsg/"+tenantId+"/"+productName+"/"+deviceId;
        subscribeTopics.put(topic,1);
        subscribeTopics.put("/device/cmd/business/"+deviceId,1);
        subscribeTopics.put("/device/cmd/business/app/"+deviceId,1);
        subscribeTopics.put("/device/cmd/business/mcu/"+deviceId,1);


    }
//    private String[] pubscriptionTopicArr = {
//            "/sys/platform/device/inform",
//            "/sys/platform/device/ack",
//    };


    public String getChangeUrl() {
        return changeUrl;
    }

    public void setChangeUrl(String changeUrl) {
        this.changeUrl = changeUrl;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
        init();
    }

    public long getHeartBeatDuration() {
        return heartBeatDuration;
    }

    public void setHeartBeatDuration(long heartBeatDuration) {
        this.heartBeatDuration = heartBeatDuration;
    }

    public String getRegistUrl() {
        return registUrl;
    }

    public void setRegistUrl(String registUrl) {
        this.registUrl = registUrl;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceAlias() {
        return deviceAlias;
    }

    public void setDeviceAlias(String deviceAlias) {
        this.deviceAlias = deviceAlias;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public int getSubProductCount() {
        return subProductCount;
    }

    public void setSubProductCount(int subProductCount) {
        this.subProductCount = subProductCount;
    }

    public String getSubProductId() {
        return subProductId;
    }

    public void setSubProductId(String subProductId) {
        this.subProductId = subProductId;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Map<String, Integer> getSubscribeTopics() {
        return subscribeTopics;
    }

    public void setSubscribeTopics(Map<String, Integer> subscribeTopics) {
        this.subscribeTopics = subscribeTopics;
    }

    public long getHeartDuration() {
        return heartDuration;
    }

    public String getClientId(){
        return "device"+"_"+productName+"_"+deviceId;
    }


    public void setHeartDuration(long heartDuration) {
        this.heartDuration = heartDuration;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        doSubscribeTopics();
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getMqttUrl() {
        return mqttUrl;
    }

    public void setMqttUrl(String mqttUrl) {
        this.mqttUrl = mqttUrl;
    }

    public String getMqttUserName() {
        return mqttUserName;
    }

    public void setMqttUserName(String mqttUserName) {
        this.mqttUserName = mqttUserName;
    }

    public String getMqttPassword() {
        return mqttPassword;
    }

    public void setMqttPassword(String mqttPassword) {
        this.mqttPassword = mqttPassword;
    }

    public boolean setMqttClientTopices(MqttAndroidClient client){
        LogUtils.d(TAG,subscribeTopics.toString());
        int[] qoes = new int[subscribeTopics.size()];
        String[] topics = new String[subscribeTopics.size()];
        int i=0;
        for (Map.Entry<String,Integer> entry:subscribeTopics.entrySet()){
            qoes[i] = entry.getValue();
            topics[i] = entry.getKey();
            i++;
        }
        try {
            client.subscribe(topics, qoes);
            return true;
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return false;
    }




}
