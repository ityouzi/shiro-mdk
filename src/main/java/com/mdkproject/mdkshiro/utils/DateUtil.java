package com.mdkproject.mdkshiro.utils;

import java.text.DecimalFormat;

/**
 * @program: mdk-shiro->DateUtil
 * @description: 日期工具类
 * @author: lizhen
 * @create: 2019-07-15 13:36
 **/
public class DateUtil {




    public static int[] averageANum(double num, int groupCount){

        DecimalFormat df = new DecimalFormat("#.0");
        String average_str = df.format(num/groupCount);
        String average_str_oneDecimal = average_str.substring(0, average_str.indexOf('.') + 2);
        int[] two_parts = splitADoubleNumByDot(Double.valueOf(average_str_oneDecimal));
        int inteter_part = two_parts[0];
        int decimal_part = two_parts[1];
        if(decimal_part > 5){
            inteter_part++;
        }

        int[] arr = new int[groupCount];
        for(int i = 0; i < groupCount - 1; i++){
            arr[i] = inteter_part;
        }
        arr[groupCount - 1] = (int) (num - inteter_part*(groupCount - 1));
        return arr;

    }

    public static int[] splitADoubleNumByDot(double num){
        String str = Double.toString(num);
        String[] two_parts = str.split("\\.");
        int part1 = Integer.valueOf(two_parts[0]);
        int part2 = Integer.valueOf(two_parts[1]);
        int[] ints = {part1, part2};
        return ints;
    }

    public static void main(String[] args) {
        int[] ints = averageANum(90, 2);

    }


}
