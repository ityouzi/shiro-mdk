package com.mdkproject.mdkshiro.entity;

import java.util.List;

public class SysRole {

    private Integer id;

    private Boolean available = Boolean.FALSE;

    private String description;

    private String role;

    private String yuliu1;//上级角色

    private String yuliu2;

    private String yuliu3;

    private String yuliu4;

    private String yuliu5;

    //角色 -- 权限关系：多对多关系;
    private List<SysPermission> permissions;    //一个角色对应多个权限
    // 用户 -- 角色关系定义: 多对多关系
    private List<UserInfo> userInfos;           // 一个角色对应多个用户

    public List<SysPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<SysPermission> permissions) {
        this.permissions = permissions;
    }

    public List<UserInfo> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(List<UserInfo> userInfos) {
        this.userInfos = userInfos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role == null ? null : role.trim();
    }

    public String getYuliu1() {
        return yuliu1;
    }

    public void setYuliu1(String yuliu1) {
        this.yuliu1 = yuliu1 == null ? null : yuliu1.trim();
    }

    public String getYuliu2() {
        return yuliu2;
    }

    public void setYuliu2(String yuliu2) {
        this.yuliu2 = yuliu2 == null ? null : yuliu2.trim();
    }

    public String getYuliu3() {
        return yuliu3;
    }

    public void setYuliu3(String yuliu3) {
        this.yuliu3 = yuliu3 == null ? null : yuliu3.trim();
    }

    public String getYuliu4() {
        return yuliu4;
    }

    public void setYuliu4(String yuliu4) {
        this.yuliu4 = yuliu4 == null ? null : yuliu4.trim();
    }

    public String getYuliu5() {
        return yuliu5;
    }

    public void setYuliu5(String yuliu5) {
        this.yuliu5 = yuliu5 == null ? null : yuliu5.trim();
    }
}