package com.yuchen.starssdk.net;

/**
 * Created by lny on 2017/10/16.
 */

public abstract class NetWorkStringCallBack  {

    public void onBefore(){

    }
    public abstract void onError(String msg);

    public abstract void onResponse(String json);

    public void onAfter(){

    }
}
