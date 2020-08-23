package com.Utils;

/**
 * @author Jianshu
 * @time 20200823
 */
import java.util.*;

/*
 *该类主要是两个Map工具类
 */
public class MapUtils {
    //将Map根据value的大小进行降序排列
    public static List<Map.Entry<String, Integer>> MapSort(Map<String,Integer> map){
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet()); //转换为list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        return list;
    }

    //List进行统计，并以Map<K,V>形式保存
    public static Map<String,Integer> MapStatistics(List<String> list){
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (String temp : list) {
            Integer count = map.get(temp);
            map.put(temp, (count == null) ? 1 : count + 1);
        }
        return map;
    }
}
