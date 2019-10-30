package com.mdkproject.mdkshiro.entity;

import java.util.Date;
import java.util.List;

public class SysPermission {

    private Integer id;

    private Boolean available = Boolean.FALSE;

    private String name;

    private Date numberSort;

    private Long parentId;

    private String parentIds;

    private String permission;

    private String resourceType;

    private String url;

    private String yuliu1;

    private String yuliu2;

    private String yuliu3;

    private String yuliu4;

    private String yuliu5;
    private List<SysRole> roles;

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getNumberSort() {
        return numberSort;
    }

    public void setNumberSort(Date numberSort) {
        this.numberSort = numberSort;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds == null ? null : parentIds.trim();
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission == null ? null : permission.trim();
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType == null ? null : resourceType.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
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