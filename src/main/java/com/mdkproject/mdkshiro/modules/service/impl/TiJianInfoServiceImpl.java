package com.mdkproject.mdkshiro.modules.service.impl;

import com.mdkproject.mdkshiro.dao.AreaMapper;
import com.mdkproject.mdkshiro.dao.HearthCardInfoMapper;
import com.mdkproject.mdkshiro.dao.HospitalInfoMapper;
import com.mdkproject.mdkshiro.dao.TiJianInfoMapper;
import com.mdkproject.mdkshiro.entity.*;
import com.mdkproject.mdkshiro.error.BusinessException;
import com.mdkproject.mdkshiro.error.EmBusinessError;
import com.mdkproject.mdkshiro.modules.service.TiJianInfoService;
import com.mdkproject.mdkshiro.modules.service.model.TjshModel;
import com.mdkproject.mdkshiro.modules.util.Base64Util;
import com.mdkproject.mdkshiro.modules.util.DateToStrUtil;
import com.mdkproject.mdkshiro.modules.util.QRcodeUtils;
import com.mdkproject.mdkshiro.utils.MyTools;
import com.mdkproject.mdkshiro.validator.ValidationResult;
import com.mdkproject.mdkshiro.validator.ValidatorImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

import static com.mdkproject.mdkshiro.utils.MyTools.hideAllIdCardNum;

/**
 * @program: mdk-shiro->TiJianInfoServiceImpl
 * @description: 体检信息管理
 * @author: lizhen
 * @create: 2019-07-15 16:05
 **/
@Service
@Slf4j
public class TiJianInfoServiceImpl implements TiJianInfoService {

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private TiJianInfoMapper tiJianInfoMapper;
    @Autowired
    private HearthCardInfoMapper hearthCardInfoMapper;
    @Autowired
    private HospitalInfoMapper hospitalInfoMapper;
    @Autowired
    private AreaMapper areaMapper;



