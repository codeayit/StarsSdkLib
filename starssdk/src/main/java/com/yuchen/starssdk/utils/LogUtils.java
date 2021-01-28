package com.yuchen.starssdk.utils;

import android.util.Log;

public class LogUtils {
//    public static void d(String content, Object... args) {
////        for (int i = 0; i < Thread.currentThread().getStackTrace().length; i++) {
//            String realContent = getContent(content, 0, args);
//            Log.d("default", realContent);
////        }
//    }

    public static void d(String content, Object... args) {
        Log.d("stars-sdk", getContent(content, 4, args));
    }

    public static void d(String tag, String content, Object... args) {
        Log.d(tag, getContent(content, 4, args));
    }

    private static String getNameFromTrace(StackTraceElement[] traceElements, int place) {
        StringBuilder taskName = new StringBuilder();
        if (traceElements != null && traceElements.length > place) {
            StackTraceElement traceElement = traceElements[place];
            taskName.append(traceElement.getMethodName());
            taskName.append("(").append(traceElement.getFileName()).append(":").append(traceElement.getLineNumber()).append(")");
        }
        return taskName.toString();
    }

    private static String getContent(String msg, int place, Object... args) {
        try {
            String sourceLinks = getNameFromTrace(Thread.currentThread().getStackTrace(), place);
            return sourceLinks + String.format(msg, args);
        } catch (Throwable throwable) {
            return msg;
        }
    }
}
