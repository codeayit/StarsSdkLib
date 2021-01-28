package com.yuchen.starssdk.utils;

import android.app.ActivityManager;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

public class DeviceUtil {

    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        Intent i = manager.getLaunchIntentForPackage(packageName);
        if (i == null) {
            return false;
        }
        return true;
    }


    public static String getDeviceSn() {
        //设备sn
        try {

            StringBuffer fileData = new StringBuffer(1024);
            BufferedReader reader = new BufferedReader(new FileReader("/sys/class/net/eth0/address"));
            char[] buf = new char[1024];
            int numRead = 0;

            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
            }
            reader.close();
//            return fileData.toString();
            return fileData.toString().substring(0, 17);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    // 获得可用的内存
    public static String getMemoryAvail(Context mContext) {
        if (mContext == null) {
            return "unknow";
        }
        // 得到ActivityManager
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        // 创建ActivityManager.MemoryInfo对象

        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);

        // 取得剩余的内存空间
        return getUnit(Float.valueOf(mi.availMem));
    }

    // 获得总内存
    public static String getMemoryAll() {
        long mTotal;
        // /proc/meminfo读出的内核信息进行解释
        String path = "/proc/meminfo";
        String content = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path), 8);
            String line;
            if ((line = br.readLine()) != null) {
                content = line;
            }
            // beginIndex
            int begin = content.indexOf(':');
            // endIndex
            int end = content.indexOf('k');
            // 截取字符串信息

            content = content.substring(begin + 1, end).trim();
            mTotal = Integer.parseInt(content) * 1024;
        } catch (Exception e) {
            e.printStackTrace();
            mTotal = 0;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return getUnit(mTotal);
    }

    public static String getStorageAll() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());

//        //存储块总数量
//        long blockCount = statFs.getBlockCountLong();
//        //块大小
//        long blockSize = statFs.getBlockSizeLong();
//        //可用块数量
//        long availableCount = statFs.getAvailableBlocksLong();
//        //剩余块数量，注：这个包含保留块（including reserved blocks）即应用无法使用的空间
//        long freeBlocks = statFs.getFreeBlocksLong();
        //这两个方法是直接输出总内存和可用空间，也有getFreeBytes
        //API level 18（JELLY_BEAN_MR2）引入
        long totalSize = statFs.getTotalBytes();
//        long availableSize = statFs.getAvailableBytes();
        return getUnit(totalSize);
    }



    /**
     * API 26 android O
     * 获取总共容量大小，包括系统大小
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getTotalSize(Context context, String fsUuid) {
        try {
            UUID id;
            if (fsUuid == null) {
                id = StorageManager.UUID_DEFAULT;
            } else {
                id = UUID.fromString(fsUuid);
            }
            StorageStatsManager stats = context.getSystemService(StorageStatsManager.class);
            return stats.getTotalBytes(id);
        } catch (NoSuchFieldError | NoClassDefFoundError | NullPointerException | IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getStorageAvail() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long availableSize = statFs.getAvailableBytes();
        return getUnit(availableSize);
    }

    public static int getStorageAvailAlarm() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long availableSize = statFs.getAvailableBytes();
        if(availableSize < 1073741824){
            return -1;
        }
        return 0;
    }



    private static String[] units = {"B", "KB", "MB", "GB", "TB"};

    /**
     * 单位转换
     */
    private static String getUnit(float size) {
        int index = 0;
        while (size > 1024 && index < 4) {
            size = size / 1024;
            index++;
        }
        return String.format(Locale.getDefault(), "%.2f%s", size, units[index]);
    }
}
