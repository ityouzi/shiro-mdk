package com.mdkproject.mdkshiro.dao;

import com.mdkproject.mdkshiro.entity.HospitalInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface HospitalInfoMapper {
    int deleteByPrimaryKey(Integer hospitalId);

    int insert(HospitalInfo record);

    HospitalInfo selectByPrimaryKey(Integer hospitalId);

    List<HospitalInfo> selectAll();

    int updateByPrimaryKey(HospitalInfo record);

    /**
     * 依据区域编号获取对应的医院集合
     * */
    List<HospitalInfo> selectHospitalByAreaNum(String areaNum);

    /**
     * 医院列表
     * */
    List<HospitalInfo> hospitalList(Map<String, Object> param);

    /**
     * 医院数量统计
     * */
    int hospitalCount();

    /**
     * 获取医院信息通过医院编号
     * */
    HospitalInfo selectByHospitalNum(@Param("hospitalNum") String hospitalNum);

    /**
     * 获取医院编号集合
     * */
    List<String> hospitalNumListByAreaNum(String areaNum);

    /**
     * 医院模板管理
     * */
    void updateTemplate(Integer hospitalId, String templateNum, String time);

    /**
     * 医院每天处理体检人数控制
     * */
    void updatetjNum(Integer hospitalId, String num);

    /**
     * 设置医院体检时间
     * */
    void UpdateHospitalTime(Integer hospitalId, String time);
}