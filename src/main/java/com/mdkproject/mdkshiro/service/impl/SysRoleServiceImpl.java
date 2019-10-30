package com.mdkproject.mdkshiro.service.impl;

import com.mdkproject.mdkshiro.controller.view.SysRoleVo;
import com.mdkproject.mdkshiro.dao.SysPermissionMapper;
import com.mdkproject.mdkshiro.dao.SysRoleMapper;
import com.mdkproject.mdkshiro.dao.SysRolePermissionMapper;
import com.mdkproject.mdkshiro.dao.SysUserRoleMapper;
import com.mdkproject.mdkshiro.entity.*;
import com.mdkproject.mdkshiro.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Auther: Liberal-World
 * @Date: 2019-06-20 14:19
 * @Description: 角色管理
 * @Version 1.0
 */
@Slf4j
@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;



    /**
     * @auther: lizhen
     * @date: 2019-06-20 14:46
     * 功能描述: 添加角色
     */
    @Override
    public boolean save(SysRole sr) {
        try {
            sysRoleMapper.insertSelective(sr);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("添加角色异常信息："+e.getMessage());
            return false;
        }
    }

    /**
     * @auther: lizhen
     * @date: 2019-06-20 14:52
     * 功能描述: 更新角色
     */
    @Override
    public boolean updateRole(SysRole sr) {
        try {
           sysRoleMapper.updateByPrimaryKey(sr);
           return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateState(Boolean available, String id) {
        return false;
    }

    /**
     * @auther: lizhen
     * @date: 2019-06-26 18:21
     * 功能描述: 对角色分配权限,获取角色已有权限
     * 参数：角色ID
     */
    @Override
    public List<SysPermissionVo> findByOne2(String roleId) {
        List<SysPermissionVo> listVO = new ArrayList<>();
        List<SysRolePermission> sysRolePermissions = sysRolePermissionMapper.selectByRoleId(Integer.valueOf(roleId));//已有权限集合
        List<SysPermission> all = sysPermissionMapper.findAll();//所有权限集合

        SysPermissionVo root = new SysPermissionVo();
        root.setId(0);
        root.setParent("#");
        root.setText("资源根节点");
        root.setState("0");
        listVO.add(root);

        for (int i=0;i<all.size();i++){
            SysPermission sysPermission = all.get(i);
            Integer permissionId = sysPermission.getId();
            for (SysRolePermission sp:sysRolePermissions){
                if (permissionId.equals(sp.getPermissionId())){
                    sysPermission.setYuliu2("1");//
                }
            }
            SysPermissionVo vo = new SysPermissionVo();
            vo.setId(sysPermission.getId());
            vo.setParent(sysPermission.getParentId()+"");
            vo.setText(sysPermission.getName());
            if (sysPermission.getYuliu2()==null){
                vo.setState("0");
            }else{
                vo.setState(sysPermission.getYuliu2());//有
            }
            listVO.add(vo);
        }
        return listVO;
    }


    /**
     * @auther: lizhen
     * @date: 2019-07-01 11:54
     * 功能描述:
     */
    @Override
    public SysRole findByOne(String id) {
        return sysRoleMapper.selectByPrimaryKey(Integer.valueOf(id));
    }

    /**
     * @auther: lizhen
     * @date: 2019-06-26 13:19
     * 功能描述: 获取角色列表

     */
    @Override
    public List<SysRoleVo> findAll() {
        List<SysRoleVo> lists = new ArrayList<>();
        List<SysRole> list = sysRoleMapper.selectAllRoles();
        SysRoleVo root = new SysRoleVo();
        root.setId(0);
        root.setPid("#");
        root.setName("资源根节点");
        root.setStatus("0");
        lists.add(root);

        for (SysRole sr : list){
            SysRoleVo vo = new SysRoleVo();
            vo.setId(sr.getId());
            vo.setPid(sr.getYuliu1()+"");
            vo.setName(sr.getDescription());
            vo.setStatus("0");
            lists.add(vo);
        }
        return lists;
    }


    /**
     * @auther: lizhen
     * @date: 2019-06-26 15:55
     * 功能描述: 依据Uid获取角色列表,获取资源列表
     */
    @Override
    public List<SysPermission> findByUid(Integer uid) {
        List<SysPermission> allList = new ArrayList<>();
        List<SysUserRole> list1 = sysUserRoleMapper.selectByUid(uid);//角色集合
        for (SysUserRole sysRole : list1){
            List<SysRolePermission> list2 = sysRolePermissionMapper.selectByRoleId(sysRole.getRoleId());
            for (SysRolePermission permission : list2){
                SysPermission sysPermission = sysPermissionMapper.selectByPrimaryKey(permission.getPermissionId());
                allList.add(sysPermission);
            }

        }
        return allList;
    }


    /**
     * @auther: lizhen
     * @date: 2019-06-26 18:14
     * 功能描述: 角色--权限
     */
    @Override
    public List<SysUserRole> findRoleByUid(Integer uid) {
        List<SysUserRole> list = sysUserRoleMapper.selectByUid(uid);
        return list;
    }


    /**
     * @auther: lizhen
     * @date: 2019-07-01 9:39
     * 功能描述: 角色列表分页
     */
    @Override
    public List<SysRole> findRoleListByPage(int page, int limit) {
        List<SysRole> lists = new ArrayList<>();
        Map<String,Object> data = new HashMap<>();
        data.put("page",(page-1)*limit);
        data.put("limit",limit);
        List<SysRole> list = sysRoleMapper.selectRoleListByPage(data);
        for (int i=0;i<list.size();i++){
            SysRole sysRole = list.get(i);
            String roleId;
            roleId = sysRole.getYuliu1();//上级角色ID
            if ("0".equals(roleId)){
                roleId="1";
            }
            SysRole sysRole1 = sysRoleMapper.selectByPrimaryKey(Integer.valueOf(roleId));
            if (sysRole1==null){
                sysRole.setYuliu2("--");
            }else {
                sysRole.setYuliu2(sysRole1.getDescription());
            }
            lists.add(sysRole);
        }
        return lists;
    }

    /**
     * @auther: lizhen
     * @date: 2019-07-01 9:39
     * 功能描述: 角色总数
     */
    @Override
    public int countData() {
        int i = sysRoleMapper.countData();
        return i;
    }


    /**
     * @auther: lizhen
     * @date: 2019-07-01 12:10
     * 功能描述: 给角色分配权限资源
     */
    @Override
    @Transactional
    public boolean saveRoleAndPermission(SysRole sr) {
        Integer roleId = sr.getId();
        List<SysPermission> permissions = sr.getPermissions();//要保存的权限集合
        try {
            List<SysRolePermission> sysRolePermissions = sysRolePermissionMapper.selectByRoleId(roleId);//已有
            if (sysRolePermissions.size()>0){
                sysRolePermissionMapper.deleltByRoleId(roleId);//清除之前的
                for (SysPermission sp:permissions){
                    SysRolePermission srpp=new SysRolePermission();
                    srpp.setRoleId(roleId);
                    srpp.setPermissionId(sp.getId());
                    sysRolePermissionMapper.insertSelective(srpp);
                }
            }else {
                for (SysPermission sp:permissions){
                    SysRolePermission srp=new SysRolePermission();
                    srp.setRoleId(roleId);
                    srp.setPermissionId(sp.getId());
                    sysRolePermissionMapper.insertSelective(srp);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("分配权限异常信息："+e.getMessage());
            return false;
        }
    }


    /**
     * @auther: lizhen
     * @date: 2019-07-01 15:26
     * 功能描述: 删除角色信息
     */
    @Override
    public boolean roleInfoDelete(Integer roleId) {
        try {
            sysRoleMapper.deleteByPrimaryKey(roleId);
            sysRolePermissionMapper.deleltByRoleId(roleId); //角色权限表
            sysUserRoleMapper.deleteByRoleId(roleId);       //用户角色表
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("删除角色信息异常："+e.getMessage());
            return false;
        }
    }


    /**
     * @auther: lizhen
     * @date: 2019-07-02 13:55
     * 功能描述: 依据角色获取角色对应的权限集合
     */
    @Override
    public List<SysPermission> findPermissionListByRoleId(Integer roleId) {
        List<SysPermission> list = new ArrayList<>();
        List<SysRolePermission> sysRolePermissions = sysRolePermissionMapper.selectByRoleId(roleId);//角色权限关系表
        System.out.println(sysRolePermissions.size());
        for (SysRolePermission srp:sysRolePermissions){
            SysPermission sysPermission = sysPermissionMapper.selectByPrimaryKey(srp.getPermissionId());//获取权限对象
            list.add(sysPermission);
        }
        return list;
    }

    @Override
    public List<SysRoleVo> roleTreeList() {
        List<SysRole> list = sysRoleMapper.selectAllRoles();
        List<SysRoleVo> listvo = new ArrayList<>();
        for (SysRole sr : list){
            SysRoleVo vo = new SysRoleVo();
            vo.setId(sr.getId());               //ID
            vo.setPid(sr.getYuliu1()+"");    //父级ID
            vo.setName(sr.getDescription());    //名称
            vo.setStatus("0");
            listvo.add(vo);
        }
        return listvo;
    }

    @Override
    public List<SysRoleVo> roleTreeListByRoleId(String roleId) {
        List<SysRoleVo> lists = new ArrayList<>();
        List<SysRole> list = sysRoleMapper.selectAllRoles();//所有角色集合

        List<SysRole> newlist = new ArrayList();//已有角色集合
        String[] split = roleId.split(",");
        for (int i=0;i<split.length;i++){
            String roleid = split[i];
            SysRole sysRole = sysRoleMapper.selectByPrimaryKey(Integer.valueOf(roleid));
            newlist.add(sysRole);
        }

        for (int i=0;i<list.size();i++){
            SysRole sr = list.get(i);
            Integer role = sr.getId();
            for (SysRole sr1:newlist){
                if (role.equals(sr1.getId())){
                    sr.setYuliu2("1");//有这个角色
                }
            }
            SysRoleVo vo = new SysRoleVo();
            vo.setId(sr.getId());               //ID
            vo.setPid(sr.getYuliu1()+"");       //父级ID
            vo.setName(sr.getDescription());    //名称
            if (sr.getYuliu2()==null){
                vo.setStatus("0");
            }else{
                vo.setStatus(sr.getYuliu2());
            }
            lists.add(vo);
        }
        return lists;
    }

    //



}