    /**
     * 判断是否满11个月
     * @param idcardNum
     */
    @Override
    public String selectByIdCardNum(String idcardNum) {
        try {
            TiJianInfo tiJianInfo = tiJianInfoMapper.selectByIdCardNum(idcardNum);
            if (tiJianInfo!=null){
                String startdate = tiJianInfo.getStartdate();           //第一次办证日期
                String nowDate = DateToStrUtil.getStringDateShort();    //当前日期
                Long aLong = DateToStrUtil.between_days(startdate, nowDate);
                if (aLong<365){
                    return startdate;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 体检信息快速录入,健康证
     *
     * @param tiJianInfo
     */
    @Override
    @Transactional
    public HearthCardInfo quickInput(TiJianInfo tiJianInfo) throws BusinessException {
        //入参校验
        ValidationResult result = validator.validate(tiJianInfo);
        if (result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
        try {
            String idcardPhoto = tiJianInfo.getIdcardPhoto();                           //照片
            String path = "C:/img/idcardPhoto/" + tiJianInfo.getIdcardNum() + ".jpg";   //身份证照片路径
            Base64Util.GenerateImage(idcardPhoto,path);                                 //写入文件
            tiJianInfo.setIdcardPhoto(path);
            tiJianInfo.setCreateTime(MyTools.getTime());
            tiJianInfo.setUpdateTime(MyTools.getTime());
            tiJianInfo.setYuliu2(DateToStrUtil.getStringDateShort());  //信息录入日期
            tiJianInfo.setStatus("1");                                 //默认是
            tiJianInfo.setYuliu1("1");                                 //体检项目标识
            tiJianInfoMapper.insert(tiJianInfo);

            //健康证
            HearthCardInfo hearthCardInfo = this.convertHearthCardInfoFromtiJianInfo(tiJianInfo);
            hearthCardInfo.setMedical("0");                            //快速默认合格
            hearthCardInfoMapper.insert(hearthCardInfo);
            HearthCardInfo CardInfo = this.getHealthCardInfo(hearthCardInfo.getHealthId());
            return CardInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("录入信息错误"+e);
            return null;
        }
    }

    /**
     * 体检信息基础录入
     *
     * @param tiJianInfo
     */
    @Override
    @Transactional
    public TiJianInfo slowInput(TiJianInfo tiJianInfo) throws BusinessException {
        //入参校验
        ValidationResult result = validator.validate(tiJianInfo);
        if (result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
        try {
            String idcardPhoto = tiJianInfo.getIdcardPhoto();                           //照片
            String path = "C:/img/idcardPhoto/" + tiJianInfo.getIdcardNum() + ".jpg";   //身份证照片路径
            Base64Util.GenerateImage(idcardPhoto,path);                                 //写入文件
            tiJianInfo.setIdcardPhoto(path);
            tiJianInfo.setCreateTime(MyTools.getTime());
            tiJianInfo.setUpdateTime(MyTools.getTime());
            tiJianInfo.setYuliu2(DateToStrUtil.getStringDateShort());  //信息录入日期
            tiJianInfo.setStatus("0");                                 //默认
            tiJianInfo.setYuliu1("0");                                 //没有体检项目
            tiJianInfoMapper.insert(tiJianInfo);
            Integer tjId = tiJianInfo.getTjId();
            System.out.println(tjId);
            TiJianInfo table = this.getTable(tjId);
            return table;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("基础录入异常"+e);
            return null;
        }
    }



    /**
     * 体检单条审核
     * @param tjshModel
     */
    @Override
    @Transactional
    public TiJianInfo oneTJSH(TjshModel tjshModel) {
        try {
            int i= tiJianInfoMapper.OneupdateTJstatus(tjshModel.getTjId(),tjshModel.getStatus(),tjshModel.getStartdate(),tjshModel.getEnddate(),tjshModel.getWhy());
            if (i>0){
                TiJianInfo tiJianInfo = tiJianInfoMapper.selectByPrimaryKey(Integer.valueOf(tjshModel.getTjId()));
                HearthCardInfo hearthCardInfo = hearthCardInfoMapper.selectByIdCardNum(tiJianInfo.getIdcardNum());
                if (hearthCardInfo==null){
                    HearthCardInfo hearthCardInfo1 = this.convertHearthCardInfoFromtiJianInfo(tiJianInfo);
                    hearthCardInfoMapper.insert(hearthCardInfo1);               //新增
                }else{
                    if ("1".equals(tjshModel.getStatus())){
                        hearthCardInfo.setMedical("0");                         //合格
                    }else if ("2".equals(tjshModel.getStatus())){
                        hearthCardInfo.setMedical("1");                         //不合格
                    }
                    hearthCardInfo.setUpdateTime(MyTools.getTime());            //更新时间
                    hearthCardInfoMapper.updateByPrimaryKey(hearthCardInfo);    //更新
                }
                return tiJianInfo;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 体检信息批量审核
     * @param tjshModel
     */
    @Override
    @Transactional
    public boolean batchUpTJdate(TjshModel tjshModel) {
        try {
            //入参校验
            ValidationResult result = validator.validate(tjshModel);
            if (result.isHasErrors()){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
            }
            List<String> list = new ArrayList<>();
            String[] split = tjshModel.getTjId().split(",");
            for (int i=0;i<split.length;i++){
                list.add(split[i]);
            }
            tiJianInfoMapper.batchUpTJdate(list,tjshModel.getStatus(),tjshModel.getStartdate(),tjshModel.getEnddate(),tjshModel.getWhy());
            //健康证
            List<HearthCardInfo> hearthCardInfoList = new ArrayList<>();
            for (int i=0;i<list.size();i++){
                TiJianInfo tiJianInfo = tiJianInfoMapper.selectByPrimaryKey(Integer.valueOf(list.get(i)));
                HearthCardInfo hearthCardInfo = this.convertHearthCardInfoFromtiJianInfo(tiJianInfo);
                hearthCardInfoList.add(hearthCardInfo);
            }
            hearthCardInfoMapper.batchInsertData(hearthCardInfoList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("体检信息批量审核异常"+e);
            return false;
        }
    }

    /**
     * 安卓端体检信息录入
     * @param tiJianInfo
     */
    @Override
    @Transactional
    public boolean AndroidInput(TiJianInfo tiJianInfo) {
        //体检编号&健康证编号
        int num = (int) (( Math.random()*9+1)*100000);
        String code1 = String.valueOf(num);
        int num1 = (int) (( Math.random()*9+1)*100000);
        String code2 = String.valueOf(num1);
        String year = DateToStrUtil.getYear();            //年

        HospitalInfo hospitalInfo = hospitalInfoMapper.selectByHospitalNum(tiJianInfo.getDeptNum());
        Area area = areaMapper.selectByAreaNum(hospitalInfo.getAreaNum());
        try {
            String path = "C:/img/idcardPhoto/" + tiJianInfo.getIdcardNum() + ".jpg";   //身份证照片路径
            tiJianInfo.setNumber("T"+year+code1);                           //体检编号
            tiJianInfo.setHearthcardNum(area.getYuliu1()+year+code2);       //健康证编号
            tiJianInfo.setYuliu1("0");                                      //打印状态
            tiJianInfo.setYuliu2(DateToStrUtil.getStringDateShort());          //信息录入日期
            tiJianInfo.setYuliu3(hospitalInfo.getHospitalName());               //医院名称
            tiJianInfo.setStatus("0");                                      //审核状态默认0未审核
            Base64Util.GenerateImage(tiJianInfo.getIdcardPhoto(),path);
            tiJianInfo.setIdcardPhoto(path);                                //照片路径
            tiJianInfo.setCreateTime(MyTools.getTime());
            tiJianInfo.setUpdateTime(MyTools.getTime());
            tiJianInfo.setStartdate(DateToStrUtil.getStringDateShort());       //有效日
            tiJianInfo.setEnddate(DateToStrUtil.getOneYearData());             //失效期
            tiJianInfoMapper.insert(tiJianInfo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("安卓端体检信息录入异常"+e);
            return false;
        }
    }

    /**
     * 获取体检当月体检数据
     * @param hospitalNum
     */
    @Override
    public int getMonthData(String hospitalNum) {
        return tiJianInfoMapper.getMonthData(hospitalNum);
    }

    /**
     * 获取体检最近一周数据
     *
     * @param hospitalNum
     * @param status 0未审核1已审核
     * @param page
     * @param limit
     */
    @Override
    public List<TiJianInfo> getWeekData(String hospitalNum, String status, int page, int limit) throws Exception {
        List<TiJianInfo> lists = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        data.put("page",(page-1)*limit);
        data.put("limit",limit);
        data.put("hospitalNum",hospitalNum);
        data.put("status",status);
        List<TiJianInfo> list = tiJianInfoMapper.getWeekData(data);
        for (int i=0;i<list.size();i++){
            TiJianInfo tiJianInfo = list.get(i);
            String idcardPhoto = Base64Util.encodeFile(tiJianInfo.getIdcardPhoto());
            tiJianInfo.setIdcardPhoto(idcardPhoto);
            HospitalInfo hospitalInfo = hospitalInfoMapper.selectByHospitalNum(hospitalNum);
            Area area = areaMapper.selectByAreaNum(hospitalInfo.getAreaNum());
            tiJianInfo.setYuliu4(area.getAreaCity()+area.getAreaDistrict());
            lists.add(tiJianInfo);
        }
        return lists;
    }

    /**
     * 获取体检最近一周数据统计
     *
     * @param hospitalNum
     * @param status
     */
    @Override
    public int getWeekDataCount(String hospitalNum, String status) {
        return tiJianInfoMapper.getWeekDataCount(hospitalNum,status);
    }

    /**
     * 查询当日体检人数
     * @param hospitalNum
     */
    @Override
    public int getTodayData(String hospitalNum) {
        return tiJianInfoMapper.getTodayData(hospitalNum);
    }

    /**
     * 查询一家医院体检总人数
     * @param hospitalNum
     */
    @Override
    public int getAllByHospitalNum(String hospitalNum) {
        return tiJianInfoMapper.getAllByHospitalNum(hospitalNum);
    }

    /**
     * 体检审核状态列表
     * @param hospitalNum
     * @param status 0未审核1合格2不合格
     * @param page
     * @param limit
     */
    @Override
    public List<TiJianInfo> getStatusList(String hospitalNum, String status, int page, int limit) {
        Map<String,Object> data = new HashMap<>();
        data.put("status",status);
        data.put("hospitalNum",hospitalNum);
        data.put("page",(page-1)*limit);
        data.put("limit",limit);
        return tiJianInfoMapper.getStatusList(data);
    }

    /**
     * 体检审核状态列表统计
     * @param hospitalNum
     * @param status
     */
    @Override
    public int getStatusListCount(String hospitalNum, String status) {
        return tiJianInfoMapper.getStatusListCount(hospitalNum,status);
    }

    /**
     * 体检信息审核多条件查询
     */
    @Override
    public List<TiJianInfo> keywordSelect(String name,String number,String idCardNum,String dates) {
        String startTime;
        String endTime;
        if (StringUtils.isEmpty(dates)){
            startTime  = null;
            endTime = null;
        }else{
            String[] split = dates.split(" - ");
            startTime  = split[0];
            endTime = split[1];
        }
        Map<String,Object> data = new HashMap<>();
        data.put("name",name);
        data.put("idcardNum",idCardNum);
        data.put("number",number);
        data.put("startTime",startTime);
        data.put("endTime",endTime);
        return tiJianInfoMapper.keywordSelect(data);
    }

    /**
     * 当日体检&发证统计,查询一周数据
     * @param dateToWeek
     * @param hospitalNum
     */
    @Override
    public Map<String, Object> tjAddfzWeekData(List<String> dateToWeek, String hospitalNum) {
        Map<String,Object> map = new HashedMap();
        Map<String,Integer> map1 = new TreeMap<>();
        Map<String,Integer> map2 = new TreeMap<>();
        for (int i=0;i<dateToWeek.size();i++){
            String oneDay = dateToWeek.get(i);
            int i1 = hearthCardInfoMapper.tjAddfzWeekData(oneDay,hospitalNum);
            int i2 = tiJianInfoMapper.tjAddfzWeekData(oneDay,hospitalNum);
            map1.put(oneDay,i1);    //发证
            map2.put(oneDay,i2);    //体检
        }
        map.put("card",map1);
        map.put("tj",map2);
        return map;
    }

    /**
     * 表
     * @param tjId
     */
    @Override
    public TiJianInfo getTable(Integer tjId){
        TiJianInfo tiJianInfo = tiJianInfoMapper.selectByPrimaryKey(tjId);
        String hospitalNum = tiJianInfo.getDeptNum();
        HospitalInfo hospitalInfo = hospitalInfoMapper.selectByHospitalNum(hospitalNum);
        Area area = areaMapper.selectByAreaNum(hospitalInfo.getAreaNum());
        if (tiJianInfo == null){
            return null;
        }
        try {
            String idCardPhoto = Base64Util.encodeFile(tiJianInfo.getIdcardPhoto());
            tiJianInfo.setYuliu4(area.getAreaCity()+area.getAreaDistrict());
            tiJianInfo.setIdcardPhoto(idCardPhoto);
            return tiJianInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("获取体检表信息异常"+e);
            return null;
        }

    }

    /**
     * 保存&打印健康证
     * @param healthId
     */
    @Override
    public HearthCardInfo getHealthCardInfo(Integer healthId) {
        HearthCardInfo hearthCardInfo = hearthCardInfoMapper.selectByPrimaryKey(healthId);
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
                hearthCardInfo.setYuliu4(hospitalInfo.getHospitalName());
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
     * web端补录
     * @param tiJianInfo
     */
    @Override
    public boolean puluInfo(TiJianInfo tiJianInfo) {
        try {
            String idcardPhoto = tiJianInfo.getIdcardPhoto();                           //照片
            String path = "C:/img/idcardPhoto/" + tiJianInfo.getIdcardNum() + ".jpg";   //身份证照片路径
            Base64Util.GenerateImage(idcardPhoto,path);                                 //写入文件
            tiJianInfo.setIdcardPhoto(path);
            tiJianInfo.setUpdateTime(MyTools.getTime());
            tiJianInfoMapper.updateByKey(tiJianInfo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("web端补录异常"+e);
            return false;
        }
    }

    /**
     * excel导入导出
     * @param successList
     */
    @Override
    public boolean importExcel(List<ExcelModel> successList) {
        try {
            tiJianInfoMapper.batchInsert(successList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取体检机构最新一条数据
     * @param hospitalNum
     */
    @Override
    public String getNewData(String hospitalNum) {
        String number = tiJianInfoMapper.getNewData(hospitalNum);
        return number;
    }


    //模型转化
    private HearthCardInfo convertHearthCardInfoFromtiJianInfo(TiJianInfo tiJianInfo){
        HearthCardInfo hearthCardInfo = new HearthCardInfo();
        QRcodeUtils qRcodeUtils = new QRcodeUtils();
//        BeanUtils.copyProperties(hearthCardInfo,tiJianInfo);
        String status = tiJianInfo.getStatus();
        hearthCardInfo.setName(tiJianInfo.getName());
        hearthCardInfo.setAge(tiJianInfo.getAge());
        hearthCardInfo.setGender(tiJianInfo.getSex());
        if ("1".equals(status)){
            hearthCardInfo.setMedical("0");                         //合格
        }else if ("2".equals(status)){
            hearthCardInfo.setMedical("1");                         //不合格
        }
        hearthCardInfo.setIdCard(tiJianInfo.getIdcardNum());
        hearthCardInfo.setHealthNum(tiJianInfo.getHearthcardNum());     //健康证编号
        hearthCardInfo.setStartTime(tiJianInfo.getStartdate());         //发证日期
        hearthCardInfo.setEndTime(tiJianInfo.getEnddate());             //健康证有效期
        //生成二维码
        BufferedImage qrCodeImg = qRcodeUtils.createQRCode("健康证明，" + tiJianInfo.getHearthcardNum()+"，"+ tiJianInfo.getName() +"，"+ hideAllIdCardNum(tiJianInfo.getIdcardNum()) +"，"+ tiJianInfo.getEnddate() +"，"+ "玛迪卡云");
        //写入文件，远程二维码路径
        String path = "C:/img/qrcodeImg/"+MyTools.getDateR()+".png";
        qRcodeUtils.writeToFile(qrCodeImg,new File(path));
        hearthCardInfo.setQrCode(path);                                 //二维码路径
        hearthCardInfo.setHospitalId(tiJianInfo.getDeptNum());          //体检机构医院代码
        hearthCardInfo.setPrintStatus("0");                         //默认打印状态1
        hearthCardInfo.setYuliu1(DateToStrUtil.getStringDateShort());  //体检信息录入日期
        hearthCardInfo.setCreateTime(MyTools.getTime());
        hearthCardInfo.setUpdateTime(MyTools.getTime());
        hearthCardInfo.setYuliu2("0");                              //电子健康证领取状态0未领取默认
        return hearthCardInfo;
    }
}
