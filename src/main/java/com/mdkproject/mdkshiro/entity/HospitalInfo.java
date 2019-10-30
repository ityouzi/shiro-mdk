package com.mdkproject.mdkshiro.entity;

import lombok.Data;

/**
 * @Author：lizhen
 * @Date：2019-07-12
 * @Description：
 */
@Data
public class HospitalInfo {
    /**
     * 主键ID
     */
    private Integer hospitalId;

    /**
     * 医院名称
     */
    private String hospitalName;

    /**
     * 医院编号
     */
    private String hospitalNum;

    /**
     * 医院公章
     */
    private String hospitalGz;

    /**
     * 省
     */
    private String hospitalProvince;

    /**
     * 市
     */
    private String hospitalCity;

    /**
     * 区
     */
    private String hospitalDistrict;

    /**
     * 街道
     */
    private String hospitalAddress;

    /**
     * 区域编号
     */
    private String areaNum;

    /**
     * 
     */
    private String createTime;

    /**
     * 
     */
    private String updateTime;

    /**
     * 医院电话
     */
    private String hospitalTel;

    /**
     * 备注
     */
    private String bz;

    /**
     * 医院模板管理
     */
    private String yuliu1;

    /**
     * 医院一天处理预约人数
     */
    private String yuliu2;

    /**
     * 医院体检时间设置
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


    public Integer getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalNum() {
        return hospitalNum;
    }

    public void setHospitalNum(String hospitalNum) {
        this.hospitalNum = hospitalNum;
    }

    public String getHospitalGz() {
        return hospitalGz;
    }

    public void setHospitalGz(String hospitalGz) {
        this.hospitalGz = hospitalGz;
    }

    public String getHospitalProvince() {
        return hospitalProvince;
    }

    public void setHospitalProvince(String hospitalProvince) {
        this.hospitalProvince = hospitalProvince;
    }

    public String getHospitalCity() {
        return hospitalCity;
    }

    public void setHospitalCity(String hospitalCity) {
        this.hospitalCity = hospitalCity;
    }

    public String getHospitalDistrict() {
        return hospitalDistrict;
    }

    public void setHospitalDistrict(String hospitalDistrict) {
        this.hospitalDistrict = hospitalDistrict;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public String getAreaNum() {
        return areaNum;
    }

    public void setAreaNum(String areaNum) {
        this.areaNum = areaNum;
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

    public String getHospitalTel() {
        return hospitalTel;
    }

    public void setHospitalTel(String hospitalTel) {
        this.hospitalTel = hospitalTel;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
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