package com.yuchen.starssdk;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface MqttServiceCallBack {
    /**
     * 链接成功
     */
    void connectSuccess();
    /**
     * 链接失败
     */
    void connectFail(Throwable e);
    /**
     * 链接丢失
     */
    void connectLost(Throwable arg0);
    /**
     * 链接断开
     */
    void disconnected();
    /**
     * 消息发送成功
     */
    void messageDeliveryComplete(IMqttDeliveryToken arg0);
    /**
     * 收到消息
     */
    void messageArrived(String topic, MqttMessage message);

}
