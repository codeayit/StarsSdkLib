package com.yuchen.starssdk.net;

import java.util.HashMap;

public interface GlobalFilter {
    void onPreResponse(int code, String json);
    void onPreRequest(String url, HashMap<String, String> headers, HashMap<String, String> params);
}
