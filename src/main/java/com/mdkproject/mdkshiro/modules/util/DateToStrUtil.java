package com.mdkproject.mdkshiro.modules.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: mdk-shiro->DateToStrUtil
 * @description: 日期转换工具类
 * @author: lizhen
 * @create: 2019-07-15 16:33
 **/
public class DateToStrUtil {
    //格式转换 19981205---1998-12-05
    public static String datetoStr(String datestr){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = format.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        return format1.format(date);
    }

    //Date转string
    public static String dateToString(Date date,String gs){
        SimpleDateFormat format = new SimpleDateFormat(gs);
        String format1 = format.format(date);
        return format1;
    }


    //Str 转Data
    public static Date StrToDate(String str){
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = null;
        try {
            parse = format1.parse(str);
            return parse;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }


    //那么如何处理000001自增1变为000002,顺延数
    //%nd 输出的整型宽度至少为n位，右对齐，%5d即宽度至少为5位，位数大于5则输出实际位数
    //%0nd 用得比较多，表示输出的整型宽度至少为n位，不足n位用0填充
    //printf（"%05d",1）输出：00001
    //printf（"%5d",1）输出：****1（*为空格）
    //6位
    public static  String getNewEquipmentNo6(String autonumber){
        //初始值
        String newEquipmentNo = "000001";
        if (autonumber != null && !autonumber.isEmpty()) {
            int newEquipment = Integer.parseInt(autonumber)+1;
            newEquipmentNo = String.format("%06d",newEquipment);
        }
        return newEquipmentNo;
    }
    //5位
    public static  String getNewEquipmentNo5(String autonumber){
        //初始值
        String newEquipmentNo = "00001";
        if (autonumber != null && !autonumber.isEmpty()) {
            int newEquipment = Integer.parseInt(autonumber)+1;
            newEquipmentNo = String.format("%05d",newEquipment);
        }
        return newEquipmentNo;
    }
    //7位
    public static  String getNewEquipmentNo7(String autonumber){
        //初始值
        String newEquipmentNo = "0000001";
        if (autonumber != null && !autonumber.isEmpty()) {
            int newEquipment = Integer.parseInt(autonumber)+1;
            newEquipmentNo = String.format("%07d",newEquipment);
        }
        return newEquipmentNo;
    }

    //获取年月
    public static String getYearMonth(){
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyMM");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    //获取当前年份
    public static String getYear(){
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    //获取现在时间（yyyy-MM-dd）
    public static String getStringDateShort(){
        Date currentTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(currentTime);
        return dateString;
    }
    //Java计算两个字符串日期之间的天数差（yyy-MM-dd）

    /**
     * a 当前时间
     * b 创建时间
     */
    public static Long between_days(String a, String b) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// 自定义时间格式

        Calendar calendar_a = Calendar.getInstance();// 获取日历对象
        Calendar calendar_b = Calendar.getInstance();

        Date date_a = null;
        Date date_b = null;

        try {
            date_a = simpleDateFormat.parse(a);//字符串转Date
            date_b = simpleDateFormat.parse(b);
            calendar_a.setTime(date_a);// 设置日历
            calendar_b.setTime(date_b);
        } catch (ParseException e) {
            e.printStackTrace();//格式化异常
        }

        long time_a = calendar_a.getTimeInMillis();
        long time_b = calendar_b.getTimeInMillis();

        long between_days = (time_b - time_a) / (1000 * 3600 * 24);//计算相差天数

        return between_days;
    }


    /**
     * @auther: lizhen
     * @date: 2019-06-19 11:39
     * 功能描述: 获取一周日期
     */
    public static List<String> getDateToWeek(Date date){
        List<String> dateWeekList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = "";
        //flag用来存取与当天日期的相差数
        int flag = 0;
        for(int i=1;i<8;i++){
            //新建日历
            Calendar cal = Calendar.getInstance();
            //在日历中找到当前日期
            cal.setTime(date);
            //当前日期时本周第几天，默认按照西方惯例上周星期天为第一天
            flag = -cal.get(Calendar.DAY_OF_WEEK);
            //根据循环。当天与上周星期天和本周一到周五相差的天数
            cal.add(Calendar.DATE, flag+i);
            //转化格式
            time = sdf.format(cal.getTime());
            //存入list
            dateWeekList.add(time);
        }
        return dateWeekList;
    }


    /**
     * @auther: lizhen
     * @date: 2019-06-19 13:50
     * 功能描述: 更具当前日期，获取周一到周五日期
     */
    public static List<String> getWeekDay() {
        List<String> list = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time;
        for (int i=0;i<5;i++){
            Calendar calendar = Calendar.getInstance();
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                calendar.add(Calendar.DAY_OF_WEEK, -1);
            }
            calendar.getTime();//在日历中找到当前日期
            calendar.add(Calendar.DATE, 1);
            //日期格式
            time = dateFormat.format(calendar);
            list.add(time);
        }

        return list;
    }


    /**
     * 获取当前日期&顺延一年后的日期
     * */
    public static String getOneYearData(){
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR,1);
        date = calendar.getTime();
        String newData = dateToString(date, "yyyy-MM-dd");
        return newData;
    }


    /**
     * 2个日期比较
     */
    public static int bijiaoDate(String date1,String date2){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int compareTo = 0;
        try {
            Date time1 = format.parse(date1);
            Date time2 = format.parse(date2);
            compareTo = time1.compareTo(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return compareTo;
    }
    public static boolean bijiaoDate2(String date1,String date2){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        boolean result = false;
        try {
            Date time1 = format.parse(date1);
            Date time2 = format.parse(date2);
            result = time1.before(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

/**
      * 判断时间是否在时间段内
      * @param nowTime
      * @param beginTime
      * @param endTime
      * @return
      */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        //设置当前时间
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        //设置开始时间
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        //设置结束时间
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        //处于开始时间之后，和结束时间之前的判断
        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }


    //判断当前时间是上午还是下午(结果为“0”是上午     结果为“1”是下午)
    public static int nowTime(){
        GregorianCalendar ca = new GregorianCalendar();
        int i = ca.get(GregorianCalendar.AM_PM);
        return i;
    }



    //测试
    public static void main(String[] args) {


        System.out.println(nowTime());




    }



}
