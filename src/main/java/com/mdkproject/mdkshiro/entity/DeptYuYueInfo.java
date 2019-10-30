package com.mdkproject.mdkshiro.entity;

import lombok.Data;

/**
 * @Author：lizhen
 * @Date：2019-07-22
 * @Description：
 */
@Data
public class DeptYuYueInfo {
    /**
     * 主键ID自增
     */
    private Integer deptId;

    /**
     * 单位名称
     */
    private String deptName;

    /**
     * 单位征信代码
     */
    private String deptCode;

    /**
     * 单位上传图片
     */
    private String deptImgurl;

    /**
     * 0默认未审核1审核通过2审核不通过
     */
    private String deptShenhe;

    /**
     * 提交医院编号
     */
    private String deptTjcode;

    /**
     * 单位预约时间
     */
    private String deptTime;

    /**
     * 预约电话
     */
    private String deptPhone;

    /**
     * 单位预约人数
     */
    private Integer deptPeoplenum;

    /**
     * 不通过原因
     */
    private String deptYuanyin;

    /**
     * 
     */
    private String createTime;

    /**
     * 
     */
    private String updateTime;

    /**
     * 体检状态，0没来，1来了
     */
    private String tjstatus;

    /**
     * 单位资质审核1通过2不通过
     */
    private String yuliu1;

    /**
     *  预约时间段
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