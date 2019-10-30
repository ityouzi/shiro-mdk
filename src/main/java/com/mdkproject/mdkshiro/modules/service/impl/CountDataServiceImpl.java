package com.mdkproject.mdkshiro.modules.service.impl;

import com.mdkproject.mdkshiro.dao.HospitalInfoMapper;
import com.mdkproject.mdkshiro.dao.TiJianInfoMapper;
import com.mdkproject.mdkshiro.entity.HospitalInfo;
import com.mdkproject.mdkshiro.entity.TiJianInfo;
import com.mdkproject.mdkshiro.modules.service.CountDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: mdk-shiro->CountDataServiceImpl
 * @description: 统计模块
 * @author: lizhen
 * @create: 2019-07-17 10:14
 **/
@Slf4j
@Service
public class CountDataServiceImpl implements CountDataService {

    @Autowired
    private TiJianInfoMapper tiJianInfoMapper;
    @Autowired
    private HospitalInfoMapper hospitalInfoMapper;


    /**
     * 区域+医院+时间
     * @param hospitalNum
     * @param dates
     * @param page
     * @param limit
     */
    @Override
    public List<TiJianInfo> selectByAreaNumAndHospitalNum(String hospitalNum, String dates, int page, int limit) {
        List<TiJianInfo> lists = new ArrayList<>();
        Map<String,Object> data = new HashMap<>();
        HospitalInfo hospitalInfo = hospitalInfoMapper.selectByHospitalNum(hospitalNum);
        String startTime;
        String endTime;
        if (dates==null){
            startTime  = null;
            endTime = null;
        }else{
            String[] split = dates.split(" - ");
            startTime  = split[0];
            endTime = split[1];
        }
        data.put("hospitalNum",hospitalNum);                        //医院编号
        data.put("startTime",startTime);                            //开始时间
        data.put("endTime",endTime);                                //结束时间
        data.put("page",(page-1)*limit);
        data.put("limit",limit);
        List<TiJianInfo> list = tiJianInfoMapper.selectByAreaNumAndHospitalNum(data);
        for (int i=0;i<list.size();i++){
            TiJianInfo tiJianInfo = list.get(i);
            tiJianInfo.setYuliu3(hospitalInfo.getHospitalName());
            lists.add(tiJianInfo);
        }
        return lists;
    }


    /**
     * 医院+时间(统计)
     * @param hospitalNum
     * @param dates
     */
    @Override
    public int selectHospitalNumCount(String hospitalNum, String dates) {
        String startTime;
        String endTime;
        if (dates==null){
            startTime  = null;
            endTime = null;
        }else{
            String[] split = dates.split(" - ");
            startTime  = split[0];
            endTime = split[1];
        }
        return tiJianInfoMapper.selectHospitalNumCount(hospitalNum,startTime,endTime);
    }

}
