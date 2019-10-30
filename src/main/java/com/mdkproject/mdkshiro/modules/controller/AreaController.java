package com.mdkproject.mdkshiro.modules.controller;

import com.mdkproject.mdkshiro.controller.BaseController;
import com.mdkproject.mdkshiro.entity.Area;
import com.mdkproject.mdkshiro.modules.service.AreaService;
import com.mdkproject.mdkshiro.response.CommonReturnType;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: mdk-shiro->AreaController
 * @description: 区域管理模块
 * @author: lizhen
 * @create: 2019-07-12 09:48
 **/

@Controller
@RequestMapping("area")
public class AreaController extends BaseController {

    @Autowired
    AreaService areaService;


    /**
     * @auther: lizhen
     * @date: 2019/7/12 10:37
     * 功能描述: 区域列表
     */
    @RequestMapping(value = "areaList")
    @ResponseBody
    public CommonReturnType areaList(@RequestParam(defaultValue = "1")int page,
                                     @RequestParam(defaultValue = "10")int limit){
        return CommonReturnType.createCommonReturnType(areaService.areaList(page,limit));
    }


    /**
     * @auther: lizhen
     * @date: 2019/7/12 10:18
     * 功能描述: 添加区域
     */
    @RequestMapping(value = "areaAdd")
    @ResponseBody
    @RequiresPermissions("area:add")
    public CommonReturnType areaAdd(Area area){
        if (areaService.areaAdd(area)){
            return CommonReturnType.createCommonReturnType("成功","200");
        }
        return CommonReturnType.createCommonReturnType("失败","100");
    }

    /**
     * @auther: lizhen
     * @date: 2019/7/12 10:33
     * 功能描述: 删除区域
     */
    @RequestMapping(value = "areaDelete")
    @ResponseBody
    @RequiresPermissions("area:del")
    public CommonReturnType areaDelete(Area area){
        if (areaService.areaDelete(area.getAreaId())){
            return CommonReturnType.createCommonReturnType("成功","200");
        }
        return CommonReturnType.createCommonReturnType("失败","100");
    }

    /**
     * @auther: lizhen
     * @date: 2019/7/12 10:35
     * 功能描述: 修改区域
     */
    @RequestMapping(value = "areaUpdate")
    @ResponseBody
    @RequiresPermissions("area:update")
    public CommonReturnType areaUpdate(Area area){
        if (areaService.areaUpdate(area)){
            return CommonReturnType.createCommonReturnType("成功","200");
        }
        return CommonReturnType.createCommonReturnType("失败","100");
    }

    /**
     * @auther: lizhen
     * @date: 2019/7/12 10:35
     * 功能描述: 查询区域详情
     */
    @RequestMapping(value = "areaGet")
    @ResponseBody
    public CommonReturnType areaGet(Area area){
        return CommonReturnType.createCommonReturnType(areaService.areaGet(area.getAreaId()));
    }




}
