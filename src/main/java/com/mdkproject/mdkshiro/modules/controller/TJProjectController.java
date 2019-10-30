package com.mdkproject.mdkshiro.modules.controller;

import com.mdkproject.mdkshiro.controller.BaseController;
import com.mdkproject.mdkshiro.entity.UserInfo;
import com.mdkproject.mdkshiro.modules.service.TJProjectService;
import com.mdkproject.mdkshiro.modules.service.model.TjProjectModel;
import com.mdkproject.mdkshiro.response.CommonReturnType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: mdk-shiro->TJProjectController
 * @description: 体检项目控制类
 * @author: lizhen
 * @create: 2019-07-18 13:04
 **/
@RestController
@RequestMapping("tjproject")
public class TJProjectController extends BaseController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private TJProjectService tjProjectService;


    /**
     * @auther: lizhen
     * @date: 2019/7/19 11:14
     * 功能描述: 基础录入添加体检项目
     */
    @RequestMapping(value = "addProject")
    @Transactional
    public CommonReturnType addProject(TjProjectModel tjProjectModel){
        if (tjProjectService.addProject(tjProjectModel)){
            return CommonReturnType.createCommonReturnType("成功","200");
        }
        return CommonReturnType.createCommonReturnType("失败","100");
    }


    /**
     * @auther: lizhen
     * @date: 2019/7/19 11:49
     * 功能描述: 获取当前医院体检项目数据
     */
    @RequestMapping(value = "getDataByHospitalNum")
    public CommonReturnType getDataByHospitalNum(String hospitalNum,
                                                 @RequestParam(defaultValue = "1")int page,
                                                 @RequestParam(defaultValue = "10")int limit){
        UserInfo currentUser = getCurrentUser(request);
        Map<String,Object> map = new HashMap<>();
        if (currentUser==null){
            return CommonReturnType.createCommonReturnType("登录过期！","250");
        }else{
            List<TjProjectModel> tjProjectModels = tjProjectService.getDataByHospitalNum(hospitalNum,page,limit);
            int i = tjProjectService.getDataByHospitalNumCount(hospitalNum);
            map.put("count",i);
            map.put("pageData",tjProjectModels);
            return CommonReturnType.createCommonReturnType(map,"200");
        }
    }


}
