package com.yuchen.starssdk.mqtt;



import com.yuchen.starssdk.StarsConstent;
import com.yuchen.starssdk.StarsSdkHelper;
import com.yuchen.starssdk.utils.DateTimeUtil;

import java.io.Serializable;

/**
 * 定义由平台发出去的消息的结构。 SERVER ->
 */
public class MqttMessageSendCommon extends MqttMessageCommon implements Serializable {

    private static final long serialVersionUID = 686478657449929724L;

    public static MqttMessageSendCommon createInstance(String msgId) {

        return new MqttMessageSendCommon(msgId);
    }

    public static MqttMessageSendCommon createInstance() {
        String msgId = StarsSdkHelper.getInstence().getmStarsConfig().getDeviceId()+"_"+ System.currentTimeMillis();
        return createInstance(msgId);
    }

    public MqttMessageSendCommon() {
    }

    /**
     * 消息分为三部分：
     * 1、固定头部   ：  消息版本号、消息标识符、终端类型、消息类型、消息接收者类型、消息接收者Id、消息的发送者类型、消息的发送方id、消息发送时间
     * 2、可变头部   :    租户id、产品id、设备id、
     * 3、有效载荷   :    消息体
     */

    private static final String DEFAULT_VERSION=ProtocolVersion.V4_0_0.getValue();

    private String msgVersion = DEFAULT_VERSION;
    private int id;

    //增加消息标识符

    //    @ApiModelProperty("消息标识符")
    private String msgId;

    //    @ApiModelProperty("消息主题")
//    @JSONField(serialize = false)
    private String msgTopic;


    //    @ApiModelProperty("终端类型")
    private String endType;
    //    @ApiModelProperty("指令键")
    private String msgKey;     //告诉消息的接收端，它要执行什么动作

    //    @ApiModelProperty("消息类型")
    private String msgType;

    //    @ApiModelProperty("业务内容")
    private String msgBody;    //消息的接收端，执行动作，所需要的数据

    //    @ApiModelProperty("是否需要应答")
    private Boolean needAck;

    //    @ApiModelProperty("租户id")
    private String tenantId;
    //    @ApiModelProperty("产品id")
    private String productId;
    //    @ApiModelProperty("设备id")
    private String deviceId;
    //    @ApiModelProperty("接收者类型")
    private String receiverType;
    //    @ApiModelProperty("接受者id")
    private String receiverId;

    //    @ApiModelProperty("消息发送者类型： 1、设备、2、业务应用：租户id")
    private String senderType;
    //    @ApiModelProperty("消息发送者id")
    private String senderId;

    private String bunsName;



    //    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
//    @ApiModelProperty("发送时间")
    private String sendTime;

    public MqttMessageSendCommon(String msgId) {
        this.sendTime = DateTimeUtil.formatDateTime(System.currentTimeMillis(),DateTimeUtil.data_format_str_3);
        this.deviceId = StarsSdkHelper.getInstence().getmStarsConfig().getDeviceId();
        this.msgId = msgId;
        this.tenantId =StarsSdkHelper.getInstence().getmStarsConfig().getTenantId();
        this.bunsName = StarsSdkHelper.getInstence().getmStarsConfig().getProductName();
        this.productId = StarsSdkHelper.getInstence().getmStarsConfig().getProductId();
        this.senderId = StarsSdkHelper.getInstence().getmStarsConfig().getDeviceId();

    }

    public String getBunsName() {
        return bunsName;
    }

    public void setBunsName(String bunsName) {
        this.bunsName = bunsName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void newMsgId() {
        this.msgId = this.deviceId + "_" + System.currentTimeMillis();
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

    public String getMsgTopic() {
        return msgTopic;
    }

    public void setMsgTopic(String msgTopic) {
        this.msgTopic = msgTopic;
    }

    public String getEndType() {
        return endType;
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

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public Boolean getNeedAck() {
        return needAck;
    }

    public void setNeedAck(Boolean needAck) {
        this.needAck = needAck;
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

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "MqttMessageSendCommon{" +
                "msgVersion='" + msgVersion + '\'' +
                ", msgId='" + msgId + '\'' +
                ", msgTopic='" + msgTopic + '\'' +
                ", endType='" + endType + '\'' +
                ", msgKey='" + msgKey + '\'' +
                ", msgType='" + msgType + '\'' +
                ", msgBody='" + msgBody + '\'' +
                ", needAck=" + needAck +
                ", tenantId='" + tenantId + '\'' +
                ", productId='" + productId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", receiverType='" + receiverType + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", senderType='" + senderType + '\'' +
                ", senderId='" + senderId + '\'' +
                ", sendTime=" + sendTime +
                '}';
    }
}
