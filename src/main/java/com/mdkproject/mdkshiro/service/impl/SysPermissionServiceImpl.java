package com.mdkproject.mdkshiro.service.impl;

import com.mdkproject.mdkshiro.dao.SysPermissionMapper;
import com.mdkproject.mdkshiro.dao.SysRolePermissionMapper;
import com.mdkproject.mdkshiro.entity.SysPermission;
import com.mdkproject.mdkshiro.entity.SysPermissionVo;
import com.mdkproject.mdkshiro.service.SysPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Liberal-World
 * @Date: 2019-06-20 11:59
 * @Description:
 * @Version 1.0
 */
@Slf4j
@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    /**
     * @auther: lizhen
     * @date: 2019-06-20 12:00
     * 功能描述: 获取所有资源
     */
    @Override
    public List<SysPermissionVo> findAll() {
        List<SysPermission> list  = sysPermissionMapper.findAll();
        List<SysPermissionVo> listvo = new ArrayList<>();
        SysPermissionVo root = new SysPermissionVo();
        root.setId(0);
        root.setParent("#");
        root.setText("资源根节点");
        root.setState("0");
        listvo.add(root);

        for (SysPermission sysPermission : list){
            SysPermissionVo vo = new SysPermissionVo();
            vo.setId(sysPermission.getId());
            vo.setParent(sysPermission.getParentId()+"");
            vo.setText(sysPermission.getName());
            vo.setState("0");
            listvo.add(vo);
        }
        return listvo;
    }


    /**
     * @auther: lizhen
     * @date: 2019-06-20 13:44
     * 功能描述: 资源添加
     */
    @Override
    public boolean save(SysPermission sp) {
        try {
            sysPermissionMapper.insertSelective(sp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("资源添加异常："+e.getMessage());
            return false;
        }
    }

    /**
     * @auther: lizhen
     * @date: 2019-06-20 13:58
     * 功能描述: 更新资源信息
     */
    @Override
    public boolean updatePermission(SysPermission sp) {
        String name = sp.getName();
        String resourceType = sp.getResourceType();
        String url = sp.getUrl();
        String permission = sp.getPermission();
        Integer id = sp.getId();
        boolean state = sp.getAvailable();
        System.out.println(resourceType+"-------------------"+id);
        System.out.println(permission+"-------------------"+url);

        try {
            sysPermissionMapper.updateByPrimaryKey(sp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @auther: lizhen
     * @date: 2019-06-20 14:09
     * 功能描述: 通过id查询资源信息
     */
    @Override
    public SysPermission findByOne(String id) {
        return sysPermissionMapper.selectByPrimaryKey(Integer.valueOf(id));
    }


    /**
     * @auther: lizhen
     * @date: 2019-06-20 14:15
     * 功能描述: 通过id删除资源信息
     */
    @Override
    public boolean delete(String permissionId) {

        try {
            sysPermissionMapper.deleteByPrimaryKey(Integer.valueOf(permissionId));
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            log.info("删除资源异常信息："+e.getMessage());
            return false;
        }

    }


    /**
     * @auther: lizhen
     * @date: 2019-07-01 10:00
     * 功能描述: 部门列表，资源列表
     */
    @Override
    public List<SysPermission> findAllPermissionByPage(int page, int limit) {
        Map<String,Object> data = new HashMap<>();
        data.put("page",(page-1)*limit);
        data.put("limit",limit);
        List<SysPermission> list = sysPermissionMapper.selectPermissionByPage(data);
        return list;
    }



    /**
     * @auther: lizhen
     * @date: 2019-07-01 10:07
     * 功能描述: 总数
     */
    @Override
    public int countData() {
        int i = sysPermissionMapper.countData();
        return i;
    }


    /**
     * @auther: lizhen
     * @date: 2019-07-01 15:56
     * 功能描述: 取消权限
     */
    @Override
    public boolean deletePower(String roleId, String permissionId) {
        try {
            sysRolePermissionMapper.deleteByRoleIdAdnPermissionId(roleId,permissionId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
