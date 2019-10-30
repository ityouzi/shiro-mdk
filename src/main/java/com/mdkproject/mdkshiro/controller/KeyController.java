package com.mdkproject.mdkshiro.controller;

import com.mdkproject.mdkshiro.entity.KeyCode;
import com.mdkproject.mdkshiro.response.CommonReturnType;
import com.mdkproject.mdkshiro.service.UserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: mdk-shiro->KeyController
 * @description: KEY
 * @author: lizhen
 * @create: 2019-07-19 17:03
 **/
@RestController
@RequestMapping("user")
public class KeyController {

    @Autowired
    private UserInfoService userInfoService;


    /**
     * @auther: lizhen
     * @date: 2019/7/11 15:59
     * 功能描述: 激活码查询
     */
    @RequestMapping(value = "getKeyCode")
    @ResponseBody
    public CommonReturnType getKeyCode(String key){
        if (StringUtils.isEmpty(key)){
            return CommonReturnType.createCommonReturnType("请输入激活码","300");
        }
        Map<String,String> map = userInfoService.getKeyCode(key);
        if ("0".equals(map.get("result"))){
            return CommonReturnType.createCommonReturnType("激活码不存在","400");
        }else if ("2".equals(map.get("result"))){
            return CommonReturnType.createCommonReturnType("激活码已失效","100");
        }else{
            return CommonReturnType.createCommonReturnType(map,"200");
        }
    }



    /**
     * @auther: lizhen
     * @date: 2019/7/11 17:25
     * 功能描述: 新增激活码
     */
    @RequestMapping(value = "addKeyCode")
    @ResponseBody
    public CommonReturnType addKeyCode(String hospitalNum){
        if (userInfoService.addKeyCode(hospitalNum)){
            return CommonReturnType.createCommonReturnType("成功","200");
        }
        return CommonReturnType.createCommonReturnType("失败","100");
    }


    /**
     * @auther: lizhen
     * @date: 2019/7/22 11:13
     * 功能描述: 激活码列表
     */
    @RequestMapping(value = "keyList")
    public CommonReturnType keyList(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int limit){
        Map<String,Object> map = new HashMap<>();
        List<KeyCode> list = userInfoService.keyList(page,limit);
        int i = userInfoService.keyListCount();
        map.put("count",i);
        map.put("pageData",list);
        return CommonReturnType.createCommonReturnType(map);
    }
}
