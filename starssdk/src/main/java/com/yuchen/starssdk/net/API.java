package com.yuchen.starssdk.net;

public interface API {


    String gatewayDynamicRegister = "http://device.starsaiot.com:9600/hercules/open/api/v1/device/business/gateway/dynamicRegister";
    String directDevDynamicRegister = "http://device.starsaiot.com:9600/hercules/open/api/v1/device/business/directDev/dynamicRegister";
    String gatewayChange = "http://device.starsaiot.com:9600/hercules/open/api/v1/device/business/gateway/change";

    String gatewayDynamicRegisterDebug = "http://iot.iperfumetech.com:9600/hercules/open/api/v1/device/business/gateway/dynamicRegister";
    String directDevDynamicRegisterDebug = "http://iot.iperfumetech.com:9600/hercules/open/api/v1/device/business/directDev/dynamicRegister";
    String gatewayChangeDebug = "http://iot.iperfumetech.com:9600/hercules/open/api/v1/device/business/gateway/change";

}
