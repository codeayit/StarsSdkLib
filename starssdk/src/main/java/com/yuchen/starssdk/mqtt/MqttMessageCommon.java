package com.yuchen.starssdk.mqtt;

public class MqttMessageCommon {
    enum ProtocolVersion
    {
        V2_0_0("V2.0.0"),
        V3_0_0("V3.0.0"),
        V4_0_0("V4.0.0");

        private String value;

        ProtocolVersion(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
