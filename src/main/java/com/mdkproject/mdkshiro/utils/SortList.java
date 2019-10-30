package com.mdkproject.mdkshiro.utils;

//SortList.java

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class SortList<E>{

  

  public void Sort(List<E> list, final String method){

     //排序

     Collections.sort(list, new Comparator() {        

         public int compare(Object a, Object b) {

         int ret = 0;

         try{

             Method m1 = ((E)a).getClass().getMethod(method, null);

             Method m2 = ((E)b).getClass().getMethod(method, null);

             ret = m1.invoke(((E)a), null).toString().compareTo(m2.invoke(((E)b), null).toString());                  

         }catch(NoSuchMethodException ne){

             System.out.println(ne);

            }catch(IllegalAccessException ie){

                System.out.println(ie);

            }catch(InvocationTargetException it){

                System.out.println(it);

            }

         return ret;

         }

      });

  }


    /**
     * 自动生成编号
     * @param prefix  前缀，往往是一串字符串
     * @param nowNum  当前要生成的数字
     * @return
     */
    public static String getConteactNo(String prefix,int nowNum ) {

        StringBuilder builder = new StringBuilder();
        StringBuilder num = new StringBuilder();
        AtomicInteger count = new AtomicInteger(nowNum);
        // 4位数字的采取编号处理。9999的情况下从001开始采取。
        if (count.get() > 9999) {
            count = new AtomicInteger(1);
        }

        // 采用4位数的数字进行序号处理。
        if (count.get() < 10) {
            num.append("00").append(count.getAndIncrement());
        } else if (count.get() >= 100) {
            num.append(count.getAndIncrement());
        } else {
            num.append("0").append(count.getAndIncrement());
        }

        // 组合。
        builder.append(prefix);
        builder.append(num);

        return builder.toString();

    }





}