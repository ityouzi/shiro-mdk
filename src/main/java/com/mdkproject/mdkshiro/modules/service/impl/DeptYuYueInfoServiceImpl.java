package com.mdkproject.mdkshiro.modules.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.mdkproject.mdkshiro.dao.DeptYuYueInfoMapper;
import com.mdkproject.mdkshiro.dao.HospitalInfoMapper;
import com.mdkproject.mdkshiro.entity.DeptYuYueInfo;
import com.mdkproject.mdkshiro.entity.HospitalInfo;
import com.mdkproject.mdkshiro.error.BusinessException;
import com.mdkproject.mdkshiro.error.EmBusinessError;
import com.mdkproject.mdkshiro.modules.service.DeptYuYueInfoService;
import com.mdkproject.mdkshiro.modules.util.Base64Util;
import com.mdkproject.mdkshiro.modules.util.DateToStrUtil;
import com.mdkproject.mdkshiro.utils.DateUtil;
import com.mdkproject.mdkshiro.utils.MyTools;
import com.mdkproject.mdkshiro.validator.ValidationResult;
import com.mdkproject.mdkshiro.validator.ValidatorImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: mdk-shiro->DeptYuYueInfoServiceImpl
 * @description: 单位预约模块
 * @author: lizhen
 * @create: 2019-07-12 12:12
 **/
@Slf4j
@Service
public class DeptYuYueInfoServiceImpl implements DeptYuYueInfoService {

    @Autowired
    private DeptYuYueInfoMapper deptYuYueInfoMapper;
    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private HospitalInfoMapper hospitalInfoMapper;

    /**
     * 单位预约列表
     * */
    @Override
    public List<DeptYuYueInfo> deptList(String hospitalNum, String deptShenhe,int page, int limit) {
        Map<String,Object> data = new HashMap<>();
        data.put("page",(page-1)*limit);
        data.put("limit",limit);
        data.put("hospitalNum",hospitalNum);
        data.put("deptShenhe",deptShenhe);
        List<DeptYuYueInfo>list = deptYuYueInfoMapper.selectAll(data);
        return list;
    }

    /**
     * 单位预约审核状态统计
     * */
    @Override
    public int deptCountBYhospitalNum(String hospitalNum, String deptShenhe) {
        int i = deptYuYueInfoMapper.deptCountBYhospitalNum(hospitalNum,deptShenhe);
        return i;
    }

    /**
     * 待审核总数
     * */
    @Override
    public int dshCount(String hospitalNum) {
        String status="0";
        int i = deptYuYueInfoMapper.dshCount(hospitalNum,status);
        return i;
    }

    /**
     * 已审核总数
     * */
    @Override
    public int yshCount(String hospitalNum) {
        String status1 = "1";
        String status2 = "2";
        int i = deptYuYueInfoMapper.yshCount(hospitalNum,status1,status2);
        return i;
    }

