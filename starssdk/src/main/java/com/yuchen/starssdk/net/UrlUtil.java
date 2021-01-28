package com.yuchen.starssdk.net;

import android.net.Uri;

public class UrlUtil {


    public static String cutFileSuffixFromUrl(String url) {
        return url.split("\\.")[url.split("\\.").length - 1];
    }

    public static String cutFileNameFromUrl(String url) {
        return url.split("\\/")[url.split("\\/").length - 1];
    }

    public static String urlEncode(String url) {
        String urlEncode = Uri.encode(url);
        urlEncode = urlEncode.replace("%3A", ":");
        urlEncode = urlEncode.replace("%2F", "/");
        return urlEncode;
//        try {
//            String urlEncode = URLEncoder.encode(url,"UTF-8");
//            urlEncode = urlEncode.replace("%3A", ":");
//            urlEncode = urlEncode.replace("%2F", "/");
//            return urlEncode;
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return null;
    }
}
