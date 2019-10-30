package com.mdkproject.mdkshiro.modules.service;

import com.mdkproject.mdkshiro.entity.HearthCardInfo;

import java.util.List;
import java.util.Map;

/**
 * @auther: lizhen
 * @date: 2019/7/15 17:08
 * 功能描述: 健康证管理
 */
public interface HearthCardService {
    /**
     * 健康证有效期判断
     * */
    boolean selectByData(String idcardNum);

    /**
     * 健康证查询WEB端
     * */
    Map<String,String> selectByIdCardNum(String idCard);

    /**
     * 健康证查询小程序接口
     * */
    Map<String, Object> getXCX(String idCard) throws Exception;

    /**
     * 领取电子健康证
     * */
    boolean lingquXCX(Integer healthId);

    /**
     * 健康证点击查看详情
     * */
    Map<String,String> xiangqing(Integer healthId);

    /**
     * 健康证打印状态列表，状态0未打印1已打印
     * */
    List<HearthCardInfo> dayin(String hospitalNum, String printStatus, int page, int limit);
    /**
     * 健康证打印状态列表统计，状态0未打印1已打印
     * */
    int dayinCount(String hospitalNum, String printStatus);

    /**
     * 不合格健康证列表
     * */
    List<HearthCardInfo> healthCardNoPass(String hospitalNum, int page, int limit);

    /**
     * 不合格健康证列表统计
     * */
    int healthCardNoPassCount(String hospitalNum);



    /**
     * 单条修改打印状态
     * */
    boolean xuanzheDY(String healthId);

    /**
     * 批量打印，修改打印状态
     * */
    boolean batchUpdateData(String healthId);

    /**
     * 当日发证人数
     * */
    int getTodayData(String hospitalNum);

    /**
     * 打印获取健康证
     * */
    HearthCardInfo selectByIdCardNum2(String idCardNum);
    /**
     * 健康证多条件查询
     * */
    List<HearthCardInfo> keywordSelect(String printStatus,String status,String name, String idCard, String dates, int page, int limit);
    /**
     * 多条件查询统计
     * */
    int keywordSelectCount(String printStatus,String status,String name, String idCard, String dates);
}
