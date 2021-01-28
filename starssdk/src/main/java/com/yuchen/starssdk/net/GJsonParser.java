package com.yuchen.starssdk.net;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class GJsonParser extends JsonParser {

    public static final int OK_CODE = 200;

    public GJsonParser(String json) {
        super(json);
    }

    public static GJsonParser parser(String json){
        return new GJsonParser(json);
    }

    public int parseCode(){
        return parseInteger(jo,"code");
    }

    public boolean isOk(){
        return parseCode() == OK_CODE;
    }


    public JSONObject parseContent(){
        return jo.getJSONObject("content");
    }
    public JSONArray parseContentArray(){
        return jo.getJSONArray("content");
    }

    public String parseLincnseTxt(){
        return jo.getString("content");
    }


    public String parseMessage(){
        return jo.getString("message");
    }

//    public AppVersion parseAppVersion() {
//        return parseObject(jo, "content", AppVersion.class);
//    }




}
