package com.mdkproject.mdkshiro.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mdkproject.mdkshiro.controller.view.SysRoleVo;
import com.mdkproject.mdkshiro.entity.SysRole;
import com.mdkproject.mdkshiro.entity.UserInfo;
import com.mdkproject.mdkshiro.error.BusinessException;
import com.mdkproject.mdkshiro.error.EmBusinessError;
import com.mdkproject.mdkshiro.response.CommonReturnType;
import com.mdkproject.mdkshiro.service.SysRoleService;
import com.mdkproject.mdkshiro.service.UserInfoService;
import com.mdkproject.mdkshiro.utils.listToTree;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Liberal-World
 * @Date: 2019-06-18 09:39
 * @Description:  用户管理模块
 * @Version 1.0
 */
@RestController
@RequestMapping("userInfo")
@CrossOrigin(origins = "*",allowCredentials = "true")
public class UserInfoController extends BaseController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private SysRoleService sysRoleService;



    /**
     * @auther: lizhen
     * @date: 2019-06-18 9:53
     * 功能描述: 用户注册
     */
    @RequestMapping(value = "register",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public CommonReturnType register(UserInfo ui){
        logger.info("客户注册："+ui.getName());
        UserInfo userInfo = userInfoService.findByAccount(ui.getAccount());
        if (userInfo!=null){
            return CommonReturnType.createCommonReturnType("注册失败，用户已存在","300");
        }
        if (userInfoService.save(ui)){
            return CommonReturnType.createCommonReturnType("注册成功","200");
        }
        return CommonReturnType.createCommonReturnType("注册失败","100");
    }




    /**
     * @auther: lizhen
     * @date: 2019-06-18 10:09
     * 功能描述: 获取用户列表
     */
    @RequestMapping(value = "userList")
    @ResponseBody
//    @RequiresPermissions("userInfo:view")   //拥有此权限的才可以访问
//    @RequiresRoles("admin")                 //拥有此角色才可以访问
    public CommonReturnType userList(HttpServletRequest request,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int limit){
        UserInfo currentUser = getCurrentUser(request);
        Map<String,Object> map = new HashMap<>();
        if (currentUser==null){
            return CommonReturnType.createCommonReturnType("登陆过期，请重新登陆！","250");
        }
        logger.info("权限管理列表："+currentUser.getAccount());
        try {
            List<UserInfo> list = userInfoService.findAllUser(page,limit);
            int i = userInfoService.CountUserData();
            map.put("count",i);
            map.put("pageData",list);
            return CommonReturnType.createCommonReturnType(map,"200");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("异常信息："+e.getMessage());
            return CommonReturnType.createCommonReturnType("获取用户列表失败","100");
        }
    }

    /**
     * @auther: lizhen
     * @date: 2019-06-18 9:53
     * 功能描述: 管理员添加用户
     */
    @RequestMapping(value = "userAdd",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @RequiresPermissions("userInfo:add")//拥有此权限的才可以访问
    @RequiresRoles("admin")             //拥有此角色才可以访问
    public CommonReturnType userAdd(@RequestBody UserInfo ui){
        logger.info("添加客户："+ui.getName());
        if (userInfoService.save(ui)){
            return CommonReturnType.createCommonReturnType( "添加用户成功","200");
        }
        return CommonReturnType.createCommonReturnType("添加用户失败","100");
    }
    
    /**
     * @auther: lizhen
     * @date: 2019-06-26 12:59
     * 功能描述: 修改用户信息
     */

    @RequestMapping(value = "userInfoUpdate",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @RequiresPermissions("userInfo:update")
    @RequiresRoles("admin")
    public CommonReturnType userUpdate(UserInfo ui) throws BusinessException {
        logger.info("修改客户："+ui.getName());
        if ("1".equals(ui.getUid())){
            throw new BusinessException(EmBusinessError.CANT_INFO_ADMIN);
        }
        if (userInfoService.update(ui)){
            return CommonReturnType.createCommonReturnType("修改客户成功","200");
        }
        return CommonReturnType.createCommonReturnType("修改失败","100");
    }

    /**
     * @auther: lizhen
     * @date: 2019-06-18 11:30
     * 功能描述: 用户禁用或启用
     */
    @RequestMapping(value = "updateStatus",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @RequiresPermissions("roleInfo:update")
    public CommonReturnType updateStatus(@RequestBody UserInfo ui) throws BusinessException {
        String uid = String.valueOf(ui.getUid());
        if ("1".equals(uid)){
            throw new BusinessException(EmBusinessError.CANT_FREEZE_ADMIN);
        }
        if (userInfoService.updateStatus(ui)){
            return CommonReturnType.createCommonReturnType("操作成功","200");
        }
        return CommonReturnType.createCommonReturnType("操作失败","100");
    }

    /**
     * @auther: lizhen
     * @date: 2019-06-18 11:53
     * 功能描述: 修改密码
     */
    @RequestMapping(value = "updatePassword")
    @ResponseBody
//    @RequiresRoles("admin")
//    @RequiresPermissions("userInfo:update")
    public CommonReturnType updatePassword(String password, String uid, HttpServletRequest request) throws BusinessException {
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser==null){
            return CommonReturnType.createCommonReturnType("登陆过期","250");
        }
        if ("1".equals(uid)){                           //不能修改超级管理员
            throw new BusinessException(EmBusinessError.CANT_PASSWORD_ADMIN);
        }
        if (userInfoService.updatePassword(password,uid)){
            return CommonReturnType.createCommonReturnType("成功","200");
        }
        return CommonReturnType.createCommonReturnType("失败","100");
    }

    /**
     * @auther: lizhen
     * @date: 2019-06-27 16:00
     * 功能描述: 依据用户ID获取用户已拥有角色
     */
    @RequestMapping(value = "findRoleByUid")
    @ResponseBody
    public CommonReturnType findRoleByUid(Integer uid){
        UserInfo us = userInfoService.findByOne(uid);
        String roleId = us.getYuliu1();//角色ID
        if (StringUtils.isEmpty(roleId)){
            List<SysRoleVo> list = sysRoleService.roleTreeList();
            String s = JSON.toJSONString(list);
            JSONArray jsonArray1 = listToTree.listToTree(JSONArray.parseArray(s), "id", "pid", "children");
            System.out.println(jsonArray1);
            return CommonReturnType.createCommonReturnType(jsonArray1);
        }else {
            List<SysRoleVo> list = sysRoleService.roleTreeListByRoleId(roleId);
            String s = JSON.toJSONString(list);
            JSONArray jsonArray1 = listToTree.listToTree(JSONArray.parseArray(s), "id", "pid", "children");
            System.out.println(jsonArray1);
            return CommonReturnType.createCommonReturnType(jsonArray1);
        }

    }

    /**
     * @auther: lizhen
     * @date: 2019-06-26 13:08
     * 功能描述: 给用户分配角色
     * 参数：角色id，用户Id
     */
    @RequestMapping(value = "saveRoleId")
    @ResponseBody
    @RequiresRoles("admin")
    @RequiresPermissions("userInfo:update")
    public CommonReturnType saveRoleId(String roleId,Integer uid) throws BusinessException {
        if ("1".equals(uid)){
            throw new BusinessException(EmBusinessError.CANT_CHANGE_ADMIN);
        }
        String[] array = roleId.split(",");
        System.out.println(array.toString());
        List<SysRole> list = new ArrayList<>();
        for (int i=0;i<array.length;i++){
            SysRole sr = new SysRole();
            sr.setId(Integer.parseInt(array[i]));
            list.add(sr);
        }
        UserInfo ui = new UserInfo();
        ui.setUid(uid);
        ui.setRoleList(list);
        ui.setYuliu1(roleId);                       //角色ID
        if (userInfoService.saveRoleId(ui)){
            return CommonReturnType.createCommonReturnType("操作成功","200");
        }
        return CommonReturnType.createCommonReturnType("操作失败","100");
    }


    /**
     * @auther: lizhen
     * @date: 2019-06-26 13:35
     * 功能描述: 重置用户密码
     */
    @RequestMapping(value = "resetPW",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    @RequiresRoles("admin")
    @RequiresPermissions("userInfo:update")
    public CommonReturnType resetPW(@RequestParam(name = "uid") Integer uid) throws BusinessException {
        if ("1".equals(uid)){
            throw new BusinessException(EmBusinessError.CANT_PASSWORD_ADMIN);
        }
        if (userInfoService.resetPW(uid)){
            return CommonReturnType.createCommonReturnType("成功","200");
        }
        return CommonReturnType.createCommonReturnType("失败","100");
    }


    /**
     * @auther: lizhen
     * @date: 2019-07-01 15:17
     * 功能描述: 删除用户
     */
    @RequestMapping(value = "userInfoDelete")
    @ResponseBody
    @RequiresPermissions("userInfo:del")
    @RequiresRoles("admin")
    public CommonReturnType userInfoDelete(String uid) throws BusinessException {
        if ("1".equals(uid)){
            throw new BusinessException(EmBusinessError.CANT_DELETE_ADMIN);
        }
        if (userInfoService.deleteUser(Integer.valueOf(uid))){
            return CommonReturnType.createCommonReturnType("删除用户成功","200");
        }
        return CommonReturnType.createCommonReturnType("删除用户失败","100");
    }


    /**
     * @auther: lizhen
     * @date: 2019-07-03 9:04
     * 功能描述: 清除给用户分配的角色
     */
    @RequestMapping("userInfoDeleteRole")
    @ResponseBody
    @RequiresPermissions("userInfo:del")
    @RequiresRoles("admin")
    public CommonReturnType userInfoDeleteRole(String uid,String roleId) throws BusinessException {
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(roleId)){
            return CommonReturnType.createCommonReturnType("操作失败，参数错误","100");
        }
        if ("1".equals(uid)){
            throw new BusinessException(EmBusinessError.CANT_CHANGE_ADMIN);
        }
        if (userInfoService.userInfoDeleteRole(uid,roleId)){
            return CommonReturnType.createCommonReturnType("操作成功","200");
        }
        return CommonReturnType.createCommonReturnType("操作失败","100");
    }








}
