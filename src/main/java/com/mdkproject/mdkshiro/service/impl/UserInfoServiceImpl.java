package com.mdkproject.mdkshiro.service.impl;

import com.mdkproject.mdkshiro.dao.*;
import com.mdkproject.mdkshiro.entity.*;
import com.mdkproject.mdkshiro.error.BusinessException;
import com.mdkproject.mdkshiro.error.EmBusinessError;
import com.mdkproject.mdkshiro.service.UserInfoService;
import com.mdkproject.mdkshiro.utils.MyMD5Util;
import com.mdkproject.mdkshiro.utils.MyTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Liberal-World
 * @Date: 2019-06-18 09:42
 * @Description: 用户信息
 * @Version 1.0
 */
@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {


    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;//用户-角色
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;//角色-部门
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private KeyCodeMapper keyCodeMapper;
    @Autowired
    private HospitalInfoMapper hospitalInfoMapper;


    /**
     * @auther: lizhen
     * @date: 2019-06-18 10:00
     * 功能描述: 添加用户
     * @param ui
     */
    @Override
    public boolean save(UserInfo ui){
        try {
            //判断账号是否重复
            UserInfo byAccount = this.findByAccount(ui.getAccount());
            if (byAccount!=null){
                throw new BusinessException(EmBusinessError.USER_IS_EXIT);//用户已存在
            }
            ui.setStatus(0);//默认冻结账号
            ui.setCreateTime(MyTools.getTime());
            ui.setUpdateTime(MyTools.getTime());
            String salt = MyTools.getRandomSalt(5);
            ui.setSalt(salt);//盐
            String newPassword = MyMD5Util.encodePassword(ui.getPassword(),salt);//密码加密（密码，盐）
            ui.setPassword(newPassword);
            userInfoMapper.insertSelective(ui);
            return true;
        }catch (Exception e){
            log.info("添加用户异常："+e.getMessage());
            return false;
        }
    }
    
    /**
     * @auther: lizhen
     * @date: 2019-06-18 10:05
     * 功能描述: 通过账号获取用户
     */
    @Override
    public UserInfo findByAccount(String account) {
        UserInfo userInfo = userInfoMapper.findByAccount(account);
        return userInfo;
    }


    /**
     * @auther: lizhen
     * @date: 2019-06-18 11:41
     * 功能描述: 用户禁用或启用1启动2禁用
     */
    @Override
    public boolean updateStatus(UserInfo ui) {
        try {
            UserInfo userInfo = userInfoMapper.selectByPrimaryKey(ui.getUid());
            userInfo.setStatus(ui.getStatus());
            userInfo.setUpdateTime(MyTools.getTime());
            userInfoMapper.updateByPrimaryKey(userInfo);
            return true;
        } catch (Exception e) {
            log.info("操作异常："+e.getMessage());
            return false;
        }
    }

    /**
     * @auther: lizhen
     * @date: 2019-06-26 13:28
     * 功能描述: 获取用户 --uid
     */
    @Override
    public UserInfo findByOne(Integer uid) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(uid);
        return userInfo;
    }

    /**
     * @auther: lizhen
     * @date: 2019-06-26 13:31
     * 功能描述: 分配角色,已有角色就不能添加了
     *
     */
    @Override
    public boolean saveRoleId(UserInfo ui) {
        try {
            Integer uid = ui.getUid();
            List<SysRole> roleList = ui.getRoleList();
            for (int i=0;i<roleList.size();i++){
                SysRole sysRole = roleList.get(i);
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUid(uid);
                sysUserRole.setRoleId(sysRole.getId());
                sysUserRoleMapper.insertSelective(sysUserRole);             //用户角色关系表
            }
            userInfoMapper.updateRoleid(uid,ui.getYuliu1());                //更新用户表角色字段
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("分配角色异常信息："+e.getMessage());
            return false;
        }
    }





    /**
     * @auther: lizhen
     * @date: 2019-06-18 11:56
     * 功能描述: 修改密码
     */
    @Override
    public boolean updatePassword(String password, String uid) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(Integer.valueOf(uid));
        String updateTime = MyTools.getTime();
        if (userInfo==null){
            return false;
        }
        try {
            String salt = MyTools.getRandomSalt(5);               //盐
            String newPassword = MyMD5Util.encodePassword(password,salt);//密码+盐
            userInfo.setSalt(salt);
            userInfo.setPassword(newPassword);
            userInfo.setUpdateTime(updateTime);
            userInfoMapper.updateByPrimaryKey(userInfo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("修改密码异常信息："+e.getMessage());
            return false;
        }
    }


    /**
     * @auther: lizhen
     * @date: 2019-06-26 12:14
     * 功能描述: 用户管理列表 部门多对多角色，用户多对多角色
     */
    @Override
    public List<UserInfo> findAllUser(int page, int limit) {
        List<UserInfo> list = new ArrayList<>();

        Map<String,Object> data = new HashMap<>();
        data.put("page",(page-1)*limit);
        data.put("limit",limit);
        List<UserInfo> userInfoList = userInfoMapper.selectPageUserCount(data);//分页用户
        List<String> listStr = new ArrayList<>();
        String roleName;
        for (UserInfo ui:userInfoList){
            if (StringUtils.isNotEmpty(ui.getYuliu1())){
                String[] roles = ui.getYuliu1().split(",");         //角色ID
                if (roles.length<=1){
                    SysRole sysRole = sysRoleMapper.selectByPrimaryKey(Integer.valueOf(roles[0]));
                    roleName = sysRole.getDescription();
                }else{
                    for (int i=0;i<roles.length;i++){
                        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(Integer.valueOf(roles[i]));
                        listStr.add(sysRole.getDescription());
                    }
                    roleName= listStr.toString();
                }
                System.out.println(roleName);
                Integer permissionId = Integer.parseInt(ui.getYuliu2());    //部门ID
                SysPermission sysPermission = sysPermissionMapper.selectByPrimaryKey(permissionId);
                ui.setYuliu2(sysPermission.getName());
                ui.setYuliu1(roleName);
                list.add(ui);
            }else{
                Integer permissionId = Integer.parseInt(ui.getYuliu2());    //部门ID
                SysPermission sysPermission = sysPermissionMapper.selectByPrimaryKey(permissionId);
                ui.setYuliu1("");
                ui.setYuliu2(sysPermission.getName());
                list.add(ui);
            }

        }
        return list;
    }


    /**
     * @auther: lizhen
     * @date: 2019-06-26 13:03
     * 功能描述: 修改用户信息
     */
    @Override
    public boolean update(UserInfo ui) {
        try {
            ui.setUpdateTime(MyTools.getTime());
            userInfoMapper.updateByPrimaryKey(ui);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("修改用户异常信息："+e.getMessage());
            return false;
        }
    }


    /**
     * @auther: lizhen
     * @date: 2019-06-26 13:39
     * 功能描述: 重置用户密码
     */
    @Override
    public boolean resetPW(Integer uid) {
        try {
            UserInfo ui = userInfoMapper.selectByPrimaryKey(uid);
            String newPassword = MyMD5Util.encodePassword("123456",ui.getSalt());//密码加密（密码，盐）
            ui.setPassword(newPassword);
            ui.setUpdateTime(MyTools.getTime());
            userInfoMapper.updateByPrimaryKeySelective(ui);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            log.info("重置密码异常："+e.getMessage());
            return false;
        }

    }
    
    
    /**
     * @auther: lizhen
     * @date: 2019-06-28 10:02
     * 功能描述: 获取角色列表，通过用户id
     */
    @Override
    public List<SysRole> roleTreeListByUserId(Integer uid) {
        List<SysRole> lists = new ArrayList();
        List<SysUserRole> list = sysUserRoleMapper.selectByUid(uid);//角色集合ID
        for (int i=0;i<list.size();i++){
            SysUserRole sysUserRole = list.get(i);
            SysRole sysRole = sysRoleMapper.selectByPrimaryKey(sysUserRole.getRoleId());
            lists.add(sysRole);
        }
        return lists;
    }

    /**
     * @auther: lizhen
     * @date: 2019-07-01 9:10
     * 功能描述: 用户总数
     */
    @Override
    public int CountUserData() {
        int i = userInfoMapper.countUser();
        return i;
    }

    /**
     * @auther: lizhen
     * @date: 2019-07-01 15:21
     * 功能描述: 删除用户
     */
    @Override
    @Transactional
    public boolean deleteUser(Integer uid) {
        try {
            userInfoMapper.deleteByPrimaryKey(uid);                         //用户
            sysUserRoleMapper.deleteByUid(uid);                             //用户角色表
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("删除用户异常信息："+e.getMessage());
            return false;
        }
    }


    /**
     * @auther: lizhen
     * @date: 2019-07-03 9:10
     * 功能描述: 清除给用户分配的角色
     */
    @Override
    public boolean userInfoDeleteRole(String uid, String roleId) {
        try {
            sysUserRoleMapper.deleteByUidAndRoleId(uid,roleId);//用户角色
            userInfoMapper.updateRoleid(Integer.valueOf(uid),roleId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断账号是否存在
     * @param account
     */
    @Override
    public String selectByAccount(String account) {
        String key = userInfoMapper.selectByAccount(account);
        if (key==null){
            return null;
        }
        return key;
    }

    /***
     *
     *
     * @param key*/
    /**
     * @auther: lizhen
     * @date: 2019/7/11 16:14
     * 功能描述: 激活码查询
     */
    @Override
    public Map<String,String> getKeyCode(String key) {
        Map<String,String> map = new HashMap<>();
        KeyCode keyCode = keyCodeMapper.selectBykeyCode(key);
        if (keyCode==null){
            map.put("result","0");                              //激活码不存在
        }else {
            String status = keyCode.getKeyStatus()+"";
            if ("1".equals(status)){                            //激活成功
                String hospitalNum = keyCode.getHospitalNum();
                HospitalInfo hospitalInfo = hospitalInfoMapper.selectByHospitalNum(hospitalNum);
                map.put("result",hospitalNum);
                map.put("hospiatlName",hospitalInfo.getHospitalName());
                keyCode.setUpdateTime(MyTools.getTime());
                keyCode.setKeyStatus(2);
                keyCodeMapper.updateByPrimaryKey(keyCode);
            }else{
                map.put("result","2");                          //激活码失效
            }
        }
        return map;
    }

    /**
     * 生成激活码
     * @param hospitalNum
     */
    @Override
    public boolean addKeyCode(String hospitalNum) {
        try {
            KeyCode keyCode = new KeyCode();
            keyCode.setKeyCode(MyTools.getStringRandom(16));
            keyCode.setHospitalNum(hospitalNum);
            keyCode.setKeyStatus(1);
            keyCode.setCreateTime(MyTools.getTime());
            keyCode.setUpdateTime(MyTools.getTime());
            keyCodeMapper.insert(keyCode);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 激活码列表
     * @param page
     * @param limit
     */
    @Override
    public List<KeyCode> keyList(int page, int limit) {
        Integer page1 = (page-1)*limit;
        Integer limit1 = limit;
        List<KeyCode> keyCodes = keyCodeMapper.selectAll(page1,limit1);
        return keyCodes;
    }

    /**
     * 激活码列表统计
     */
    @Override
    public int keyListCount() {
        return keyCodeMapper.keyListCount();
    }


}
