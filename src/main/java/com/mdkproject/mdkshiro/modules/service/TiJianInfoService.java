package com.mdkproject.mdkshiro.modules.service;

import com.mdkproject.mdkshiro.entity.ExcelModel;
import com.mdkproject.mdkshiro.entity.HearthCardInfo;
import com.mdkproject.mdkshiro.entity.TiJianInfo;
import com.mdkproject.mdkshiro.error.BusinessException;
import com.mdkproject.mdkshiro.modules.service.model.TjshModel;

import java.util.List;
import java.util.Map;

public interface TiJianInfoService {




    /**
     * 判断是否满11个月
     * */
    String selectByIdCardNum(String idcardNum);

    /**
     * 体检信息快速录入
     * */
    HearthCardInfo quickInput(TiJianInfo tiJianInfo) throws BusinessException;

    /**
     * 体检信息基础录入
     * */
    TiJianInfo slowInput(TiJianInfo tiJianInfo) throws BusinessException;

    /**
     * 体检单条审核
     *
     **/
    TiJianInfo oneTJSH(TjshModel tjshModel);

    /**
     * 体检信息批量审核
     * */
    boolean batchUpTJdate(TjshModel tjshModel);

    /**
     * 安卓端体检信息录入
     * */
    boolean AndroidInput(TiJianInfo tiJianInfo);

    /**
     * 获取体检当月体检数据
     * */
    int getMonthData(String hospitalNum);

    /**
     * 获取体检最近一周数据
     * */
    List<TiJianInfo> getWeekData(String hospitalNum, String status, int page, int limit) throws Exception;
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
    List<TiJianInfo> getStatusList(String hospitalNum, String status, int page, int limit);
    /**
     * 体检审核状态列表统计
     * */
    int getStatusListCount(String hospitalNum, String status);

    /**
     * 体检信息审核多条件查询
     * */
    List<TiJianInfo> keywordSelect(String name,String number,String idCardNum,String dates);


    /**
     * 当日体检&发证统计,查询一周数据
     * */
    Map<String, Object> tjAddfzWeekData(List<String> dateToWeek, String hospitalNum);

    /**
     * 表
     * */
    TiJianInfo getTable(Integer tjId) throws Exception;


    /**
     * 保存&打印健康证
     * */
    HearthCardInfo getHealthCardInfo(Integer healthId);

    /**
     * web端补录
     * */
    boolean puluInfo(TiJianInfo tiJianInfo);


    /**
     * excel导入导出
     * */
    boolean importExcel(List<ExcelModel> successList);

    /**
     * 获取体检机构最新一条数据
     */
    String getNewData(String hospitalNum);
}
