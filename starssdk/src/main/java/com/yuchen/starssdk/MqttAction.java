package com.yuchen.starssdk;

public interface MqttAction  extends MqttStatus{
    String messageArrived = "messageArrived";
    String deliveryComplete = "deliveryComplete";
}
