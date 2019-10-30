package com.mdkproject.mdkshiro.modules.service;

import com.mdkproject.mdkshiro.entity.HospitalInfo;

import java.util.List;

/**
 * @program: mdk-shiro->HospitalService
 * @description: 体检医院
 * @author: lizhen
 * @create: 2019-07-12 15:15
 **/
public interface HospitalService {

    /**
     * 依据区域编号获取对应的医院集合
     * */
    List<HospitalInfo> selectNum(String areaNum);

    /**
     * 添加医院信息
     * */
    boolean hospitalAdd(HospitalInfo hospitalInfo);

    /**
     * 医院列表
     * */
    List<HospitalInfo> hospitalList(int page, int limit);

    /**
     * 医院数量统计
     * */
    int hospitalCount();

    /**
     * 获取医院编号集合
     * */
    List<String> hospitalNumListByAreaNum(String areaNum);

    /**
     * 通过医院编号获取
     * */
    HospitalInfo selectByHospitalNum(String hospitalNum);

    /**
     * 医院模板管理
     * */
    boolean templateManager(Integer hospitalId, String templateNum);

    /**
     * 医院每天处理体检人数控制
     * */
    boolean tjNum(Integer hospitalId, String num);

    /**
     * 设置医院体检时间
     * */
    boolean hospitalTime(Integer hospitalId, String time);
}
