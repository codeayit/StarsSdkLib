package com.yuchen.starssdk.mqtt;


import com.yuchen.starssdk.PartitionNumUtils;
import com.yuchen.starssdk.StarsSdkHelper;

/**
 * MQTT 发布消息的topic
 */
public class PubTopic {
    /**
     * 设备遥测属性
     */
    public static String getInform(){
        return "/sys/platform/device/inform";
    }

    /**
     * 心跳实时日志
     * @return
     */
    public static String getRealTime(){
        return "/sys/platform/device/realtime";
    }

//    /sys/platform/bunsmsg/realtime+"/"+动态分区号
    /**
     * 心跳实时日志
     * @return
     */
    public static String getHeartbeat(){
        return "/sys/platform/bunsmsg/heartbeat/"+new PartitionNumUtils().getPartitionNum(StarsSdkHelper.getInstence().getmStarsConfig().getTenantId());
    }

    /**
     * 设备端相应指令
     * @return
     */
    public static String getAck(){
        return "/sys/platform/device/ack";
    }

    /**
     * 业务系统回应
     * @return
     */
    public static String getBussAck(){
        return "/sys/platform/bunsmsg/ack/"+new PartitionNumUtils().getPartitionNum(StarsSdkHelper.getInstence().getmStarsConfig().getTenantId());
    }

    /**
     * 业务系统回应
     * @return
     */
    public static String getBussReport(){
        return "/sys/platform/bunsmsg/report/"+new PartitionNumUtils().getPartitionNum(StarsSdkHelper.getInstence().getmStarsConfig().getTenantId());
    }

}
