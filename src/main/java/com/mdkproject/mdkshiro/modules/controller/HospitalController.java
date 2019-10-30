package com.mdkproject.mdkshiro.modules.controller;

import com.mdkproject.mdkshiro.controller.BaseController;
import com.mdkproject.mdkshiro.entity.HospitalInfo;
import com.mdkproject.mdkshiro.entity.UserInfo;
import com.mdkproject.mdkshiro.modules.service.HospitalService;
import com.mdkproject.mdkshiro.response.CommonReturnType;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: mdk-shiro->HospitalController
 * @description: 体检医院
 * @author: lizhen
 * @create: 2019-07-12 15:13
 **/
@RestController
@RequestMapping(value = "hospital")
public class HospitalController extends BaseController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HospitalService hospitalService;



    /**
     * @auther: lizhen
     * @date: 2019/7/12 14:58
     * 功能描述: 依据区域编号获取对应的医院集合
     */
    @RequestMapping(value = "selectNum")
    @ResponseBody
    public CommonReturnType selectNum(String areaNum){
        if (StringUtils.isEmpty(areaNum)){
            return CommonReturnType.createCommonReturnType("参数为空！","100");
        }
        //医院+医院编号
        List<HospitalInfo> list = hospitalService.selectNum(areaNum);
        return CommonReturnType.createCommonReturnType(list);
    }


    /**
     * @auther: lizhen
     * @date: 2019/7/15 12:20
     * 功能描述: 添加医院信息
     */
    @RequestMapping(value = "hospitalAdd")
    @ResponseBody
    @RequiresPermissions("hospital:add")
    public CommonReturnType hospitalAdd(HospitalInfo hospitalInfo){
        if (hospitalService.hospitalAdd(hospitalInfo)){
            return CommonReturnType.createCommonReturnType("成功","200");
        }
        return CommonReturnType.createCommonReturnType("失败","100");
    }

    /**
     * @auther: lizhen
     * @date: 2019/7/15 13:39
     * 功能描述: 医院列表
     */
    @RequestMapping(value = "hospitalList")
    @ResponseBody
    public CommonReturnType hospitalList(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int limit){
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser == null){
            return CommonReturnType.createCommonReturnType("登录过期！","250");
        }
        Map<String,Object> map = new HashMap<>();
        List<HospitalInfo> list = hospitalService.hospitalList(page,limit);
        int i = hospitalService.hospitalCount();
        map.put("count",i);
        map.put("pageData",list);
        return CommonReturnType.createCommonReturnType(map);
    }
    
    
    /**
     * @auther: lizhen
     * @date: 2019/7/18 17:04
     * 功能描述: 医院模板管理
     */
    @RequestMapping(value = "templateManager")
    @RequiresPermissions("hospital:add")
    @RequiresRoles("admin")
    public CommonReturnType templateManager(Integer hospitalId,String templateNum){
        if (hospitalService.templateManager(hospitalId,templateNum)){
            return CommonReturnType.createCommonReturnType("成功","200");
        }
        return CommonReturnType.createCommonReturnType("失败","100");
    }

    /**
     * @auther: lizhen
     * @date: 2019/7/19 10:52
     * 功能描述: 医院模板查看
     */




    /**
     * @auther: lizhen
     * @date: 2019/7/24 16:11
     * 功能描述: 医院每天处理体检人数控制
     */
    @RequestMapping(value = "tjNum")
    public CommonReturnType tjNum(Integer hospitalId,String num){
        if (hospitalService.tjNum(hospitalId,num)){
            return CommonReturnType.createCommonReturnType("成功","200");
        }
        return CommonReturnType.createCommonReturnType("失败","100");
    }

    /**
     * @auther: lizhen
     * @date: 2019/7/24 17:36
     * 功能描述: 修改医院信息
     */
    @RequestMapping(value = "updateHospital")
    public CommonReturnType updateHospital(HospitalInfo hospitalInfo){

        return CommonReturnType.createCommonReturnType(null);
    }

    /**
     * @auther: lizhen
     * @date: 2019/7/26 10:55
     * 功能描述: 设置医院体检时间
     */
    @RequestMapping(value = "hospitalTime")
    public CommonReturnType hospitalTime(Integer hospitalId,String time){
        if (hospitalService.hospitalTime(hospitalId,time)){
            return CommonReturnType.createCommonReturnType("成功","200");
        }
        return CommonReturnType.createCommonReturnType("失败","100");
    }

}
