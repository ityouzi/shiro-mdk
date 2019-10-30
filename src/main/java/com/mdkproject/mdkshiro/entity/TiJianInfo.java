package com.mdkproject.mdkshiro.entity;

import lombok.Data;

/**
 * @Author：lizhen
 * @Date：2019-07-15
 * @Description：
 */
@Data
public class TiJianInfo {
    /**
     * 主键ID
     */
    private Integer tjId;

    /**
     * 体检编号
     */
    private String number;

    /**
     * 电话
     */
    private String telphone;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 身份证编号
     */
    private String idcardNum;

    /**
     * 健康证编号
     */
    private String hearthcardNum;

    /**
     * 体检人员单位名称
     */
    private String company;

    /**
     * 通讯地址
     */
    private String adress;

    /**
     * 身份证照片
     */
    private String idcardPhoto;

    /**
     * 办证日期
     */
    private String startdate;

    /**
     * 失效日期
     */
    private String enddate;

    /**
     * 办证人员
     */
    private String person;

    /**
     * 体检审核结果0未审核，1审核通过，2审核不通过
     */
    private String status;

    /**
     * 体检医院编号
     */
    private String deptNum;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 监控体检项目添加状态1已添加0未添加
     */
    private String yuliu1;

    /**
     * 体检日期
     */
    private String yuliu2;

    /**
     * 医院名称
     */
    private String yuliu3;

    /**
     * 原因
     */
    private String yuliu4;

    /**
     * 通讯地址
     */
    private String yuliu5;


}