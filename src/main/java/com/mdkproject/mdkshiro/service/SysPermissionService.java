package com.mdkproject.mdkshiro.service;

import com.mdkproject.mdkshiro.entity.SysPermission;
import com.mdkproject.mdkshiro.entity.SysPermissionVo;

import java.util.List;

public interface SysPermissionService {
    List<SysPermissionVo> findAll();

    boolean save(SysPermission sp);

    //更新资源
    boolean updatePermission(SysPermission sp);

    //通过uid查询资源信息
    SysPermission findByOne(String id);

    //通过uid删除资源信息
    boolean delete(String permissionId);


    //部门列表
    List<SysPermission> findAllPermissionByPage(int page, int limit);
    //总数
    int countData();

    //取消权限
    boolean deletePower(String roleId, String permissionId);
}
