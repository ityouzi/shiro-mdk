package com.mdkproject.mdkshiro.modules.service.impl;

import com.mdkproject.mdkshiro.dao.HearthCardInfoMapper;
import com.mdkproject.mdkshiro.dao.HospitalInfoMapper;
import com.mdkproject.mdkshiro.dao.TiJianInfoMapper;
import com.mdkproject.mdkshiro.entity.HearthCardInfo;
import com.mdkproject.mdkshiro.entity.HospitalInfo;
import com.mdkproject.mdkshiro.entity.TiJianInfo;
import com.mdkproject.mdkshiro.modules.service.HearthCardService;
import com.mdkproject.mdkshiro.modules.util.Base64Util;
import com.mdkproject.mdkshiro.modules.util.DateToStrUtil;
import com.mdkproject.mdkshiro.utils.MyTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: mdk-shiro->HearthCardServiceImpl
 * @description: 健康证管理
 * @author: lizhen
 * @create: 2019-07-15 17:09
 **/
@Slf4j
@Service
public class HearthCardServiceImpl implements HearthCardService {

    @Autowired
    private HearthCardInfoMapper hearthCardInfoMapper;
    @Autowired
    private HospitalInfoMapper hospitalInfoMapper;
    @Autowired
    private TiJianInfoMapper tiJianInfoMapper;

