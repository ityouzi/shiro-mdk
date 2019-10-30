package com.mdkproject.mdkshiro.entity;

import lombok.Data;

/**
 * @Author：lizhen
 * @Date：2019-07-19
 * @Description：
 */
@Data
public class KeyCode {
    /**
     * 主键ID
     */
    private Integer keyId;

    /**
     * 激活码
     */
    private String keyCode;

    /**
     * 激活码状态
     */
    private Integer keyStatus;

    /**
     * 医院编号
     */
    private String hospitalNum;

    /**
     * 
     */
    private String createTime;

    /**
     * 
     */
    private String updateTime;


}