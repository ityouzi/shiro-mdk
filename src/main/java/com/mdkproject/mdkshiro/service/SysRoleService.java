package com.mdkproject.mdkshiro.service;

import com.mdkproject.mdkshiro.controller.view.SysRoleVo;
import com.mdkproject.mdkshiro.entity.SysPermission;
import com.mdkproject.mdkshiro.entity.SysPermissionVo;
import com.mdkproject.mdkshiro.entity.SysRole;
import com.mdkproject.mdkshiro.entity.SysUserRole;

import java.util.List;

public interface SysRoleService {



    //添加角色
    boolean save(SysRole sr);

    //更新角色
    boolean updateRole(SysRole sr);

    //角色禁用或启用
    boolean updateState(Boolean available, String id);

    //查询角色已有资源
    List<SysPermissionVo> findByOne2(String id);

    //
    SysRole findByOne(String id);

    //角色列表树
    List<SysRoleVo> findAll();
    //角色列表--uid
    List<SysPermission> findByUid(Integer uid);

    //角色-权限
    List<SysUserRole> findRoleByUid(Integer uid);


    //角色列表分页
    List<SysRole> findRoleListByPage(int page, int limit);
    int countData();

    //角色分配权限资源
    boolean saveRoleAndPermission(SysRole sr);

    //删除角色信息
    boolean roleInfoDelete(Integer roleId);

    //依据角色获取角色对应的权限集合
    List<SysPermission> findPermissionListByRoleId(Integer roleId);

    //角色树
    List<SysRoleVo> roleTreeList();

    List<SysRoleVo> roleTreeListByRoleId(String roleId);
}
