package com.mdkproject.mdkshiro.dao;

import com.mdkproject.mdkshiro.entity.DeptYuYueInfo;

import java.util.List;
import java.util.Map;

public interface DeptYuYueInfoMapper {
    int deleteByPrimaryKey(Integer deptId);

    int insert(DeptYuYueInfo record);

    DeptYuYueInfo selectByPrimaryKey(Integer deptId);


    /**
     * 预约列表
     * */
    List<DeptYuYueInfo> selectAll(Map<String, Object> data);

    int updateByPrimaryKey(DeptYuYueInfo record);

    /**
     * 单位预约审核状态统计
     * */
    int deptCountBYhospitalNum(String hospitalNum, String deptShenhe);

    /**
     * 待审核总数
     * */
    int dshCount(String hospitalNum, String status);

    /**
     * 已审核总数
     * */
    int yshCount(String hospitalNum, String status1, String status2);

    /**
     * 单位预约资质审核
     * */
    void updateByDeptId(String deptId, String deptShenhe, String time, String why, String updateTime);

    /**
     * 单位体检预约
     * */
    DeptYuYueInfo selectBYcompanyCode(String deptCode);

    /**
     * 当日预约单位数
     * */
    int getTodayData(String hospitalNum);

    /**
     * 多条件查询
     * */
    List<DeptYuYueInfo> keywordSelect(DeptYuYueInfo deptYuYueInfo, String hospitalNum);

    /**
     * 获取一周每一天数据
     * */
    int getOneData(String oneDay, String hospitalNum);

    /**
     * 判断是否是第一次预约，如果不是依据征信代码，判断审核状态
     * */
    DeptYuYueInfo selectByCompanyCode(String deptOrganization);

    /**
     * 单位体检预约判断，最近一次
     * */
    DeptYuYueInfo getZuiJinOneOrder(String deptCode);

    /**
     * 判断失信次数,状态为0
     * */
    int yuyueCount(String deptCode);

    /**
     * 预约未体检&预约已体检列表
     * */
    List<DeptYuYueInfo> tjStatusList(Map<String, Object> data);

    /**
     * 预约未体检&预约已体检列表统计
     * */
    int tjStatusListCount(String tjStatus, String hospitalNum);

    /**
     * 修改体检时间
     * */
    void updateTJtime(Integer deptId, String time);

    /**
     * 当天是否可以继续预约
     * */
    List<Map<String,Object>> companyOrder(String hospitalNum, String time);
}