package com.mdkproject.mdkshiro.modules.controller;

import com.mdkproject.mdkshiro.controller.BaseController;
import com.mdkproject.mdkshiro.entity.TiJianInfo;
import com.mdkproject.mdkshiro.entity.UserInfo;
import com.mdkproject.mdkshiro.modules.service.*;
import com.mdkproject.mdkshiro.modules.util.DateToStrUtil;
import com.mdkproject.mdkshiro.response.CommonReturnType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @program: mdk-shiro->CountDataController
 * @description: 数据统计管理
 * @author: lizhen
 * @create: 2019-07-17 09:22
 **/
@RestController
@RequestMapping("countData")
public class CountDataController extends BaseController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private TiJianInfoService tiJianInfoService;
    @Autowired
    private DeptYuYueInfoService deptYuYueInfoService;
    @Autowired
    private HearthCardService hearthCardService;
    @Autowired
    private CountDataService countDataService;
    @Autowired
    private HospitalService hospitalService;


    /**
     * @auther: lizhen
     * @date: 2019/7/17 9:24
     * 功能描述: 首页5快统计
     */
    @RequestMapping(value = "allCount")
    @Transactional
    public CommonReturnType allCount(String hospitalNum){
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser == null){
            return CommonReturnType.createCommonReturnType("登录过期，请重新登录！", "250");
        }else{
            Map<String,Object> map = new HashMap<>();
            int todayData1 = tiJianInfoService.getTodayData(hospitalNum);                   //当日体检人数
            int todayData2 = deptYuYueInfoService.getTodayData(hospitalNum);                //当日预约人数
            int todayData3 = hearthCardService.getTodayData(hospitalNum);                   //当日发证人数
            int monthData4 = tiJianInfoService.getMonthData(hospitalNum);                   //当月体检人数
            int allByHospitalNum = tiJianInfoService.getAllByHospitalNum(hospitalNum);      //体检总人数
            map.put("day_tjcount",todayData1);
            map.put("day_yuyue",todayData2);
            map.put("day_fz",todayData3);
            map.put("month_tjcount",monthData4);
            map.put("all_tjcount",allByHospitalNum);
            return CommonReturnType.createCommonReturnType(map,"200");
        }
    }
    
    
    /**
     * @auther: lizhen
     * @date: 2019/7/17 9:39
     * 功能描述: 预约单位人数数据统计，统计7天数据
     */
    @RequestMapping(value = "yuyueWeekData")
    @Transactional
    public CommonReturnType yuyueWeekData(String hospitalNum){
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser == null){
            return CommonReturnType.createCommonReturnType("登录过期，请重新登录！", "250");
        }else{
            List<String> dateToWeek = DateToStrUtil.getDateToWeek(new Date());//一周日期yyyy-MM-dd
            Map<String,Object> map = new TreeMap<>(
//                new Comparator<String>() {
//                    @Override
//                    public int compare(String o1, String o2) {
//                        //降序
//                        return o2.compareTo(o1);
//                    }
//                }
            );
            String oneDay;
            int oneDataSum;
            for (int i=0;i<dateToWeek.size();i++){
                oneDay = dateToWeek.get(i);
                oneDataSum = deptYuYueInfoService.getOneData(oneDay,hospitalNum);
                map.put(oneDay,oneDataSum);
            }
            return CommonReturnType.createCommonReturnType(map);
        }

    }


    /**
     * @auther: lizhen
     * @date: 2019/7/17 9:48
     * 功能描述: 当日体检&发证统计
     */
    @RequestMapping(value = "tjAddfzWeekData")
    @Transactional
    public CommonReturnType tjAddfzWeekData(String hospitalNum){
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser == null){
            return CommonReturnType.createCommonReturnType("登录过期，请重新登录！", "250");
        }else{
            List<String> dateToWeek = DateToStrUtil.getDateToWeek(new Date());//一周日期yyyy-MM-dd
            Map<String,Object> map = tiJianInfoService.tjAddfzWeekData(dateToWeek,hospitalNum);
            return CommonReturnType.createCommonReturnType(map);
        }
    }


    /**
     * @auther: lizhen
     * @date: 2019/7/17 10:05
     * 功能描述: 健康证表格统计
     */
    @RequestMapping(value = "healthCount")
    @Transactional
    public CommonReturnType healthCount(String areaNum, String hospitalNum, String dates,
                                        @RequestParam(defaultValue = "1")int page,
                                        @RequestParam(defaultValue = "10")int limit){
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser == null){
            return CommonReturnType.createCommonReturnType("登录过期，请重新登录","250");
        }else {
            Map<String,Object> map = new HashMap<>();
            String areaNum1 = currentUser.getYuliu4();       //登录用户区域编号
            if (StringUtils.isEmpty(areaNum) && StringUtils.isEmpty(hospitalNum)){
                return CommonReturnType.createCommonReturnType("请选择查询条件","150");
            }
            if (!areaNum1.equals(areaNum)){
                return CommonReturnType.createCommonReturnType("您没有查询该区域数据权限！","400");
            }else{
                //区域+医院+时间
                if (StringUtils.isNotEmpty(areaNum) && StringUtils.isNotEmpty(hospitalNum)){
                    List list = countDataService.selectByAreaNumAndHospitalNum(hospitalNum,dates,page,limit);
                    int i = countDataService.selectHospitalNumCount(hospitalNum,dates);
                    map.put("count",i);
                    map.put("pageData",list);
                    return CommonReturnType.createCommonReturnType(map,"200");
                }else if(StringUtils.isNotEmpty(areaNum) || StringUtils.isNotEmpty(hospitalNum)){
                 //区域+时间
                    int countt = 0;
                    List<TiJianInfo> listAll = new ArrayList<>();
                    List<String> list = hospitalService.hospitalNumListByAreaNum(areaNum);
                    for (int i=0;i<list.size();i++){
                        String hospitalNums = list.get(i);
                        List lists = countDataService.selectByAreaNumAndHospitalNum(hospitalNums,dates,page,limit);
                        int i1 = countDataService.selectHospitalNumCount(hospitalNums,dates);
                        countt = countt + i1;
                        listAll.addAll(lists);
                    }
                    map.put("count",countt);
                    map.put("pageData",listAll);
                    return CommonReturnType.createCommonReturnType(map,"200");
                }else {
                    return CommonReturnType.createCommonReturnType("请选择区域","150");
                }
            }
        }
    }


}
