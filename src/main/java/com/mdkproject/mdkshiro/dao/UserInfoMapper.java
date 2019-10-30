package com.mdkproject.mdkshiro.dao;

import com.mdkproject.mdkshiro.entity.UserInfo;
import com.mdkproject.mdkshiro.entity.UserInfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserInfoMapper {

    long countByExample(UserInfoExample example);

    int deleteByExample(UserInfoExample example);


    int deleteByPrimaryKey(Integer uid);


    int insert(UserInfo record);


    int insertSelective(UserInfo record);

    List<UserInfo> selectByExample(UserInfoExample example);

    UserInfo selectByPrimaryKey(Integer uid);

    int updateByExampleSelective(@Param("record") UserInfo record, @Param("example") UserInfoExample example);

    int updateByExample(@Param("record") UserInfo record, @Param("example") UserInfoExample example);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);
    
    UserInfo findByAccount(String account);

    List<UserInfo> findAll();
    //分页数据
    List<UserInfo> selectPageUserCount(Map<String, Object> data);

    //用户总数
    int countUser();
    //更新用户表角色字段
    void updateRoleid(Integer uid, String roleIds);

    /**
     * 判断账号是否存在
     * */
    String selectByAccount(String account);
}