package com.senon.leanstoragedemo.util.timeselectorutils;

import java.util.ArrayList;
import java.util.List;


public class ZJWheelItem {
    public String value;
    public String text;
    public ZJWheelItem(String value, String text){
        this.value = value;
        this.text = text;
    }

    /**
     * 根据数组获取List
     * @param arr
     * @return
     */
    public static List<ZJWheelItem> listFromStringArray(String[] arr){
        List<ZJWheelItem> list = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            list.add(new ZJWheelItem(arr[i],arr[i]));
        }
        return list;
    }

    /**
     * 根据数字获取List
     * @param minNum 最小值
     * @param maxNum 最大值
     * @return
     */
    public static List<ZJWheelItem> listFromNum(int minNum, int maxNum){
        List<ZJWheelItem> list = new ArrayList<>();
        for (int i = minNum; i <= maxNum; i++) {
            list.add(new ZJWheelItem(""+i,""+i));
        }
        return list;
    }

}
