package com.mdkproject.mdkshiro.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: mdk-shiro->ExcelModel
 * @description: Excel类
 * @author: lizhen
 * @create: 2019-07-31 12:19
 **/
@Data
public class ExcelModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "手机号")
    private String telphone;

    @Excel(name = "姓名")
    private String name;

    @Excel(name = "性别")
    private String sex;

    @Excel(name = "身份证号")
    private String idcardNum;

    @Excel(name = "健康证号")
    private String hearthcardNum;

    @Excel(name = "照片")
    private String idcardPhoto;

    @Excel(name = "办证日期",format = "yyyy-MM-dd")
    private String startdate;

    @Excel(name = "有效期",format = "yyyy-MM-dd")
    private String enddate;

    @Excel(name = "状态")
    private String status;

    @Excel(name = "医院编号")
    private String deptNum;

    @Excel(name = "创建时间",format = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    @Excel(name = "项目")
    private String yuliu1;

    @Excel(name = "登记日期",format = "yyyy-MM-dd")
    private String yuliu2;

    @Excel(name = "体检机构")
    private String yuliu3;





}
