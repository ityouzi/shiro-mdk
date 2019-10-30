package com.mdkproject.mdkshiro.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mdkproject.mdkshiro.controller.view.SysRoleVo;
import com.mdkproject.mdkshiro.entity.SysPermission;
import com.mdkproject.mdkshiro.entity.SysPermissionVo;
import com.mdkproject.mdkshiro.entity.SysRole;
import com.mdkproject.mdkshiro.entity.UserInfo;
import com.mdkproject.mdkshiro.error.BusinessException;
import com.mdkproject.mdkshiro.error.EmBusinessError;
import com.mdkproject.mdkshiro.response.CommonReturnType;
import com.mdkproject.mdkshiro.service.SysPermissionService;
import com.mdkproject.mdkshiro.service.SysRoleService;
import com.mdkproject.mdkshiro.utils.listToTree;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Liberal-World
 * @Date: 2019-06-20 14:18
 * @Description: 角色管理
 * @Version 1.0
 */
@Controller
@RequestMapping("roleInfo")
public class SysRoleController extends BaseController{
    @Autowired
    private HttpServletRequest request;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysPermissionService sysPermissionService;



    /**
     * @auther: lizhen
     * @date: 2019-06-20 14:23
     * 功能描述: 角色列表
     */
    @RequestMapping(value = "roleList")
    @ResponseBody
//    @RequiresPermissions("roleInfo:view")
//    @RequiresRoles("admin")
    public CommonReturnType roleList(int page,int limit){
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser==null){
            return CommonReturnType.createCommonReturnType("登陆过期，请重新登陆！","250");
        }
        Map<String,Object> map = new HashMap<>();
        try {
            List<SysRole> list = sysRoleService.findRoleListByPage(page,limit);//分页
            int i = sysRoleService.countData();
            map.put("count",i);
            map.put("pageData",list);
            return CommonReturnType.createCommonReturnType(map,"200");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("获取角色列表异常信息："+e.getMessage());
            return CommonReturnType.createCommonReturnType("获取角色列表失败","100");
        }
    }


    /**
     * @auther: lizhen
     * @date: 2019-06-20 14:43
     * 功能描述: 角色添加
     */
    @RequestMapping(value = "roleAdd")
    @ResponseBody
    @RequiresRoles("admin")                     //角色
    @RequiresPermissions("roleInfo:add")        //权限
    public CommonReturnType sysRoleAdd(SysRole sr){
        logger.info("添加："+sr.getRole());
        if (sysRoleService.save(sr)){
            return CommonReturnType.createCommonReturnType("添加角色成功","200");
        }
        return CommonReturnType.createCommonReturnType("添加角色失败","100");
    }


    /**
     * @auther: lizhen
     * @date: 2019-06-20 14:48
     * 功能描述: 更新角色
     */
    @RequestMapping(value = "roleUpdate")
    @ResponseBody
    @RequiresRoles("admin")
    @RequiresPermissions("roleInfo:update")
    public CommonReturnType updateRole(SysRole sr){
        logger.info("更新："+ sr.getRole() );
        logger.info("更新："+ sr.getId());
        logger.info("更新："+ sr.getDescription());
        if (sysRoleService.updateRole(sr)){
            return CommonReturnType.createCommonReturnType("更新角色成功","200");
        }
        return CommonReturnType.createCommonReturnType("更新角色失败","100");
    }


    /**
     * @auther: lizhen
     * @date: 2019-07-01 15:23
     * 功能描述: 删除角色
     */
    @RequestMapping(value = "roleDelete")
    @ResponseBody
    @RequiresPermissions("roleInfo:del")
    @RequiresRoles("admin")
    public CommonReturnType roleInfoDelete(String roleId) throws BusinessException {
        if (roleId.equals("1")){
            throw new BusinessException(EmBusinessError.CANT_DELETE_ADMIN);
        }
        if (sysRoleService.roleInfoDelete(Integer.valueOf(roleId))){
            return CommonReturnType.createCommonReturnType("删除角色成功","200");
        }
        return CommonReturnType.createCommonReturnType("删除角色失败","100");
    }


    
    
    
    /**
     * @auther: lizhen
     * @date: 2019-06-20 14:58
     * 功能描述: 角色禁用或启用
     */
    @RequestMapping(value = "updateState")
    @ResponseBody
    @RequiresPermissions("roleInfo:update")
    @RequiresRoles("admin")
    public CommonReturnType updateState(Boolean available,String id){
        if (sysRoleService.updateState(available,id)){
            return CommonReturnType.createCommonReturnType("操作成功","200");
        }
        return CommonReturnType.createCommonReturnType("操作失败","100");
    }


    /**
     * @auther: lizhen
     * @date: 2019-06-20 15:06
     * 功能描述: 通过uid查询角色信息
     */
    @RequestMapping(value = "findByOne")
    @ResponseBody
    public CommonReturnType findByOne(String id){
        logger.info("查询角色角色信息："+id);
        SysRole sysRole = sysRoleService.findByOne(id);
        if (sysRole != null){
            JsonConfig config = new JsonConfig();
            config.setExcludes(new String[]{"permissions","userInfo"});
            JSONObject json = JSONObject.fromObject(sysRole, config);
            return CommonReturnType.createCommonReturnType(json,"200");
        }
        return CommonReturnType.createCommonReturnType("fail","100");
    }


    /**
     * @auther: lizhen
     * @date: 2019-06-20 15:14
     * 功能描述: 分配角色，获取角色列表树
     */
    @RequestMapping(value = "findAll")
    @ResponseBody
