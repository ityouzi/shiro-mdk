package com.mdkproject.mdkshiro.entity;

import lombok.Data;

/**
 * @Author：lizhen
 * @Date：2019-07-12
 * @Description：
 */
@Data
public class Area {
    /**
     * 主键ID
     */
    private Integer areaId;

    /**
     * 区域编号
     */
    private String areaNum;

    /**
     * 省
     */
    private String areaProvince;

    /**
     * 市
     */
    private String areaCity;

    /**
     * 区
     */
    private String areaDistrict;

    /**
     * 区的简称
     */
    private String areaNickname;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 创建人
     */
    private String createby;

    /**
     * 简称
     */
    private String yuliu1;

    /**
     * 
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

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAreaNum() {
        return areaNum;
    }

    public void setAreaNum(String areaNum) {
        this.areaNum = areaNum;
    }

    public String getAreaProvince() {
        return areaProvince;
    }

    public void setAreaProvince(String areaProvince) {
        this.areaProvince = areaProvince;
    }

    public String getAreaCity() {
        return areaCity;
    }

    public void setAreaCity(String areaCity) {
        this.areaCity = areaCity;
    }

    public String getAreaDistrict() {
        return areaDistrict;
    }

    public void setAreaDistrict(String areaDistrict) {
        this.areaDistrict = areaDistrict;
    }

    public String getAreaNickname() {
        return areaNickname;
    }

    public void setAreaNickname(String areaNickname) {
        this.areaNickname = areaNickname;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateby() {
        return createby;
    }

    public void setCreateby(String createby) {
        this.createby = createby;
    }

    public String getYuliu1() {
        return yuliu1;
    }

    public void setYuliu1(String yuliu1) {
        this.yuliu1 = yuliu1;
    }

    public String getYuliu2() {
        return yuliu2;
    }

    public void setYuliu2(String yuliu2) {
        this.yuliu2 = yuliu2;
    }

    public String getYuliu3() {
        return yuliu3;
    }

    public void setYuliu3(String yuliu3) {
        this.yuliu3 = yuliu3;
    }

    public String getYuliu4() {
        return yuliu4;
    }

    public void setYuliu4(String yuliu4) {
        this.yuliu4 = yuliu4;
    }

    public String getYuliu5() {
        return yuliu5;
    }

    public void setYuliu5(String yuliu5) {
        this.yuliu5 = yuliu5;
    }
}