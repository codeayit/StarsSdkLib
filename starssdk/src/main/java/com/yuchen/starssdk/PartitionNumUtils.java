package com.yuchen.starssdk;

import java.util.Random;

/**
 * @Author: chihaojie
 * @Date: 2020/12/14 19:12
 * @Version 1.0
 * @Note
 */
public class PartitionNumUtils {

    private int DEFAULT = 1;

    private int partitionNum = DEFAULT;

    public int getPartitionNum(String tenantId){
      return   getPartitionNum(tenantId,2);
    }

    public int getPartitionNum(String tenantId,int partNum){
        try{
            int num = Integer.valueOf(tenantId);
            int even =num % 2;
            if(partNum == 2){
                if(even == 0){
                    partitionNum = 1;
                    return partitionNum;
                }else {
                    partitionNum = 2;
                    return partitionNum;
                }
            }else {
                partitionNum = new Random().nextInt(partNum);  //.randomInt(1,partNum);
//            partitionNum = Random(1,partNum);
                return partitionNum;
            }
        }catch (Exception e){
            partitionNum = new Random().nextInt(partNum);  //.randomInt(1,partNum);
//            partitionNum = Random(1,partNum);
            return partitionNum;
        }
    }
}
