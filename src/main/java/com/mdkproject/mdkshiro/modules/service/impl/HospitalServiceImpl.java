package com.mdkproject.mdkshiro.modules.service.impl;

import com.mdkproject.mdkshiro.dao.HospitalInfoMapper;
import com.mdkproject.mdkshiro.entity.HospitalInfo;
import com.mdkproject.mdkshiro.modules.service.HospitalService;
import com.mdkproject.mdkshiro.utils.MyTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: mdk-shiro->HospitalServiceImpl
 * @description: 体检医院
 * @author: lizhen
 * @create: 2019-07-12 16:24
 **/
@Slf4j
@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalInfoMapper hospitalInfoMapper;


    /**
     * 依据区域编号获取对应的医院集合
     * @param areaNum
     */
    @Override
    public List<HospitalInfo> selectNum(String areaNum) {
        List<HospitalInfo> list = hospitalInfoMapper.selectHospitalByAreaNum(areaNum);
        return list;
    }

    /**
     * 添加医院信息
     * @param hospitalInfo
     */
    @Override
    public boolean hospitalAdd(HospitalInfo hospitalInfo) {
        try {
            hospitalInfo.setCreateTime(MyTools.getTime());
            hospitalInfo.setUpdateTime(MyTools.getTime());
            hospitalInfoMapper.insert(hospitalInfo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("添加医院信息异常"+e);
            return false;
        }
    }

    /**
     * 医院列表
     * @param page
     * @param limit
     */
    @Override
    public List<HospitalInfo> hospitalList(int page, int limit) {
        Map<String,Object> param = new HashMap<>();
        param.put("page",(page-1)*limit);
        param.put("limit",limit);
        return hospitalInfoMapper.hospitalList(param);
    }

    /**
     * 医院数量统计
     */
    @Override
    public int hospitalCount() {
        return hospitalInfoMapper.hospitalCount();
    }

    /**
     * 获取医院编号集合
     * @param areaNum
     */
    @Override
    public List<String> hospitalNumListByAreaNum(String areaNum) {
        return hospitalInfoMapper.hospitalNumListByAreaNum(areaNum);
    }

    /**
     * 通过医院编号获取
     * @param hospitalNum
     */
    @Override
    public HospitalInfo selectByHospitalNum(String hospitalNum) {
        HospitalInfo hospitalInfo = hospitalInfoMapper.selectByHospitalNum(hospitalNum);
        if (hospitalInfo == null){
            return null;
        }
        return hospitalInfo;
    }

    /**
     * 医院模板管理
     * @param hospitalId
     * @param templateNum
     */
    @Override
    public boolean templateManager(Integer hospitalId, String templateNum) {
        HospitalInfo hospitalInfo = hospitalInfoMapper.selectByPrimaryKey(hospitalId);
        if (hospitalInfo != null){
            String time = MyTools.getTime();
            hospitalInfoMapper.updateTemplate(hospitalId,templateNum,time);
            return true;
        }
        return false;
    }

    /**
     * 医院每天处理体检人数控制
     * @param hospitalId
     * @param num
     */
    @Override
    public boolean tjNum(Integer hospitalId, String num) {
        try {
            hospitalInfoMapper.updatetjNum(hospitalId,num);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置医院体检时间
     *
     * @param hospitalId
     * @param time
     */
    @Override
    public boolean hospitalTime(Integer hospitalId, String time) {
        try {
            hospitalInfoMapper.UpdateHospitalTime(hospitalId,time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
