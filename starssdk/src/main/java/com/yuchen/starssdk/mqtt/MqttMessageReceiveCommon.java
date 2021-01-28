package com.yuchen.starssdk.mqtt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * @Author: chihaojie
 * @Date: 2020/7/14 19:09
 * @Version 1.0
 * @Note    由外部发到平台的消息，  ->  SERVER
 */

public class MqttMessageReceiveCommon extends MqttMessageCommon{

    private static final long serialVersionUID = 686478657449929724L;


    private static  final String DEFAULT_VERSION=ProtocolVersion.V3_0_0.getValue();

    private String msgVersion=DEFAULT_VERSION;

    /**
     * 消息分为三部分：
     * 1、固定头部   ：  消息版本号、消息标识符、终端类型、消息类型、消息发送者类型、消息发送者Id、消息发送时间
     * 2、可变头部   :
     * 3、有效载荷
     */

    //增加消息标识符

//    @ApiModelProperty("消息标识符,64位")
    private String msgId;

//    @ApiModelProperty("终端类型")
    private String endType;


//    @ApiModelProperty("指令键")
    private String msgKey;     //告诉消息的接收端，它要执行什么动作，如果是"ack"，则表明，这是一条响应消息
                               //其他如：heartBeat 、attrInform、alarmInform
    private String msgTopic;

//    @ApiModelProperty("指令类型")
    private String msgType;     //消息类型：

    private boolean needAck;


//    @ApiModelProperty("携带的数据")
    private String msgBody;    //消息的接收端，执行动作，所需要的数据





//    @ApiModelProperty("租户id")
    private String tenantId;
//    @ApiModelProperty("产品id")
    private String productId;
//    @ApiModelProperty("设备id")
    private String deviceId;
//    @ApiModelProperty("消息发送者类型： 1、设备、2、业务应用：租户id")
    private String senderType;
//    @ApiModelProperty("消息发送者id")
    private String senderId;

//    @ApiModelProperty("接收者类型")
    private String receiverType;
//    @ApiModelProperty("接受者id")
    private String receiverId;

//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @ApiModelProperty("发送时间")
    private Date sendTime;

    public MqttMessageReceiveCommon() {
        this.sendTime = Calendar.getInstance().getTime();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static String getDefaultVersion() {
        return DEFAULT_VERSION;
    }

    public String getMsgVersion() {
        return msgVersion;
    }

    public void setMsgVersion(String msgVersion) {
        this.msgVersion = msgVersion;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getEndType() {
        return endType;
    }

    public String getMsgTopic() {
        return msgTopic;
    }

    public void setMsgTopic(String msgTopic) {
        this.msgTopic = msgTopic;
    }

    public void setEndType(String endType) {
        this.endType = endType;
    }

    public String getMsgKey() {
        return msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public boolean isNeedAck() {
        return needAck;
    }

    public void setNeedAck(boolean needAck) {
        this.needAck = needAck;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public JSONObject getMsgBodyJSON(){
        try{
            return JSON.parseObject(getMsgBody());
        }catch (Exception e){
            return null;
        }

    }

    @Override
    public String toString() {
        return "MqttMessageReceiveCommon{" +
                "msgVersion='" + msgVersion + '\'' +
                ", msgId='" + msgId + '\'' +
                ", endType='" + endType + '\'' +
                ", msgKey='" + msgKey + '\'' +
                ", msgTopic='" + msgTopic + '\'' +
                ", msgType='" + msgType + '\'' +
                ", needAck=" + needAck +
                ", msgBody='" + msgBody + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", productId='" + productId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", senderType='" + senderType + '\'' +
                ", senderId='" + senderId + '\'' +
                ", receiverType='" + receiverType + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", sendTime=" + sendTime +
                '}';
    }
}
