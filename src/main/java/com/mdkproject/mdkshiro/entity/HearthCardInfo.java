package com.mdkproject.mdkshiro.entity;

import lombok.Data;

/**
 * @Author：lizhen
 * @Date：2019-07-15
 * @Description：
 */
@Data
public class HearthCardInfo {
    /**
     * 健康证Id
     */
    private Integer healthId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别
     */
    private String gender;

    /**
     * 体检结果0合格1不合格
     */
    private String medical;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 健康证号
     */
    private String healthNum;

    /**
     * 健康证发证日期
     */
    private String startTime;

    /**
     * 健康证有效期
     */
    private String endTime;

    /**
     * 健康证打印时间
     */
    private String printTime;

    /**
     * 健康证打印状态0未打印1已打印
     */
    private String printStatus;

    /**
     * 二维码，条形码
     */
    private String qrCode;

    /**
     * 体检机构编号（区域，地址，公章）
     */
    private String hospitalId;

    /**
     * 
     */
    private String createTime;

    /**
     * 
     */
    private String updateTime;

    /**
     * 体检审核通过时间
     */
    private String yuliu1;

    /**
     * 领取电子健康证0未领取1领取
     */
    private String yuliu2;

    /**
     * 
     */
    private String yuliu3;

    /**
     * 
     */
    private String yuliu4;

    /**
     * 
     */
    private String yuliu5;


}