package com.mdkproject.mdkshiro.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mdkproject.mdkshiro.entity.SysPermission;
import com.mdkproject.mdkshiro.entity.SysPermissionVo;
import com.mdkproject.mdkshiro.entity.UserInfo;
import com.mdkproject.mdkshiro.error.BusinessException;
import com.mdkproject.mdkshiro.error.EmBusinessError;
import com.mdkproject.mdkshiro.response.CommonReturnType;
import com.mdkproject.mdkshiro.service.SysPermissionService;
import com.mdkproject.mdkshiro.utils.listToTree;
import net.sf.json.JsonConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Liberal-World
 * @Date: 2019-06-20 11:11
 * @Description: 权限管理（部门管理，资源管理）
 * @Version 1.0
 */
@Controller
@RequestMapping("sysPermission")
public class SysPermissionController extends BaseController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    SysPermissionService sysPermissionService;
    


    /**
     * @auther: lizhen
     * @date: 2019-06-20 11:39
     * 功能描述: 部门管理资源树
     */
    @RequestMapping(value = "findAll")
    @ResponseBody
    public CommonReturnType findAll(){
        List<SysPermissionVo> listVo = sysPermissionService.findAll();
        //资源树
        String s = JSON.toJSONString(listVo);
        JSONArray jsonArray1 = listToTree.listToTree(JSONArray.parseArray(s), "id", "parent", "children");
        if (listVo.size()>0){
            return CommonReturnType.createCommonReturnType(jsonArray1,"200");
        }
        return CommonReturnType.createCommonReturnType("fail","100");
    }

    /**
     * @auther: lizhen
     * @date: 2019-07-01 15:13
     * 功能描述: 资源分页列表
     */
    @RequestMapping(value = "permissionList")
    @ResponseBody
    public CommonReturnType permissionList(int page,int limit){
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser==null){
            return CommonReturnType.createCommonReturnType("登陆过期，请重新登陆！","250");
        }
        Map<String,Object> map = new HashMap<>();
        List<SysPermission> allPermissionByPage = sysPermissionService.findAllPermissionByPage(page, limit);
        int i = sysPermissionService.countData();
        map.put("count",i);
        map.put("pageData",allPermissionByPage);
        return CommonReturnType.createCommonReturnType(map);
    }

    /**
     * @auther: lizhen
     * @date: 2019-06-20 13:41
     * 功能描述: 资源添加(部门添加)
     */
    @RequestMapping(value = "sysPermissionAdd",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    @RequiresPermissions("sysPermission:add")
    @RequiresRoles("admin")
    public CommonReturnType sysPermissionAdd(SysPermission sp){
        logger.info("添加资源："+sp.getName());
        logger.info("添加资源parentId："+sp.getParentId());
        if (sysPermissionService.save(sp)){
            return CommonReturnType.createCommonReturnType("添加资源成功","200");
        }
        return CommonReturnType.createCommonReturnType("添加资源失败","100");
    }


    /**
     * @auther: lizhen
     * @date: 2019-06-20 13:55
     * 功能描述: 资源更新
     */
    @RequestMapping(value = "permissionUpdate",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    @RequiresPermissions("sysPermission:update")
    @RequiresRoles("admin")
    public CommonReturnType updatePermission(SysPermission sp){
        logger.info("更新资源："+  sp.getName());
        if (sysPermissionService.updatePermission(sp)){
            return CommonReturnType.createCommonReturnType("资源更新操作成功","200");
        }
        return CommonReturnType.createCommonReturnType("操作失败","100");
    }
    
    
    /**
     * @auther: lizhen
     * @date: 2019-06-20 14:06
     * 功能描述: 通过permissionId查询资源信息
     */
    @RequestMapping(value = "permissionGet")
    @ResponseBody
    public CommonReturnType findByOne(String permissionId){
        logger.info("查询资源信息："+permissionId);
        SysPermission sysPermission = sysPermissionService.findByOne(permissionId);
        if (sysPermission != null){
            JsonConfig config = new JsonConfig();
            config.setExcludes(new String[]{"roles"});
            net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(sysPermission, config);
            return CommonReturnType.createCommonReturnType(json,"200");
        }
        return CommonReturnType.createCommonReturnType("查询资源信息失败","100");
    }
    
    
    /**
     * @auther: lizhen
     * @date: 2019-06-20 14:13
     * 功能描述: permissionId删除资源信息
     */
    @RequestMapping(value = "permissionDelete")
    @ResponseBody
    @RequiresRoles("admin")
    @RequiresPermissions("sysPermission:del")
    public CommonReturnType delete(String permissionId){
        logger.info("删除资源信息："+permissionId);
        if (sysPermissionService.delete(permissionId)){
            return CommonReturnType.createCommonReturnType("success","200");
        }
        return CommonReturnType.createCommonReturnType("fail","100");
    }


    /**
     * @auther: lizhen
     * @date: 2019-07-01 15:50
     * 功能描述: 删除角色的权限
     */
    @RequestMapping(value = "deletePower")
    @ResponseBody
    @RequiresRoles("admin")
    @RequiresPermissions("sysPermission:del")
    public CommonReturnType deletePower(String roleId,String permissionId) throws BusinessException {
        if (StringUtils.isEmpty(roleId) || StringUtils.isEmpty(permissionId)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        if (sysPermissionService.deletePower(roleId,permissionId)){
            return CommonReturnType.createCommonReturnType("删除权限成功","200");
        }
        return CommonReturnType.createCommonReturnType("删除权限失败","100");
    }








}
