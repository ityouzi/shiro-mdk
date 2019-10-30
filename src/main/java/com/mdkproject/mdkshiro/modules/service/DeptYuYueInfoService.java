package com.mdkproject.mdkshiro.modules.service;

import com.mdkproject.mdkshiro.entity.DeptYuYueInfo;
import com.mdkproject.mdkshiro.error.BusinessException;

import java.util.List;
import java.util.Map;

/**
 * @program: mdk-shiro->DeptYuYueInfoService
 * @description: 单位预约
 * @author: lizhen
 * @create: 2019-07-12 12:12
 **/
public interface DeptYuYueInfoService {

    /**
     * 单位预约列表
     * */
    List<DeptYuYueInfo> deptList(String hospitalNum,String deptShenhe,int page, int limit);

    /**
     * 单位预约审核状态统计
     * */
    int deptCountBYhospitalNum(String hospitalNum, String deptShenhe);

    /**
     * 待审核总数
     * */
    int dshCount(String hospitalNum);

    /**
     * 已审核总数
     * */
    int yshCount(String hospitalNum);

    /**
     * 单位预约资质审核
     * */
    boolean deptSH(String deptId,String deptShenhe,String deptPhone,String time,String why);

    /**
     * 单位体检预约
     * */
    Map<String,String> deptOrder(DeptYuYueInfo deptYuYueInfo);

    /**
     * 查看单位预约资质信息
     * */
    List<String> deptInfoLook(Integer deptId) throws Exception;

    /**
     * 多条件查询
     * */
    List<DeptYuYueInfo> keywordSelect(String hospitalNum, DeptYuYueInfo deptYuYueInfo) throws BusinessException;

    /**
     * 当日预约单位数
     * */
    int getTodayData(String hospitalNum);

    /**
     * 获取一周每一天数据
     * */
    int getOneData(String oneDay, String hospitalNum);

    /**
     * 判断是否是第一次预约，如果不是依据征信代码，判断审核状态
     * */
    Map<String, Object> selectByCompanyCode(String deptOrganization);

    /**
     * 单位体检预约判断
     * */
    DeptYuYueInfo getZuiJinOneOrder(String deptCode);


    /**
     * 预约录入
     * */
    Map<String,Object> InsertDeptInfo(DeptYuYueInfo deptYuYueInfo,String apm);

    /**
     * 判断失信次数
     * */
    int yuyueCount(String deptCode);

    /**
     * 预约未体检&预约已体检
     * */
    List<DeptYuYueInfo> tjStatusList(String tjStatus, String hospitalNum, int page, int limit);

    /**
     * 预约未体检&预约已体检统计
     * */
    int tjStatusListCount(String tjStatus, String hospitalNum);

    /**
     *  预约未体检&预约已体检确认时间
     * */
    int tjStatusUpdate(Integer deptId,String tjStatus);

    /**
     * 修改体检时间
     * */
    boolean updateTJtime(Integer deptId, String time);

    /**
     * 单位体检时间预约
     * */
    Map<String, Object> companyOrder(String hospitalNum,String time);

    /**
     * 监督机关对预约资质审核
     * */
    boolean zzsh(Integer deptId, String status);
}