//    @RequiresRoles("admin")
    public CommonReturnType findAll(){
        List<SysRoleVo> listVo = sysRoleService.findAll();
        //角色树
        String s = JSON.toJSONString(listVo);
        com.alibaba.fastjson.JSONArray jsonArray1 = listToTree.listToTree(JSONArray.parseArray(s), "id", "pid", "children");
        if (listVo.size()>0){
            return CommonReturnType.createCommonReturnType(jsonArray1,"200");
        }
        return CommonReturnType.createCommonReturnType("获取角色树失败","100");
    }



    /**
     * @auther: lizhen
     * @date: 2019-06-20 15:21
     * 功能描述: 查询该角色已拥有的权限资源
     * 1
     */
    @RequestMapping(value = "findPermissionByRoleId")
    @ResponseBody
    public CommonReturnType findPermissionByRoleId(String id){
        logger.info("查询角色拥有资源："+id);
        if (StringUtils.isEmpty(id)){
            logger.info("角色ID为空");
        }
        List<SysPermissionVo> result = sysRoleService.findByOne2(id);//角色权限树
        String s = JSON.toJSONString(result);
        JSONArray array = listToTree.listToTree(JSONArray.parseArray(s),"id","parent","children");
        return CommonReturnType.createCommonReturnType(array);

    }
    
    /**
     * @auther: lizhen
     * @date: 2019-06-20 15:28
     * 功能描述: 保存角色权限资源
     * 参数：角色ID，资源ID
     * 2
     */
    @RequestMapping(value = "savePermissionId")
    @ResponseBody
    @RequiresPermissions("roleInfo:update")
    @RequiresRoles("admin")
    public CommonReturnType savePermissionId(String id,String permissionId){
        logger.info("保存角色资源："+permissionId);
        try {
            String[] split = permissionId.split(",");
            List<SysPermission> list = new ArrayList<>();
            for (int i=0;i<split.length;i++){
                SysPermission sr = new SysPermission();
                sr.setId(Integer.valueOf(split[i]));
                list.add(sr);
            }
            SysRole sr = new SysRole();
            sr.setId(Integer.valueOf(id));
            sr.setPermissions(list);
            sysRoleService.saveRoleAndPermission(sr);
            return CommonReturnType.createCommonReturnType("分配权限成功","200");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("分配权限异常信息："+e.getMessage());
            return CommonReturnType.createCommonReturnType("分配权限失败","100");
        }
    }








    

      

}
