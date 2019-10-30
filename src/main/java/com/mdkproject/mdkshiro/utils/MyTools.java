package com.mdkproject.mdkshiro.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: Liberal-World
 * @Date: 2019-03-30 15:21
 * @Description:时间工具类
 * @Version 1.0
 */
public class MyTools {
    //时间+随机数
    public static String getDateR(){
        double rd = Math.random()*1000000;
        int rds = (int)rd;
        Date date=new Date();
        DateFormat format=new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return format.format(date)+rds;
    }
    //随机数
    public static String getR(){
        double rd = Math.random()*1000000;
        int rds = (int)rd;
        return rds+"";
    }
    //当前时间
    public static String getTime(){
        Date date = new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String getUuid(){
        String uuid = UUID.randomUUID().toString();//获取UUID并转化为String对象
        uuid = uuid.replace("-","");
        return uuid;
    }

    /**
     * 获取随机盐值
     *
     * @param length
     * @return
     */
    public static String getRandomSalt(int length) {
        return getRandomString(length);
    }
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < length; ++i) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 将json字符串转为Map结构
     * 如果json复杂，结果可能是map嵌套map
     * @param jsonStr 入参，json格式字符串
     * @return 返回一个map
     */
    public static Map<String, Object> json2Map(String jsonStr) {
        Map<String, Object> map = new HashMap<>();
        if(jsonStr != null && !"".equals(jsonStr)){
            //最外层解析
            JSONObject json = JSONObject.fromObject(jsonStr);
            for (Object k : json.keySet()) {
                Object v = json.get(k);
                //如果内层还是数组的话，继续解析
                if (v instanceof JSONArray) {
                    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                    Iterator<JSONObject> it = ((JSONArray) v).iterator();
                    while (it.hasNext()) {
                        JSONObject json2 = it.next();
                        list.add(json2Map(json2.toString()));
                    }
                    map.put(k.toString(), list);
                } else {
                    map.put(k.toString(), v);
                }
            }
            return map;
        }else{
            return null;
        }
    }

    /**
     * 生成随机数字和字母
     */
    public static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }



    /**
     * 隐藏并替换字符串中所有的手机号
     * @param contentStr
     * @return
     */
    public static String hideAllPhoneNum(String contentStr){
        Pattern pattern =Pattern.compile("((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}");
        Matcher matcher = pattern.matcher(contentStr);
        StringBuffer sb = new StringBuffer();
        try {
            while(matcher.find()) {
                String phoneStr = matcher.group();
                phoneStr = phoneStr.substring(0, 3) + "****" + phoneStr.substring(7, phoneStr.length());
                matcher.appendReplacement(sb,phoneStr);
            }
            matcher.appendTail(sb);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 隐藏并替换所有的身份证号码
     * @param contentStr
     * @return
     */
    public static String hideAllIdCardNum(String contentStr){
        Pattern pattern = Pattern.compile("(\\d{6})(19|20)(\\d{2})(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])(\\d{3})(\\d|X|x)");
        Matcher matcher = pattern.matcher(contentStr);
        StringBuffer sb = new StringBuffer();
        try {
            while(matcher.find()) {
                String idCardStr = matcher.group();
                int len=idCardStr.length();
                if(len>=9){
                    idCardStr =  idCardStr.replaceAll("(.{"+(len<12?3:6)+"})(.*)(.{4})", "$1" + "****" + "$3");
                }
                matcher.appendReplacement(sb,idCardStr);
            }
            matcher.appendTail(sb);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    public static void main(String[] args) {

        System.out.println(getStringRandom(16));
    }



}
