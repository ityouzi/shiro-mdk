package com.mdkproject.mdkshiro.modules.controller;

import com.mdkproject.mdkshiro.base.aop.NoRepeatSumit;
import com.mdkproject.mdkshiro.controller.BaseController;
import com.mdkproject.mdkshiro.entity.DeptYuYueInfo;
import com.mdkproject.mdkshiro.entity.HospitalInfo;
import com.mdkproject.mdkshiro.entity.UserInfo;
import com.mdkproject.mdkshiro.error.BusinessException;
import com.mdkproject.mdkshiro.error.EmBusinessError;
import com.mdkproject.mdkshiro.modules.service.DeptYuYueInfoService;
import com.mdkproject.mdkshiro.modules.service.HospitalService;
import com.mdkproject.mdkshiro.modules.util.DateToStrUtil;
import com.mdkproject.mdkshiro.response.CommonReturnType;
import com.mdkproject.mdkshiro.utils.MyTools;
import com.mdkproject.mdkshiro.utils.SendMsgUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: mdk-shiro->DeptYuYueInfoController
 * @description: 单位预约模块
 * @author: lizhen
 * @create: 2019-07-12 12:11
 **/
@RestController
@RequestMapping("deptorder")
public class DeptYuYueInfoController extends BaseController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private DeptYuYueInfoService deptYuYueInfoService;
    @Autowired
    private HospitalService hospitalService;



    /**
     * @auther: lizhen
     * @date: 2019/7/25 17:17
     * 功能描述: 预约之前验证
     */
    @RequestMapping(value = "deptOrder1")
    public CommonReturnType deptOrder1(String deptName,String deptCode, String telphone, String otpCode) throws BusinessException {
        //redis中的key是否存在
        if (!redisTemplate.hasKey(telphone)){
            throw new BusinessException(EmBusinessError.CODE_ERROR);
        }
        //获取redis缓存中的手机号key和验证码value
        String code = redisTemplate.opsForValue().get(telphone);
        if (code==null || "".equals(code)){
            throw new BusinessException(EmBusinessError.CODE_LOSE);
        }

        //验证手机号和对应的验证码不相符合
        System.out.println(code);
        if (!StringUtils.equals(otpCode,code)){
            throw new BusinessException(EmBusinessError.CODE_ERROR);
        }
        Map<String, Object> map = new HashMap<>();
        //2.最近预约日期判断
        DeptYuYueInfo info = deptYuYueInfoService.getZuiJinOneOrder(deptCode);  //获取最近一条数据
        if (info != null) {
            String deptShenhe = info.getDeptShenhe();                           //状态
            String createTime;
            if (info.getDeptTime() == null) {
                createTime = info.getCreateTime();                              //最近预约录入时间
            } else {
                createTime = info.getDeptTime();                                //体检时间
            }
            String nowTime = DateToStrUtil.getStringDateShort();                //当前日期
            boolean b = DateToStrUtil.bijiaoDate2(createTime, nowTime);
            if (b) {
                //3.判断预约次数
                int i = deptYuYueInfoService.yuyueCount(deptCode);
                if (i >= 3) {
                    return CommonReturnType.createCommonReturnType("预约失信次数过多", "300");
                }
            } else {
                //体检时间没过，不能重复预约
                HospitalInfo hospitalInfo = hospitalService.selectByHospitalNum(info.getDeptTjcode());
                if ("0".equals(deptShenhe)) {
                    map.put("status", "0");
                    map.put("time", info.getCreateTime());                      //提交资质时间
                }
                if ("1".equals(deptShenhe)) {
                    map.put("status", "1");
                    map.put("shTime", info.getUpdateTime());                    //审核时间
                    map.put("time", info.getDeptTime());                        //医院通知体检时间
                    map.put("hospitalNum", hospitalInfo.getHospitalName());     //医院名称
                }
                if ("2".equals(deptShenhe)) {
                    map.put("status", "2");
                    map.put("why", info.getDeptYuanyin());
                }
                return CommonReturnType.createCommonReturnType(map, "400");
            }
        }
        return CommonReturnType.createCommonReturnType("进入页面", "200");
    }


    /**
     * @auther: lizhen
     * @date: 2019/7/22 11:11
     * 功能描述: 单位体检预约
     */
    @RequestMapping(value = "deptOrder2")
    @NoRepeatSumit
    public CommonReturnType deptOrder2(DeptYuYueInfo deptYuYueInfo,String apm) {
        String nowTime = deptYuYueInfo.getDeptTime();           //预约时间
        //判断当前时间是否在预约时间内(06:00-17:00)
        HospitalInfo hospitalInfo = hospitalService.selectByHospitalNum(deptYuYueInfo.getDeptTjcode());
        String time = DateToStrUtil.getStringDateShort();
        String time1;
        String time2;
        if (hospitalInfo.getYuliu3()==null){                    //空时给默认的
             time1 =time +" 06:00:00";                          //上午
             time2 = time +" 17:00:00";                         //下午
        }else{
            String[] split = hospitalInfo.getYuliu3().split(",");
            time1 =time +" "+ split[0];                         //上午
            time2 = time +" "+ split[1];                        //下午
        }
        Date beginTime = DateToStrUtil.StrToDate(time1);
        Date endTime = DateToStrUtil.StrToDate(time2);
        Date nowDate = DateToStrUtil.StrToDate(MyTools.getTime());
        boolean a = DateToStrUtil.belongCalendar(nowDate, beginTime, endTime);
        if (!a){
            return CommonReturnType.createCommonReturnType("该时间段内不能预约！","600");
        }
        String stringDateShort = DateToStrUtil.getStringDateShort();
        if (nowTime.equals(stringDateShort)){
            //上午下午判断0上午1下午
            String result = String.valueOf(DateToStrUtil.nowTime());
            String apms;
            if ("am".equals(apm)){
                apms = "0";
            }else{
                apms = "1";
            }
            if (result.equals("1")){
                if ("0".equals(apms)){
                    return CommonReturnType.createCommonReturnType("不能选择上午的","900");
                }
            }
        }


        //入库
        Map<String, Object> map1 = deptYuYueInfoService.InsertDeptInfo(deptYuYueInfo, apm);
        String status = (String) map1.get("status");
        if ("1".equals(status)){
            return CommonReturnType.createCommonReturnType("预约成功","200");
        }else if ("2".equals(status)){
            return CommonReturnType.createCommonReturnType("预约人数超过可预约人数","700");
        }else if ("3".equals(status)){
            return CommonReturnType.createCommonReturnType("当日预约人数已满，请选择其它时间","800");
        }
        return CommonReturnType.createCommonReturnType("预约失败","100");

    }

    /**
     * @auther: lizhen
     * @date: 2019/7/22 14:47
     * 功能描述: 预约未体检&预约已体检
     */
    @RequestMapping(value = "tjStatusList")
    public CommonReturnType tjStatusList(@RequestParam(defaultValue = "1")int page,
                                         @RequestParam(defaultValue = "10")int limit,
                                         String tjStatus,String hospitalNum){
        Map<String,Object> map = new HashMap<>();
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser==null){
            return CommonReturnType.createCommonReturnType("登录过期","250");
        }else{
            List<DeptYuYueInfo> list = deptYuYueInfoService.tjStatusList(tjStatus,hospitalNum,page,limit);
            int i = deptYuYueInfoService.tjStatusListCount(tjStatus,hospitalNum);
            map.put("count",i);
            map.put("pageData",list);
            return CommonReturnType.createCommonReturnType(map);
        }
    }

    /**
     * @auther: lizhen
     * @date: 2019/7/22 15:04
     * 功能描述: 预约未体检&预约已体检确认时间
     */
    @RequestMapping(value = "tjStatusUpdate")
    public CommonReturnType tjStatusUpdate(Integer deptId,String tjStatus){
        int i = deptYuYueInfoService.tjStatusUpdate(deptId, tjStatus);
        if (i==0){
            return CommonReturnType.createCommonReturnType("成功","200");
        }else if(i==1){
            return CommonReturnType.createCommonReturnType("体检时间没到","300");
        }
        return CommonReturnType.createCommonReturnType("失败","100");
    }


    /**
     * @auther: lizhen
     * @date: 2019/7/12 12:14
     * 功能描述: 预约列表
     */
    @RequestMapping(value = "deptList")
    @ResponseBody
    public CommonReturnType deptList(@RequestParam(defaultValue = "1")int page,
                                     @RequestParam(defaultValue = "10")int limit,
                                     String deptShenhe,String hospitalNum){
        Map<String,Object> map = new HashMap<>();
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser==null){
            return CommonReturnType.createCommonReturnType("登录过期","250");
        }else{
            List<DeptYuYueInfo> list = deptYuYueInfoService.deptList(hospitalNum,deptShenhe, page, limit);
            int i = deptYuYueInfoService.deptCountBYhospitalNum(hospitalNum,deptShenhe);
            map.put("count",i);
            map.put("pageData",list);
            return CommonReturnType.createCommonReturnType(map);
        }
    }
    
    
    /**
     * @auther: lizhen
     * @date: 2019/7/12 13:49
     * 功能描述: 单位预约待审/已审总数
     */
    @RequestMapping(value = "deptCount")
    @ResponseBody
    public CommonReturnType deptCount(String hospitalNum){
        Map<String,Object> map = new HashMap<>();
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser==null) {
            return CommonReturnType.createCommonReturnType("登录过期，请重新登录！", "250");
        }else{
            int i1 = deptYuYueInfoService.dshCount(hospitalNum);        //待审核总数
            int i2 = deptYuYueInfoService.yshCount(hospitalNum);        //已审核总数
            map.put("daish",i1);
            map.put("yish",i2);
            return CommonReturnType.createCommonReturnType(map);
        }
    }

    /**
     * @auther: lizhen
     * @date: 2019/7/12 14:02
     * 功能描述: 单位预约资质审核
     */
    @RequestMapping(value = "deptSH")
    @ResponseBody
    @RequiresPermissions("deptorder:update")
    public CommonReturnType deptSH(String deptId,String deptShenhe,String deptPhone,String time, String why) throws Exception {
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser == null){
            return CommonReturnType.createCommonReturnType("登录过期，请重新登录！", "250");
        }
        if (deptYuYueInfoService.deptSH(deptId,deptShenhe,deptPhone,time,why)){
            //短信提醒1通过2不通过
            if ("1".equals(deptShenhe)){
                SendMsgUtil.sendMsg(deptPhone,time,"text5");
            }
            if ("2".equals(deptShenhe)){
                SendMsgUtil.sendMsg(deptPhone, why, "text4");
            }
            return CommonReturnType.createCommonReturnType("成功","200");
        }
        return CommonReturnType.createCommonReturnType("失败","100");
    }

    /**
     * @auther: lizhen
     * @date: 2019/7/12 14:11
     * 功能描述: 单位体检预约
     */
    @RequestMapping(value = "deptOrder")
    @ResponseBody
    public CommonReturnType deptOrder(DeptYuYueInfo deptYuYueInfo){
        Map<String, String> map = deptYuYueInfoService.deptOrder(deptYuYueInfo);
        String status = map.get("status");
        if ("200".equals(status)){
            return CommonReturnType.createCommonReturnType("预约成功","200");
        }else if ("0".equals(status)){
            return CommonReturnType.createCommonReturnType(map,"300");      //已预约未审核
        }else if ("1".equals(status)){
            return CommonReturnType.createCommonReturnType(map,"300");      //审核通过
        }else if ("2".equals(status)){
            return CommonReturnType.createCommonReturnType(map,"300");      //审核不通过
        }else{
            return CommonReturnType.createCommonReturnType("预约失败","100");
        }
    }
    
    
    
    /**
     * @auther: lizhen
     * @date: 2019/7/23 15:16
     * 功能描述: 修改体检时间
     */
    @RequestMapping(value = "updateTJtime")
    @RequiresPermissions("deptorder:update")
    public CommonReturnType updateTJtime(Integer deptId,String time,String deptPhone) throws Exception {

        if (deptYuYueInfoService.updateTJtime(deptId,time)){
            SendMsgUtil.sendMsg(deptPhone,time,"text5");
            return CommonReturnType.createCommonReturnType("成功","200");
        }
        return CommonReturnType.createCommonReturnType("失败","100");
    }


    /**
     * @auther: lizhen
     * @date: 2019/7/12 14:59
     * 功能描述: 多条件查询
     */
    @RequestMapping(value = "keywordSelect")
    @ResponseBody
    public CommonReturnType keywordSelect(DeptYuYueInfo deptYuYueInfo) throws BusinessException {
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser==null){
            return CommonReturnType.createCommonReturnType("登陆过期，请重新登陆！","250");
        }else{
            String hospitalNum = currentUser.getYuliu3();
            List<DeptYuYueInfo> list = deptYuYueInfoService.keywordSelect(hospitalNum,deptYuYueInfo);
            if (list.size()>0){
                return CommonReturnType.createCommonReturnType(list,"200");
            }
            return CommonReturnType.createCommonReturnType("null","100");
        }
    }

    /**
     * @auther: lizhen
     * @date: 2019/7/12 15:00
     * 功能描述: 查看单位预约资质信息
     */
    @RequestMapping(value = "deptInfoLook")
    @ResponseBody
    public CommonReturnType deptInfoLook(Integer deptId) throws Exception {
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser == null){
            return CommonReturnType.createCommonReturnType("登录过期！","250");
        }else{
            List<String> list = deptYuYueInfoService.deptInfoLook(deptId);
            if (list.size()>0){
                return CommonReturnType.createCommonReturnType(list,"200");
            }
            return CommonReturnType.createCommonReturnType("没有照片信息","100");
        }
    }


    /**
     * @auther: lizhen
     * @date: 2019/7/24 15:40
     * 功能描述: 医院剩余可预约人数
     * 参数: hospitalNum,time
     */
    @RequestMapping(value = "companyOrder")
    public CommonReturnType companyOrder(String hospitalNum, String time){
        Map<String,Object> map = deptYuYueInfoService.companyOrder(hospitalNum,time);
        int am = (int) map.get("am");               //上午可预约人数
        int pm = (int) map.get("pm");               //下午可预约人数
        return CommonReturnType.createCommonReturnType(map,"200");
    }




    /**
     * @auther: lizhen
     * @date: 2019/7/24 17:12
     * 功能描述: 监督机关对预约资质审核
     */
    @RequestMapping(value = "zzsh")
    public CommonReturnType zzsh(Integer deptId,String status){
        if (deptYuYueInfoService.zzsh(deptId,status)){
            return CommonReturnType.createCommonReturnType("资质审核成功","200");
        }
        return CommonReturnType.createCommonReturnType("资质审核失败","100");
    }


}
