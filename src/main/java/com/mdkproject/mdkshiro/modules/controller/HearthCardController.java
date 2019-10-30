package com.mdkproject.mdkshiro.modules.controller;

import com.mdkproject.mdkshiro.controller.BaseController;
import com.mdkproject.mdkshiro.entity.HearthCardInfo;
import com.mdkproject.mdkshiro.entity.UserInfo;
import com.mdkproject.mdkshiro.error.BusinessException;
import com.mdkproject.mdkshiro.error.EmBusinessError;
import com.mdkproject.mdkshiro.modules.service.HearthCardService;
import com.mdkproject.mdkshiro.response.CommonReturnType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: mdk-shiro->HearthCardController
 * @description: 健康证模块管理
 * @author: lizhen
 * @create: 2019-07-16 14:11
 **/
@RestController
@RequestMapping("healthcard")
public class HearthCardController extends BaseController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private HearthCardService hearthCardService;


    /**
     * 健康证查询WEB端
     * */
    @RequestMapping(value = "get")
    public CommonReturnType get(@RequestParam(value = "idCard") String idCard,
                                @RequestParam(value = "telphone") String telphone,
                                @RequestParam(value = "otpCode") String otpCode) throws BusinessException {
        //入参非空校验
        if(StringUtils.isEmpty(idCard)|| StringUtils.isEmpty(telphone) || StringUtils.isEmpty(otpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //redis中的key是否存在
        if(!redisTemplate.hasKey(telphone)){
            throw new BusinessException(EmBusinessError.CODE_ERROR);
        }
        //获取redis缓存中的手机号key和验证码value
        String code = redisTemplate.opsForValue().get(telphone);
        if (code == null || "".equals(code)){
            throw new BusinessException(EmBusinessError.CODE_LOSE);
        }
        //验证
        if (!StringUtils.equals(otpCode,code)){
            throw new BusinessException(EmBusinessError.CODE_ERROR);
        }
        //返回信息
        Map<String, String> map = hearthCardService.selectByIdCardNum(idCard);
        if (map != null){
            return CommonReturnType.createCommonReturnType(map,"200");
        }
        return CommonReturnType.createCommonReturnType("没有查到信息","100");
    }

    /**
     * @auther: lizhen
     * @date: 2019/7/16 14:41
     * 功能描述: 健康证查询 小程序
     */
    @RequestMapping(value = "getXCX")
    public CommonReturnType getXCX(@RequestParam(value = "idCard")String idCard,
                                   @RequestParam(value = "telphone")String telphone,
                                   @RequestParam(value = "otpCode")String otpCode) throws Exception {
        //入参非空校验
        if(StringUtils.isEmpty(idCard)|| StringUtils.isEmpty(telphone) || StringUtils.isEmpty(otpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //redis中的key是否存在
        if (!redisTemplate.hasKey(telphone)){
            throw new BusinessException(EmBusinessError.CODE_ERROR);
        }
        //获取redis缓存中的手机号key和验证码value
        String code = redisTemplate.opsForValue().get(telphone);
        if (code==null || "".equals(code)){
            throw new BusinessException(EmBusinessError.CODE_LOSE);
        }
        //验证手机号和对应的验证码相符合
        if (!StringUtils.equals(otpCode,code)){
            throw new BusinessException(EmBusinessError.CODE_ERROR);
        }
        //状态判断（审核中，合格，不合格）
        Map<String,Object> map = hearthCardService.getXCX(idCard);
        if (map.size()==0){
            return CommonReturnType.createCommonReturnType("未检测到或者正在审核中","100");
        }else{
            String status = (String) map.get("yuliu2");//领取电子健康证0未领取1领取
            if ("0".equals(status)){
                return CommonReturnType.createCommonReturnType(map,"200");      //未领取
            }
            return CommonReturnType.createCommonReturnType(map,"300");          //已领取
        }
    }

    /**
     * @auther: lizhen
     * @date: 2019/7/16 15:03
     * 功能描述: 小程序，点击领取健康证
     */
    @RequestMapping(value = "lingquXCX")
    public CommonReturnType lingquXCX(Integer healthId){
        if (hearthCardService.lingquXCX(healthId)){
            return CommonReturnType.createCommonReturnType("领取成功","200");
        }
        return CommonReturnType.createCommonReturnType("领取失败","100");
    }


    /**
     * @auther: lizhen
     * @date: 2019/7/16 15:09
     * 功能描述: 健康证查询安卓端接口
     */
    @RequestMapping(value = "getHearthCard")
    public CommonReturnType getHearthCard(String idCard,String hospitalNum) throws Exception {

        if (StringUtils.isEmpty(idCard) || StringUtils.isEmpty(hospitalNum)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误");
        }
        //状态判断（审核中，合格，不合格）
        Map<String, Object> map = hearthCardService.getXCX(idCard);
        if (map.size()==0){
            return CommonReturnType.createCommonReturnType("未检测到该人员信息","100");
        }else{
            String hospital_id = (String) map.get("hospital_id");
            if (hospital_id.equals(hospitalNum)){                                                           //是否时同一家医院
                Map<String,Object> maps = new HashMap<>();
                maps.put("hearthCard",map);
                String status = (String) map.get("status");
                if ("0".equals(status)){
                    return CommonReturnType.createCommonReturnType(maps,"200");                      //合格
                }
                return CommonReturnType.createCommonReturnType(maps,"300");                          //不合格
            }
            return CommonReturnType.createCommonReturnType("未检测到该人员信息","100");
        }
    }


    /**
     * @auther: lizhen
     * @date: 2019/7/16 15:13
     * 功能描述: 健康证点击查看详情
     */
    @RequestMapping(value = "xiangqing")
    public CommonReturnType xiangqing(Integer healthId){
        Map<String, String> map = hearthCardService.xiangqing(healthId);
        return CommonReturnType.createCommonReturnType(map);
    }


    /**
     * @auther: lizhen
     * @date: 2019/7/16 15:33
     * 功能描述: 健康证打印状态列表，状态0未打印1已打印
     */
    @RequestMapping(value = "dayin")
    public CommonReturnType dayin(String printStatus, String hospitalNum,
                                  @RequestParam(defaultValue = "1")int page,
                                  @RequestParam(defaultValue = "10")int limit){
        Map<String,Object> map = new HashMap<>();
        List<HearthCardInfo> list = hearthCardService.dayin(hospitalNum,printStatus,page,limit);
        int i = hearthCardService.dayinCount(hospitalNum,printStatus);
        map.put("count",i);
        map.put("pageData",list);
        return CommonReturnType.createCommonReturnType(map);
    }

    /**
     * @auther: lizhen
     * @date: 2019/7/16 16:47
     * 功能描述: 不合格健康证列表
     */
    @RequestMapping(value = "healthCardNoPass")
    public CommonReturnType healthCardNoPass(String hospitalNum,
                                             @RequestParam(defaultValue = "1")int page,
                                             @RequestParam(defaultValue = "10")int limit){
        UserInfo currentUser = getCurrentUser(request);
        if (currentUser==null){
            return CommonReturnType.createCommonReturnType("登录过期！","250");
        }else{
            Map<String,Object> map = new HashMap<>();
            List<HearthCardInfo> list = hearthCardService.healthCardNoPass(hospitalNum, page,limit);
            int i = hearthCardService.healthCardNoPassCount(hospitalNum);
            map.put("count",i);
            map.put("pageData",list);
            return CommonReturnType.createCommonReturnType(map);
        }
    }


    /**
     * @auther: lizhen
     * @date: 2019/7/16 17:14
     * 功能描述: 健康证多条件查询
     */
    @RequestMapping(value = "keywordSelect")
    public CommonReturnType keywordSelect(String printStatus,String status,String name, String idCard, String dates,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int limit){
        Map<String,Object> map = new HashMap<>();
        List<HearthCardInfo> list = hearthCardService.keywordSelect(printStatus,status,name, idCard, dates,page,limit);
        int i = hearthCardService.keywordSelectCount(printStatus,status,name, idCard, dates);
        map.put("count",i);
        map.put("pageData",list);
        if (list.size()>0){
            return CommonReturnType.createCommonReturnType(map,"200");
        }
        return CommonReturnType.createCommonReturnType(map,"100");
    }



    /**
     * @auther: lizhen
     * @date: 2019/7/16 17:39
     * 功能描述: 修改打印状态
     */
    @RequestMapping(value = "xuanzheDY")
    public CommonReturnType xuanzheDY(String printStatus,String healthId){
        //判断打印状态
        if ("1".equals(printStatus)){
            logger.info("已经是打印状态");
            return null;
        }
        hearthCardService.xuanzheDY(healthId);
        return CommonReturnType.createCommonReturnType("success","200");
    }

    /**
     * @auther: lizhen
     * @date: 2019/7/17 9:14
     * 功能描述: 批量打印，修改打印状态
     */
    @RequestMapping(value = "piLiangDY")
    public CommonReturnType piLiangDY(String healthId){
        if (StringUtils.isEmpty(healthId)){
            return null;
        }
        if (hearthCardService.batchUpdateData(healthId)){
            return CommonReturnType.createCommonReturnType("success");
        }
        return CommonReturnType.createCommonReturnType("file");
    }
}
