package com.mdkproject.mdkshiro.dao;

import com.mdkproject.mdkshiro.entity.ExcelModel;
import com.mdkproject.mdkshiro.entity.TiJianInfo;
import java.util.List;
import java.util.Map;

public interface TiJianInfoMapper {
    int deleteByPrimaryKey(Integer tjId);

    int insert(TiJianInfo record);

    TiJianInfo selectByPrimaryKey(Integer tjId);

    List<TiJianInfo> selectAll();

    int updateByPrimaryKey(TiJianInfo record);

    /**
     * 依据身份证号获取
     * */
    TiJianInfo selectByIdCardNum(String idcardNum);

    /**
     * 体检单条审核
     * */
    int OneupdateTJstatus(String tjId,
                          String status,
                          String startdate,
                          String enddate,
                          String why);


    /**
     * 批量审核体检信息(未审核的)
     * */
    void batchUpTJdate(List<String> list,
                       String status,
                       String startdate,
                       String enddate,
                       String why);

    /**
     * 获取体检当月体检数据
     * */
    int getMonthData(String hospitalNum);

    /**
     * 获取体检最近一周数据
     * */
    List<TiJianInfo> getWeekData(Map<String, Object> data);
    /**
     * 获取体检最近一周数据统计
     * */
    int getWeekDataCount(String hospitalNum, String status);

    /**
     * 查询当日体检人数
     * */
    int getTodayData(String hospitalNum);

    /**
     * 查询一家医院体检总人数
     * */
    int getAllByHospitalNum(String hospitalNum);

    /**
     * 体检审核状态列表
     * */
    List<TiJianInfo> getStatusList(Map<String, Object> data);

    /**
     * 体检审核状态列表统计
     * */
    int getStatusListCount(String hospitalNum, String status);

    /**
     * 体检信息审核多条件查询
     **/
    List<TiJianInfo> keywordSelect(Map<String, Object> data);

    /**
     * 一天体检人数统计
     * */
    int tjAddfzWeekData(String oneDay, String hospitalNum);

    /**
     * 区域+医院+时间
     * */
    List<TiJianInfo> selectByAreaNumAndHospitalNum(Map<String, Object> data);

    /**
     * 区域+医院+时间(统计)
     * */
    int selectHospitalNumCount(String hospitalNum, String startTime, String endTime);

    /**
     * web端补录
     * */
    void updateByKey(TiJianInfo tiJianInfo);

    /**
     *  excel导入导出
     * */
    void batchInsert(List<ExcelModel> list);


    /**
     * 获取体检机构最新一条数据
     */
    String getNewData(String hospitalNum);
}