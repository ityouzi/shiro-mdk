package com.mdkproject.mdkshiro.base.aop;


import java.lang.annotation.*;

@Target(ElementType.METHOD)                 //作用到方法上
@Retention(RetentionPolicy.RUNTIME)         //运行时有效
@Documented
@Inherited
public @interface NoRepeatSumit {

    /**
     * 设置请求锁定时间
     */
//    int lockTime() default 10;

    String prefix() default "prefix:";

}