    /**
     * 健康证有效期判断
     * @param idcardNum
     */
    @Override
    public boolean selectByData(String idcardNum) {
        try {
            HearthCardInfo hearthCardInfo = hearthCardInfoMapper.selectByIdCardNum(idcardNum);
            if (hearthCardInfo != null){
                String startTime = hearthCardInfo.getStartTime();       //发证日期
                String dateShort = DateToStrUtil.getStringDateShort();  //当前日期
                Long aLong = DateToStrUtil.between_days(startTime, dateShort);
                if (aLong<365){
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 健康证查询WEB端
     * @param idCard
     */
    @Override
    public Map<String,String> selectByIdCardNum(String idCard) {
        Map<String, String> map = hearthCardInfoMapper.selectByIdCardNum2(idCard);
        if (map == null){
            return null;
        }else{
            String hospital_id = map.get("hospital_id");
            HospitalInfo hospitalInfo = hospitalInfoMapper.selectByHospitalNum(hospital_id);
            String hospitalGz = hospitalInfo.getHospitalGz();   //公章
            String hospitalName = hospitalInfo.getHospitalName();//名称
            try {
                String qrCode = Base64Util.encodeFile(map.get("qr_code"));
                String photo = Base64Util.encodeFile(map.get("idcard_photo"));
                String gz = Base64Util.encodeFile(hospitalGz);
                map.put("qr_code",qrCode);
                map.put("photo",photo);
                map.put("gz",gz);
                map.put("hospitalName",hospitalName);
                return map;
            } catch (Exception e) {
                e.printStackTrace();
                log.info("健康证查询WEB端"+e);
                return null;
            }
        }
    }

    /**
     * 健康证查询小程序接口
     * @param idCard
     */
    @Override
    public Map<String, Object> getXCX(String idCard) throws Exception {
        Map<String,Object> map2 = new HashMap<>();
        Map<String, String> map1 = hearthCardInfoMapper.selectByIdCardNum2(idCard);
        if (map1 != null){
            String medical =  map1.get("medical");
            String hospital_id = map1.get("hospital_id");
            HospitalInfo hospitalInfo = hospitalInfoMapper.selectByHospitalNum(hospital_id);
            String hospitalGz = hospitalInfo.getHospitalGz();       //公章
            String hospitalName = hospitalInfo.getHospitalName();   //名称
            String qrCode = Base64Util.encodeFile(map1.get("qr_code"));
            String photo = Base64Util.encodeFile(map1.get("idcard_photo"));
            String gz = Base64Util.encodeFile(hospitalGz);
            map2.put("qr_code",qrCode);
            map2.put("photo",photo);
            map2.put("gz",gz);
            map2.put("hospitalName",hospitalName);
            map2.put("name",map1.get("name"));
            map2.put("sex",map1.get("gender"));
            map2.put("age",map1.get("age"));
            map2.put("endtime",map1.get("end_time"));
            map2.put("healthNum",map1.get("health_num"));
            map2.put("healthId",map1.get("health_id"));
            map2.put("yuliu2",map1.get("yuliu2"));                  //领取状态
            map2.put("template",hospitalInfo.getYuliu1());          //健康证模板
            map2.put("hospital_id",hospital_id);
            if ("0".equals(medical)){                               //合格
                map2.put("status","0");
                map2.put("time",map1.get("update_time"));
                return map2;
            }
            if ("1".equals(medical)){                               //不合格
                map2.put("status","1");
                map2.put("time",map1.get("update_time"));
                return map2;
            }
        }
        //未检测到信息或者审核中
        return map2;
    }

    /**
     * 领取电子健康证
     * @param healthId
     */
    @Override
    @Transactional
    public boolean lingquXCX(Integer healthId) {
        HearthCardInfo hearthCardInfo = hearthCardInfoMapper.selectByPrimaryKey(healthId);
        if (hearthCardInfo == null){
            return false;
        }else{
            hearthCardInfo.setYuliu2("1");//0未领取1已领取
            int i = hearthCardInfoMapper.updateByPrimaryKey(hearthCardInfo);
            if (i>0){
                return true;
            }
            return false;
        }
    }

    /**
     * 健康证点击查看详情
     * @param healthId
     */
    @Override
    @Transactional
    public Map<String,String> xiangqing(Integer healthId) {
        Map<String,String> map = hearthCardInfoMapper.selectByHealthid(healthId);
        if (map == null){
            return null;
        }else{
            String hospital_id = map.get("hospital_id");
            HospitalInfo hospitalInfo = hospitalInfoMapper.selectByHospitalNum(hospital_id);
            String hospitalGz = hospitalInfo.getHospitalGz();   //公章
            String hospitalName = hospitalInfo.getHospitalName();//名称
            try {
                String gz = Base64Util.encodeFile(hospitalGz);
                String qrCode = Base64Util.encodeFile(map.get("qr_code"));
                String photo = Base64Util.encodeFile(map.get("idcard_photo"));
                map.put("gz",gz);
                map.put("qrCode",qrCode);
                map.put("photo",photo);
                map.put("hospitalName",hospitalName);
                return map;
            } catch (Exception e) {
                e.printStackTrace();
                log.info("健康证点击查看详情异常"+e);
                return null;
            }
        }
    }

    /**
     * 健康证打印状态列表，状态0未打印1已打印
     * @param hospitalNum
     * @param printStatus
     * @param page
     * @param limit
     */
    @Override
    @Transactional
    public List<HearthCardInfo> dayin(String hospitalNum, String printStatus, int page, int limit) {
        List<HearthCardInfo> lists = new ArrayList<>();
        Map<String,Object> data = new HashMap<>();
        data.put("printStatus",printStatus);
        data.put("hospitalNum",hospitalNum);
        data.put("page",(page-1)*limit);
        data.put("limit",limit);
        List<HearthCardInfo> list = hearthCardInfoMapper.dayin(data);
        for (int i=0;i<list.size();i++){
            HearthCardInfo hearthCardInfo = list.get(i);
            Map<String, String> map = hearthCardInfoMapper.selectByIdCardNum2(hearthCardInfo.getIdCard());
            HospitalInfo hospitalInfo = hospitalInfoMapper.selectByHospitalNum(hospitalNum);
            String hospitalGz = hospitalInfo.getHospitalGz();   //公章
            String hospitalName = hospitalInfo.getHospitalName();//名称
            try {
                String gz = Base64Util.encodeFile(hospitalGz);
                String qrCode = Base64Util.encodeFile(map.get("qr_code"));              //二维码
                String photo = Base64Util.encodeFile(map.get("idcard_photo"));          //头像
                hearthCardInfo.setQrCode(qrCode);
                hearthCardInfo.setCreateTime(hearthCardInfo.getCreateTime().split(" ")[0]);
                hearthCardInfo.setYuliu2(gz);
                hearthCardInfo.setYuliu3(photo);
                hearthCardInfo.setYuliu4(hospitalName);
                lists.add(hearthCardInfo);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("异常信息："+e);
            }
        }
        return lists;
    }

    /**
     * 健康证打印状态列表统计，状态0未打印1已打印
     * @param hospitalNum
     * @param printStatus
     */
    @Override
    public int dayinCount(String hospitalNum, String printStatus) {
        return hearthCardInfoMapper.dayinCount(hospitalNum,printStatus);
    }

    /**
     * 不合格健康证列表
     * @param hospitalNum
     * @param page
     * @param limit
     */
    @Override
    @Transactional
    public List<HearthCardInfo> healthCardNoPass(String hospitalNum, int page, int limit) {
        List<HearthCardInfo> lists = new ArrayList<>();
        Map<String,Object> data = new HashMap<>();
        data.put("hospitalNum",hospitalNum);
        data.put("page",(page-1)*limit);
        data.put("limit",limit);
        List<HearthCardInfo> list = hearthCardInfoMapper.healthCardNoPass(data);
        for (int i=0;i<list.size();i++){
            HearthCardInfo hearthCardInfo = list.get(i);
            Map<String, String> map = hearthCardInfoMapper.selectByIdCardNum2(hearthCardInfo.getIdCard());
            HospitalInfo hospitalInfo = hospitalInfoMapper.selectByHospitalNum(hospitalNum);
            String hospitalGz = hospitalInfo.getHospitalGz();   //公章
            String hospitalName = hospitalInfo.getHospitalName();//名称
            try {
                String gz = Base64Util.encodeFile(hospitalGz);
                String qrCode = Base64Util.encodeFile(map.get("qr_code"));
                String photo = Base64Util.encodeFile(map.get("idcard_photo"));
                hearthCardInfo.setQrCode(qrCode);
                hearthCardInfo.setYuliu2(gz);
                hearthCardInfo.setYuliu3(photo);
                hearthCardInfo.setYuliu4(hospitalName);
                hearthCardInfo.setCreateTime(hearthCardInfo.getCreateTime().split(" ")[0]);
                lists.add(hearthCardInfo);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("异常信息："+e);
            }
        }
        return lists;
    }

    /**
     * 不合格健康证列表统计
     * @param hospitalNum
     */
    @Override
    public int healthCardNoPassCount(String hospitalNum) {
        return hearthCardInfoMapper.healthCardNoPassCount(hospitalNum);
    }

    /**
     * 健康证多条件查询
     *
     * @param
     * @param page
     * @param limit
     */
    @Override
    @Transactional
    public List<HearthCardInfo> keywordSelect(String printStatus,String status,String name, String idCard, String dates, int page, int limit) {
        List<HearthCardInfo> lists = new ArrayList<>();
        String startTime;
        String endTime;
        if (StringUtils.isEmpty(dates)){
            startTime=null;
            endTime=null;
        }else {
            String[] split = dates.split(" - ");
            startTime=split[0];
            endTime=split[1];
        }
        List<HearthCardInfo> list = hearthCardInfoMapper.keywordSelect(printStatus,status,name,idCard,startTime, endTime,(page-1)*limit,limit);
        for (int i=0;i<list.size();i++){
            HearthCardInfo hearthCardInfo = list.get(i);
            Map<String, String> map = hearthCardInfoMapper.selectByIdCardNum2(hearthCardInfo.getIdCard());
            String hospital_id = map.get("hospital_id");
            HospitalInfo hospitalInfo = hospitalInfoMapper.selectByHospitalNum(hospital_id);
            String hospitalGz = hospitalInfo.getHospitalGz();       //公章
            String hospitalName = hospitalInfo.getHospitalName();   //名称
            try {
                String gz = Base64Util.encodeFile(hospitalGz);
                String qrCode = Base64Util.encodeFile(map.get("qr_code"));
                String photo = Base64Util.encodeFile(map.get("idcard_photo"));
                hearthCardInfo.setQrCode(qrCode);
                hearthCardInfo.setYuliu2(gz);
                hearthCardInfo.setYuliu3(photo);
                hearthCardInfo.setYuliu4(hospitalName);
                lists.add(hearthCardInfo);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("异常信息："+e);
            }
        }
        return lists;
    }


    /**
     * 修改打印状态
     * @param healthId
     * @return
     */
    @Override
    public boolean xuanzheDY(String healthId) {
        HearthCardInfo hearthCardInfo = hearthCardInfoMapper.selectByPrimaryKey(Integer.valueOf(healthId));
        if (hearthCardInfo==null){
            return false;
        }
        hearthCardInfo.setPrintStatus("1");
        hearthCardInfo.setPrintTime(MyTools.getTime());
        hearthCardInfoMapper.updateByPrimaryKey(hearthCardInfo);
        return false;
    }

    /**
     * 批量打印，修改打印状态
     * @param healthId
     */
    @Override
    public boolean batchUpdateData(String healthId) {
        List<String> list = new ArrayList<>();
        String[] split = healthId.split(",");
        for (int i=0;i<split.length;i++){
            list.add(split[i]);
        }
        try {
            hearthCardInfoMapper.batchUpdateData(list);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("批量打印，修改打印状态异常"+e);
            return false;
        }
    }

    /**
     * 当日发证人数(健康证)
     * @param hospitalNum
     */
    @Override
    public int getTodayData(String hospitalNum) {
        return hearthCardInfoMapper.getTodayData(hospitalNum);
    }

    /**
     * 打印获取健康证
     * @param idCardNum
     */
    @Override
    public HearthCardInfo selectByIdCardNum2(String idCardNum) {
        HearthCardInfo hearthCardInfo = hearthCardInfoMapper.selectByIdCardNum(idCardNum);
        if (hearthCardInfo != null){
            HospitalInfo hospitalInfo = hospitalInfoMapper.selectByHospitalNum(hearthCardInfo.getHospitalId());
            TiJianInfo tiJianInfo = tiJianInfoMapper.selectByIdCardNum(hearthCardInfo.getIdCard());
            try {
                String gz = Base64Util.encodeFile(hospitalInfo.getHospitalGz());
                String idCardPhoto = Base64Util.encodeFile(tiJianInfo.getIdcardPhoto());
                String qrCode = Base64Util.encodeFile(hearthCardInfo.getQrCode());
                hearthCardInfo.setQrCode(qrCode);
                hearthCardInfo.setYuliu2(gz);
                hearthCardInfo.setYuliu3(idCardPhoto);
                return hearthCardInfo;
            } catch (Exception e) {
                e.printStackTrace();
                log.info("获取健康证信息异常"+e);
                return null;
            }
        }
        return null;
    }

    /**
     * 多条件查询统计
     * @param name
     * @param idCard
     * @param dates
     */
    @Override
    public int keywordSelectCount(String printStatus,String status,String name, String idCard, String dates) {
        String startTime;
        String endTime;
        if (StringUtils.isEmpty(dates)){
            startTime=null;
            endTime=null;
        }else {
            String[] split = dates.split(" - ");
            startTime=split[0];
            endTime=split[1];
        }
        return hearthCardInfoMapper.keywordSelectCount(printStatus,status,name,idCard,startTime,endTime);
    }


}