    /**
     * 单位预约资质审核
     * */
    @Override
    public boolean deptSH(String deptId,String deptShenhe,String deptPhone,String time,String why) {
        try {
            String updateTime = MyTools.getTime();              //更新时间
            deptYuYueInfoMapper.updateByDeptId(deptId,deptShenhe,time,why,updateTime);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 单位体检预约
     * */
    @Override
    public Map<String,String> deptOrder(DeptYuYueInfo deptYuYueInfo) {
        Map<String,String> map = new HashMap<>();
        DeptYuYueInfo info = deptYuYueInfoMapper.selectBYcompanyCode(deptYuYueInfo.getDeptCode());
        if (info!=null){
            String deptShenhe = deptYuYueInfo.getDeptShenhe();
            if ("0".equals(deptShenhe)){
                map.put("status","0");      //未审核
                map.put("time",deptYuYueInfo.getCreateTime());
                return map;
            }else if ("1".equals(deptShenhe)){
                map.put("status","1");      //审核通过
                map.put("time",deptYuYueInfo.getDeptTime());
                return map;
            }else{
                map.put("status","2");      //审核不通过
                map.put("time",deptYuYueInfo.getDeptYuanyin());
                return map;
            }
        }else{
            try {
                StringBuilder strs = new StringBuilder();
                String path = "C:/img/company/";
                JSONArray array = JSONArray.parseArray(deptYuYueInfo.getDeptImgurl());
                for (int i=0;i<array.size();i++){
                    String r = MyTools.getDateR()+".jpg";
                    String base64img = array.getString(i);
                    Base64Util.GenerateImage(base64img,path+r);
                    strs.append(r).append("+");
                }
                deptYuYueInfo.setDeptImgurl(strs.toString());
                deptYuYueInfo.setDeptShenhe("0");                   //默认未审核
                deptYuYueInfo.setCreateTime(MyTools.getTime());     //预约时间
                deptYuYueInfo.setUpdateTime(MyTools.getTime());
                deptYuYueInfoMapper.insert(deptYuYueInfo);
                map.put("status","200");    //预约成功
                return map;
            }catch (Exception e){
                e.printStackTrace();
                map.put("status","100");     //预约失败
                return map;
            }

        }
    }


    /**
     * 查看单位预约资质信息
     * */
    @Override
    public List<String> deptInfoLook(Integer deptId) throws Exception {
        List<String> list = new ArrayList<>();
        DeptYuYueInfo info = deptYuYueInfoMapper.selectByPrimaryKey(deptId);
        if (info==null){
            return null;
        }else{
            String path = "C:/img/company/";
            String[] split = info.getDeptImgurl().split("\\+");
            for (int i=0;i<split.length;i++){
                String imgs = Base64Util.encodeFile(path + split[i]);
                list.add(imgs);
            }
            return list;
        }
    }

    /**
     * 多条件查询
     * @param hospitalNum
     * @param deptYuYueInfo
     */
    @Override
    public List<DeptYuYueInfo> keywordSelect(String hospitalNum, DeptYuYueInfo deptYuYueInfo) throws BusinessException {
        ValidationResult result = validator.validate(deptYuYueInfo);
        if (result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
        List<DeptYuYueInfo> list = deptYuYueInfoMapper.keywordSelect(deptYuYueInfo,hospitalNum);
        return list;
    }

    /**
     * 当日预约单位数
     * @param hospitalNum
     */
    @Override
    public int getTodayData(String hospitalNum) {
        return deptYuYueInfoMapper.getTodayData(hospitalNum);
    }

    /**
     * 获取一周每一天数据
     * @param oneDay
     * @param hospitalNum
     */
    @Override
    public int getOneData(String oneDay, String hospitalNum) {
        int oneData = deptYuYueInfoMapper.getOneData(oneDay,hospitalNum);
        if (oneData == 0){
            return 0;
        }
        return oneData;
    }

    /**
     * 判断是否是第一次预约，如果不是依据征信代码，判断审核状态
     * @param deptOrganization
     */
    @Override
    public Map<String, Object> selectByCompanyCode(String deptOrganization) {
        DeptYuYueInfo deptYuYueInfo = deptYuYueInfoMapper.selectByCompanyCode(deptOrganization);
        Map<String,Object> map = new HashMap<>();
        if (deptYuYueInfo != null){
            HospitalInfo hospitalInfo = hospitalInfoMapper.selectByHospitalNum(deptYuYueInfo.getDeptTjcode());
            String deptShenhe = deptYuYueInfo.getDeptShenhe();
            if ("0".equals(deptShenhe)){
                map.put("status","0");
                map.put("time",deptYuYueInfo.getCreateTime());  //提交资质时间
                return map;
            }
            if ("1".equals(deptShenhe)) {
                map.put("status", "1");
                map.put("shTime",deptYuYueInfo.getUpdateTime());        //审核时间
                map.put("time", deptYuYueInfo.getDeptTime());           //医院通知体检时间
                map.put("hospitalNum",hospitalInfo.getHospitalName());  //医院名称
                return map;
            }
        }
        return map;
    }

    /**
     * 单位体检预约判断，最近一次
     * @param deptCode
     */
    @Override
    public DeptYuYueInfo getZuiJinOneOrder(String deptCode) {
        DeptYuYueInfo deptYuYueInfo = deptYuYueInfoMapper.getZuiJinOneOrder(deptCode);
        if (deptYuYueInfo == null){
            return null;
        }

        return deptYuYueInfo;
    }

    /**
     * 预约录入
     * @param deptYuYueInfo
     */
    @Override
    public Map<String,Object> InsertDeptInfo(DeptYuYueInfo deptYuYueInfo,String apm) {
        Map<String,Object> map = new HashMap<>();
        try {
            StringBuilder strs = new StringBuilder();
            String path = "C:/img/company/";
            JSONArray array = JSONArray.parseArray(deptYuYueInfo.getDeptImgurl());
            for (int i=0;i<array.size();i++){
                String r = MyTools.getDateR()+".jpg";
                String base64img = array.getString(i);
                Base64Util.GenerateImage(base64img,path+r);
                strs.append(r).append("+");
            }
            deptYuYueInfo.setDeptImgurl(strs.toString());       //图片
            deptYuYueInfo.setDeptShenhe("0");                   //默认未审核
            deptYuYueInfo.setTjstatus("0");                     //默认未来体检
            deptYuYueInfo.setCreateTime(MyTools.getTime());     //创建时间
            deptYuYueInfo.setUpdateTime(MyTools.getTime());     //更新时间
            //预约人数判断
            Integer deptPeoplenum = deptYuYueInfo.getDeptPeoplenum();
            Map<String, Object> map1 = companyOrder(deptYuYueInfo.getDeptTjcode(), deptYuYueInfo.getDeptTime());
            int am = (int) map1.get("am");                  //上午可预约人数
            int pm = (int) map1.get("pm");                  //下午可预约人数
            if (StringUtils.isEmpty(apm)){
                map.put("status","3");
                return map;
            }

            if ("am".equals(apm)){                          //上午
                if (deptPeoplenum-am>0){
                    map.put("status","2");
                    return map;
                }else{

                }
            }
            if ("pm".equals(apm)){                          //下午
                if (deptPeoplenum-pm>0){
                    map.put("status","2");                  //预约人数超过剩余人数
                    return map;
                }
            }

            deptYuYueInfoMapper.insert(deptYuYueInfo);
            map.put("status","1");                          //成功
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status","500");
            log.info("单位预约异常"+e);
            return map;
        }
    }

    /**
     * 判断失信次数,状态为0
     * @param deptCode
     */
    @Override
    public int yuyueCount(String deptCode) {
        return deptYuYueInfoMapper.yuyueCount(deptCode);
    }

    /**
     * 预约未体检&预约已体检列表
     * @param tjStatus
     * @param hospitalNum
     * @param page
     * @param limit
     */
    @Override
    public List<DeptYuYueInfo> tjStatusList(String tjStatus, String hospitalNum, int page, int limit) {
        Map<String,Object> data = new HashMap<>();
        data.put("tjStatus",tjStatus);
        data.put("hospitalNum",hospitalNum);
        data.put("page",(page-1)*limit);
        data.put("limit",limit);
        return deptYuYueInfoMapper.tjStatusList(data);
    }

    /**
     * 预约未体检&预约已体检统计
     * @param tjStatus
     * @param hospitalNum
     */
    @Override
    public int tjStatusListCount(String tjStatus, String hospitalNum) {
        return deptYuYueInfoMapper.tjStatusListCount(tjStatus,hospitalNum);
    }

    /**
     * 预约未体检&预约已体检确认时间
     * @param deptId
     */
    @Override
    public int tjStatusUpdate(Integer deptId,String tjStatus) {
        try {
            String time=MyTools.getTime();
            DeptYuYueInfo deptYuYueInfo = deptYuYueInfoMapper.selectByPrimaryKey(deptId);
            deptYuYueInfo.setUpdateTime(time);
            deptYuYueInfo.setTjstatus(tjStatus);    //0未体检1已体检
            String deptTime = deptYuYueInfo.getDeptTime();              //体检时间
            String nowTime = DateToStrUtil.getStringDateShort();        //当前时间
            if (DateToStrUtil.bijiaoDate2(deptTime,nowTime)){
                deptYuYueInfoMapper.updateByPrimaryKey(deptYuYueInfo);
                return 0;                                               //true
            }
            return 1;                                                   //false
        } catch (Exception e) {
            e.printStackTrace();
            log.info("已体检确认异常"+e);
            return 2;
        }
    }

    /**
     * 修改体检时间
     * @param deptId
     * @param time
     */
    @Override
    public boolean updateTJtime(Integer deptId, String time) {
        try {
            deptYuYueInfoMapper.updateTJtime(deptId,time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 单位体检时间预约获取该是否可以继续预约
     * @param hospitalNum
     * @param time
     */
    @Override
    public Map<String, Object> companyOrder(String hospitalNum,String time) {
        Map<String, Object> map = new HashMap<>();
        HospitalInfo hospitalInfo = hospitalInfoMapper.selectByHospitalNum(hospitalNum);
        int a;
        int b;
        int cc;
        if (hospitalInfo.getYuliu2()==null){
            a = 100;
            b = 100;
        }else{
            cc = Integer.valueOf(hospitalInfo.getYuliu2());                  //医院规定人数
            int[] ints = DateUtil.averageANum(cc, 2);
            a=ints[0];
            b=ints[1];
        }
        List<Map<String, Object>> maps = deptYuYueInfoMapper.companyOrder(hospitalNum, time);         //剩余可预约人数
        if (maps.size()==0){
            map.put("am",a);
            map.put("pm",b);
        }else{
            Map<String, Object> map1 = maps.get(0);
            Map<String, Object> map2 = maps.get(1);
            Object total_num1 = map1.get("total_num");
            Object total_num2 = map2.get("total_num");
            int i1 = Integer.parseInt(String.valueOf(total_num1));
            int i2 = Integer.parseInt(String.valueOf(total_num2));
            //上午
            int aa = 0;
            int bb = 0;
            if (a-i1>0){
                aa = a-i1;                                      //剩余可预约人数
                map.put("am",aa);
            }else{
                map.put("am",aa);
            }

            //下午
            if (b-i2>0){
                bb = b - i2;
                map.put("pm",bb);
            }else{
                map.put("pm",bb);
            }
        }
        return map;
    }

    /**
     * 监督机关对预约资质审核
     * @param deptId
     * @param status
     */
    @Override
    public boolean zzsh(Integer deptId, String status) {
        return false;
    }


}
