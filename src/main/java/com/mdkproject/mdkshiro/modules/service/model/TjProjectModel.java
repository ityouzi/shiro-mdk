package com.mdkproject.mdkshiro.modules.service.model;

import lombok.Data;

/**
 * @program: mdk-shiro->TjProjectModel
 * @description: 体检项目模型
 * @author: lizhen
 * @create: 2019-07-19 11:16
 **/
@Data
public class TjProjectModel {
    //结论
    private String number;          //编号
    private String healthNum;       //健康证编号
    private String name;            //
    private String sex;             //
    private String age;             //
    private String idcardNum;       //身份证号
    private String company;         //单位
    private String status;          //状态
    private String tjTime;          //体检时间
    private String hospitalNum;     //医院编号
    private String qt;              //其他



    //病史
    private String ganyan;          //肝炎
    private String liji;            //痢疾
    private String shanghan;        //伤寒
    private String feijiehe;        //肺结核
    private String pifubing;        //皮肤病

    //体征
    private String xin;             //心
    private String gan;             //肝
    private String pi;              //脾
    private String fei;             //肺
    private String sx;
    private String zjx;
    private String sbsz;
    private String yx;
    private String sc;
    private String hn;

    //X线胸透
    private String xPicture;

    //实验室检验
    private String lj;
    private String sh;
    private String alt;
    private String antiHav;
    private String antiHev;


    private String createTime;
    private String updateTime;



}
