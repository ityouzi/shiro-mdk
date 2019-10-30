package com.mdkproject.mdkshiro.modules.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mdkproject.mdkshiro.controller.BaseController;
import com.mdkproject.mdkshiro.entity.HearthCardInfo;
import com.mdkproject.mdkshiro.entity.TiJianInfo;
import com.mdkproject.mdkshiro.entity.UserInfo;
import com.mdkproject.mdkshiro.modules.service.HearthCardService;
import com.mdkproject.mdkshiro.modules.service.TJProjectService;
import com.mdkproject.mdkshiro.modules.service.TiJianInfoService;
import com.mdkproject.mdkshiro.modules.service.model.TjProjectModel;
import com.mdkproject.mdkshiro.modules.service.model.TjshModel;
import com.mdkproject.mdkshiro.response.CommonReturnType;
import com.mdkproject.mdkshiro.utils.SendMsgUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: mdk-shiro->TiJianInfoController
 * @description: 体检信息管理
 * @author: lizhen
 * @create: 2019-07-15 16:02
 **/
@RestController
@RequestMapping("tijian")
public class TiJianInfoController extends BaseController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TiJianInfoService tiJianInfoService;
    @Autowired
    private HearthCardService hearthCardService;
    @Autowired
    private TJProjectService tjProjectService;
    @Autowired
    private StringRedisTemplate redisTemplate;



    /**
     * @auther: lizhen
     * @date: 2019/7/15 16:07
     * 功能描述: 体检信息快速录入，体检项目默认正常，健康证默认合格
     */
    @RequestMapping(value = "quickInput")
    @Transactional
    @RequiresPermissions("tijian:quickadd")
    public CommonReturnType quickInput(TiJianInfo tiJianInfo) throws Exception {
        if (StringUtils.isEmpty(tiJianInfo.getIdcardPhoto())) {
            return CommonReturnType.createCommonReturnType("没有身份证照片", "300");
        }
        //判断是否满11个月
        String result = tiJianInfoService.selectByIdCardNum(tiJianInfo.getIdcardNum());
        if (result != null){
            return CommonReturnType.createCommonReturnType(result,"400");
        }
        HearthCardInfo hearthCardInfo = tiJianInfoService.quickInput(tiJianInfo);
        if (hearthCardInfo != null){
            //短信
            SendMsgUtil.sendMsg(tiJianInfo.getTelphone(), tiJianInfo.getName().trim(), "text3");
            //体检项目
            TjProjectModel tjProjectModel = this.convertModel(tiJianInfo);
            tjProjectModel.setStatus("1");
            tjProjectService.addProject(tjProjectModel);
            return CommonReturnType.createCommonReturnType(hearthCardInfo,"200");
        }
        return CommonReturnType.createCommonReturnType("失败","100");
    }


    /**
     * @auther: lizhen
     * @date: 2019/7/15 16:07
     * 功能描述: 体检信息基础录入
     */
    @RequestMapping(value = "slowInput")
    @RequiresPermissions("tijian:slowadd")
    public CommonReturnType slowInput(TiJianInfo tiJianInfo) throws Exception {
        if (StringUtils.isEmpty(tiJianInfo.getIdcardPhoto())) {
            return CommonReturnType.createCommonReturnType("没有身份证照片", "300");
        }
        //判断是否满11个月
        String result = tiJianInfoService.selectByIdCardNum(tiJianInfo.getIdcardNum());
        if (result != null){
            return CommonReturnType.createCommonReturnType(result,"400");
        }
        TiJianInfo tiJianInfo1 = tiJianInfoService.slowInput(tiJianInfo);               //健康证已失效重新办理
        Map<String,Object> map = new HashMap<>();
        if (tiJianInfo1 != null){
            //短信
            SendMsgUtil.sendMsg(tiJianInfo.getTelphone(), tiJianInfo.getName().trim(), "text3");
            return CommonReturnType.createCommonReturnType(tiJianInfo1,"200");
        }
        return CommonReturnType.createCommonReturnType("失败","100");
    }


    /**
     * @auther: lizhen
     * @date: 2019/7/23 10:52
     * 功能描述: web端补录
     */
    @RequestMapping(value = "puluInfo")
    public CommonReturnType puluInfo(TiJianInfo tiJianInfo){
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser == null){
            return CommonReturnType.createCommonReturnType("登录过期！","250");
        }
        if (tiJianInfoService.puluInfo(tiJianInfo)){
            return CommonReturnType.createCommonReturnType("补录成功","200");
        }
        return CommonReturnType.createCommonReturnType("补录失败","100");
    }

    /**
     * @auther: lizhen
     * @date: 2019/7/18 17:37
     * 功能描述: 健康证
     */
    @RequestMapping(value = "getCard")
    public CommonReturnType getCard(String idCardNum){
        HearthCardInfo healthCardInfo = hearthCardService.selectByIdCardNum2(idCardNum);
        if (healthCardInfo != null){
            return CommonReturnType.createCommonReturnType(healthCardInfo,"200");
        }
        return CommonReturnType.createCommonReturnType("失败","100");
    }


    /**
     * @auther: lizhen
     * @date: 2019/7/18 13:08
     * 功能描述: 体检表
     */
    @RequestMapping(value = "getTable")
    public CommonReturnType getTable(Integer tjId) throws Exception {
        TiJianInfo tiJianInfo = tiJianInfoService.getTable(tjId);
        if (tiJianInfo != null){
            return CommonReturnType.createCommonReturnType(tiJianInfo,"200");
        }
        return CommonReturnType.createCommonReturnType("失败","200");
    }




    /**
     * @auther: lizhen
     * @date: 2019/7/15 16:09
     * 功能描述: 体检单条审核
     */
    @RequestMapping(value = "oneTJSH")
    @RequiresPermissions("tijian:update")
    public CommonReturnType oneTJSH(TjshModel tjshModel) throws Exception {
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser==null){
            return CommonReturnType.createCommonReturnType("登录过期！","250");
        }
        TiJianInfo tiJianInfo = tiJianInfoService.oneTJSH(tjshModel);
        if (tiJianInfo != null){
            //短信
            if ("1".equals(tiJianInfo.getStatus())){
                //发送审核成功短信
                SendMsgUtil.sendMsg(tiJianInfo.getTelphone(),tiJianInfo.getName().trim(),"text2");
                //体检项目
                return CommonReturnType.createCommonReturnType("成功","200");
            }
            if ("2".equals(tiJianInfo.getStatus())){
                //发送审核不通过短信
                SendMsgUtil.sendMsg(tiJianInfo.getTelphone(),tiJianInfo.getName().trim(), "text1");
                return CommonReturnType.createCommonReturnType("成功","200");
            }
        }
        return CommonReturnType.createCommonReturnType("失败","100");
    }


    /**
     * @auther: lizhen
     * @date: 2019/7/15 18:22
     * 功能描述: 体检信息批量审核
     */
    @RequestMapping(value = "batchUpTJdate")
    @RequiresPermissions("tijian:update")
    public CommonReturnType batchUpTJdate(TjshModel tjshModel,String telphoneAndName) throws Exception {
        JSONArray array = JSONArray.parseArray(telphoneAndName);
        if (tiJianInfoService.batchUpTJdate(tjshModel)){
            if ("1".equals(tjshModel.getStatus())){
                //审核合格短信
                for (Object o : array){
                    JSONObject json = (JSONObject) o;
                    SendMsgUtil.sendMsg(json.getString("tel"),json.getString("name"),"text2");                    //发送审核成功短信
                }
                //体检项目
                return CommonReturnType.createCommonReturnType("审核成功","200");
            }else{
                //审核不合格短信
                for (Object o : array){
                    JSONObject json = (JSONObject) o;
                    SendMsgUtil.sendMsg(json.getString("tel"),json.getString("name"),"text1");                   //发送审核失败短信
                }
                return CommonReturnType.createCommonReturnType("审核成功","200");
            }
        }
        return CommonReturnType.createCommonReturnType("失败","100");
    }



    /**
     * @auther: lizhen
     * @date: 2019/7/16 9:08
     * 功能描述: 安卓端体检录入
     */
    @RequestMapping(value = "AndroidInput")
    public CommonReturnType AndroidInput(TiJianInfo tiJianInfo) throws Exception {
        tiJianInfo.setName(tiJianInfo.getName().replaceAll(" ",""));
        //判断健康证是否在有效期内
        //判断是否满11个月
        String result = tiJianInfoService.selectByIdCardNum(tiJianInfo.getIdcardNum());
        if (result == null){
            if (tiJianInfoService.AndroidInput(tiJianInfo)){
                //发送登记成功短信
                SendMsgUtil.sendMsg(tiJianInfo.getTelphone().trim(),tiJianInfo.getName().trim(),"text3");
                return CommonReturnType.createCommonReturnType("成功","200");
            }
        }
        return CommonReturnType.createCommonReturnType("健康证还在有效期内","300");
    }



    /**
     * @auther: lizhen
     * @date: 2019/7/16 12:01
     * 功能描述: 查询当日体检人数
     */
    @RequestMapping(value = "getTodayData")
    public CommonReturnType getTodayData(String hospitalNum){
        int todayData = tiJianInfoService.getTodayData(hospitalNum);
        return CommonReturnType.createCommonReturnType(todayData);
    }


    /**
     * @auther: lizhen
     * @date: 2019/7/16 11:26
     * 功能描述: 获取体检最近一周数据
     */
    @RequestMapping(value = "getWeekData")
    public CommonReturnType getWeekData(String status,String hospitalNum,
                                        @RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int limit) throws Exception {
        //0未审核（基础）,1已审核（快速）
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser==null){
            return CommonReturnType.createCommonReturnType("登录过期","250");
        }else{
            Map<String,Object> map = new HashMap<>();
            List<TiJianInfo> list = tiJianInfoService.getWeekData(hospitalNum,status,page,limit);
            int i = tiJianInfoService.getWeekDataCount(hospitalNum,status);
            map.put("count",i);
            map.put("pageCount",list);
            return CommonReturnType.createCommonReturnType(map);
        }
    }

    /**
     * @auther: lizhen
     * @date: 2019/7/16 11:20
     * 功能描述: 获取体检当月体检数据
     */
    @RequestMapping(value = "getMonthData")
    public CommonReturnType getMonthData(String hospitalNum){
        int monthData = tiJianInfoService.getMonthData(hospitalNum);
        return CommonReturnType.createCommonReturnType(monthData);
    }

    /**
     * @auther: lizhen
     * @date: 2019/7/16 12:06
     * 功能描述: 查询一家医院体检总人数，首页
     */
    @RequestMapping(value = "getAll")
    public CommonReturnType getAllByHospitalNum(String hospitalNum){
        int all = tiJianInfoService.getAllByHospitalNum(hospitalNum);
        return CommonReturnType.createCommonReturnType(all);
    }



    /**
     * @auther: lizhen
     * @date: 2019/7/16 12:11
     * 功能描述: 体检审核状态列表
     */
    @RequestMapping(value = "getStatusList")
    public CommonReturnType getStatusList(String hospitalNum,String status,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int limit){
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser == null){
            return CommonReturnType.createCommonReturnType("登录过期,请重新登录！","250");
        }else{
            Map<String,Object> map = new HashMap<>();
            List<TiJianInfo> list = tiJianInfoService.getStatusList(hospitalNum,status,page,limit);
            int i = tiJianInfoService.getStatusListCount(hospitalNum,status);
            map.put("count",i);
            map.put("pageData",list);
            return CommonReturnType.createCommonReturnType(map);
        }
    }


    /**
     * @auther: lizhen
     * @date: 2019/7/16 13:54
     * 功能描述: 体检信息审核多条件查询
     */
    @RequestMapping(value = "keywordSelect")
    @ResponseBody
    public CommonReturnType keywordSelect(String name,String number,String idCardNum,String dates){
        List<TiJianInfo> list = tiJianInfoService.keywordSelect(name,number,idCardNum,dates);
        if (list.size()>0){
            return CommonReturnType.createCommonReturnType(list,"200");
        }
        return CommonReturnType.createCommonReturnType("没有查到","100");
    }





    /**
     * 模型转换 entity--->model
     * */
    private TjProjectModel convertModel(TiJianInfo tiJianInfo){
        TjProjectModel tjProjectModel = new TjProjectModel();
        BeanUtils.copyProperties(tiJianInfo,tjProjectModel);
        tjProjectModel.setHealthNum(tiJianInfo.getHearthcardNum());
        tjProjectModel.setTjTime(tiJianInfo.getYuliu2());
        tjProjectModel.setHospitalNum(tiJianInfo.getDeptNum());
        return tjProjectModel;
    }


}
