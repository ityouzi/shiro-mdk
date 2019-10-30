package com.mdkproject.mdkshiro.entity;

/**
 * @Author：lizhen
 * @Date：2019-07-19
 * @Description：
 */
public class JieLun {
    /**
     * 
     */
    private Integer id;

    /**
     * 检查结论
     */
    private String jielun;

    /**
     * 医生
     */
    private String doctor;

    /**
     * 健康检查机构意见
     */
    private String yijian;

    /**
     * 体检机构公章
     */
    private String gz;

    /**
     * 体检人身份证ID
     */
    private String personIdcard;

    /**
     * 体检审核状态0未审核1通过2不通过
     */
    private String status;

    /**
     * 体检不合格项目
     */
    private String noProject;

    /**
     * 健康证编号
     */
    private String hearthCardid;

    /**
     * 发证日期
     */
    private String name;

    /**
     * 有限期至
     */
    private String sex;

    /**
     * 
     */
    private Integer age;

    /**
     * 体检编号
     */
    private String number;

    /**
     * 单位
     */
    private String company;

    /**
     * 体检医院编号
     */
    private String yl1;

    /**
     * 体检日期
     */
    private String yl2;

    /**
     * 
     */
    private String yl3;

    /**
     * 
     */
    private String yl4;

    /**
     * 
     */
    private String yl5;

    /**
     * 
     */
    private String createTime;

    /**
     * 
     */
    private String updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJielun() {
        return jielun;
    }

    public void setJielun(String jielun) {
        this.jielun = jielun;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getYijian() {
        return yijian;
    }

    public void setYijian(String yijian) {
        this.yijian = yijian;
    }

    public String getGz() {
        return gz;
    }

    public void setGz(String gz) {
        this.gz = gz;
    }

    public String getPersonIdcard() {
        return personIdcard;
    }

    public void setPersonIdcard(String personIdcard) {
        this.personIdcard = personIdcard;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNoProject() {
        return noProject;
    }

    public void setNoProject(String noProject) {
        this.noProject = noProject;
    }

    public String getHearthCardid() {
        return hearthCardid;
    }

    public void setHearthCardid(String hearthCardid) {
        this.hearthCardid = hearthCardid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getYl1() {
        return yl1;
    }

    public void setYl1(String yl1) {
        this.yl1 = yl1;
    }

    public String getYl2() {
        return yl2;
    }

    public void setYl2(String yl2) {
        this.yl2 = yl2;
    }

    public String getYl3() {
        return yl3;
    }

    public void setYl3(String yl3) {
        this.yl3 = yl3;
    }

    public String getYl4() {
        return yl4;
    }

    public void setYl4(String yl4) {
        this.yl4 = yl4;
    }

    public String getYl5() {
        return yl5;
    }

    public void setYl5(String yl5) {
        this.yl5 = yl5;
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
}