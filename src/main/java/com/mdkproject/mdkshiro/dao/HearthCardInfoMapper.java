package com.mdkproject.mdkshiro.dao;

import com.mdkproject.mdkshiro.entity.HearthCardInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface HearthCardInfoMapper {
    int deleteByPrimaryKey(Integer healthId);

    int insert(HearthCardInfo record);

    HearthCardInfo selectByPrimaryKey(Integer healthId);

    List<HearthCardInfo> selectAll();

    int updateByPrimaryKey(HearthCardInfo record);

    /**
     * 依据身份证号查询
     * */
    HearthCardInfo selectByIdCardNum(String idcardNum);

    /**
     * 批量录入健康证信息
     * */
    void batchInsertData(List<HearthCardInfo> list);

    /**
     * 获取健康证信息
     * */
    Map<String,String> selectByIdCardNum2(String idCard);

    /**
     * 健康证详情
     * */
    Map<String, String> selectByHealthid(Integer healthId);

    /**
     * 健康证打印状态数据
     * */
    List<HearthCardInfo> dayin(Map<String, Object> data);

    /**
     * 健康证打印状态列表统计
     * */
    int dayinCount(String hospitalNum, String printStatus);

    /**
     * 不合格健康证列表
     * */
    List<HearthCardInfo> healthCardNoPass(Map<String, Object> data);

    /**
     * 不合格健康证列表统计
     * */
    int healthCardNoPassCount(@Param("hospitalNum") String hospitalNum);

    /**
     * 健康证多条件查询
     * */
    List<HearthCardInfo> keywordSelect(String printStatus,
                                       String status,
                                       String name,
                                       String idCard,
                                       String startTime,
                                       String endTime, int page, int limit);

    /**
     * 批量打印，修改打印状态
     * */
    void batchUpdateData(List<String> list);

    /**
     * 当日发证人数
     * */
    int getTodayData(String hospitalNum);

    /**
     * 一天发证人数统计
     * */
    int tjAddfzWeekData(String oneDay, String hospitalNum);

    /**
     *  多条件查询统计
     * */
    int keywordSelectCount(String printStatus,
                           String status,
                           String name,
                           String idCard,
                           String startTime,
                           String endTime);
}