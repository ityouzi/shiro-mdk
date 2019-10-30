package com.mdkproject.mdkshiro.service;

import com.mdkproject.mdkshiro.entity.KeyCode;
import com.mdkproject.mdkshiro.entity.SysRole;
import com.mdkproject.mdkshiro.entity.UserInfo;

import java.util.List;
import java.util.Map;

/**
 * @Auther: Liberal-World
 * @Date: 2019-06-18 09:41
 * @Description: 用户信息模块
 * @Version 1.0
 */
public interface UserInfoService {

    //添加用户
    boolean save(UserInfo ui);
    //通过账号获取用户
    UserInfo findByAccount(String account);



    //修改密码
    boolean updatePassword(String password, String uid);
    //用户列表
    List<UserInfo> findAllUser(int currPage, int pageSize);
    //修改用户
    boolean update(UserInfo ui);
    //用户禁用或启用
    boolean updateStatus(UserInfo ui);
    //获取用户
    UserInfo findByOne(Integer uid);
    //分配角色
    boolean saveRoleId(UserInfo ui);
    //重置密码
    boolean resetPW(Integer uid);

    List<SysRole> roleTreeListByUserId(Integer uid);

    //用户总数
    int CountUserData();

    //删除用户
    boolean deleteUser(Integer uid);

    //清除给用户分配的角色，同时清除用户字段的角色
    boolean userInfoDeleteRole(String uid, String roleId);

    /**
     * 判断账号是否存在
     * */
    String selectByAccount(String account);

    /***
     *  获取激活码
     * */
    Map<String, String> getKeyCode(String key);

    /**
     *  生成激活码
     * */
    boolean addKeyCode(String hospitalNum);

    /**
     * 激活码列表
     *
     * @param page
     * @param limit*/
    List<KeyCode> keyList(int page, int limit);

    /**
     * 激活码列表统计
     * */
    int keyListCount();
}
