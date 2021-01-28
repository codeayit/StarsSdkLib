package com.yuchen.starssdk;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class EbMqttAction {


    private String status;
    private Throwable e;
    private boolean reconnect;
    private IMqttDeliveryToken deliveryToken;
    private MqttMessage mqttMessage;
    private String topic;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public EbMqttAction(String status,String topic, MqttMessage mqttMessage) {
        this.status = status;
        this.mqttMessage = mqttMessage;
        this.topic = topic;
    }

    public EbMqttAction(String status, IMqttDeliveryToken deliveryToken) {
        this.status = status;
        this.deliveryToken = deliveryToken;
    }

    public EbMqttAction(String status, Throwable e) {
        this.status = status;
        this.e = e;
    }

    public EbMqttAction(String status, boolean reconnect) {
        this.status = status;
        this.reconnect = reconnect;
    }

    public boolean isReconnect() {
        return reconnect;
    }

    public void setReconnect(boolean reconnect) {
        this.reconnect = reconnect;
    }

    public IMqttDeliveryToken getDeliveryToken() {
        return deliveryToken;
    }

    public void setDeliveryToken(IMqttDeliveryToken deliveryToken) {
        this.deliveryToken = deliveryToken;
    }

    public MqttMessage getMqttMessage() {
        return mqttMessage;
    }

    public void setMqttMessage(MqttMessage mqttMessage) {
        this.mqttMessage = mqttMessage;
    }

    public EbMqttAction(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Throwable getE() {
        return e;
    }

    public void setE(Throwable e) {
        this.e = e;
    }

    @Override
    public String toString() {
        return "EbMqttAction{" +
                "status='" + status + '\'' +
                ", e=" + ((e==null)?"is null":e.getMessage()) +
                ", reconnect=" + reconnect +
                ", deliveryToken=" + deliveryToken +
                ", mqttMessage=" + mqttMessage +
                '}';
    }
}
