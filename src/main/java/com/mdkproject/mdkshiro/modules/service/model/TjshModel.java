package com.mdkproject.mdkshiro.modules.service.model;

import lombok.Data;

/**
 * @program: mdk-shiro->TjshModel
 * @description: 体检审核自定义参数model
 * @author: lizhen
 * @create: 2019-07-15 18:06
 **/
@Data
public class TjshModel {
    private String tjId;            //批量ID数组
    private String status;          //状态（0未审核，1审核通过，2审核不通过）
    private String startdate;       //开始时间
    private String enddate;         //结束时间
    private String why;             //原因
}
