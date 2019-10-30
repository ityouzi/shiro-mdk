package com.mdkproject.mdkshiro.controller;

import com.mdkproject.mdkshiro.entity.Area;
import com.mdkproject.mdkshiro.entity.HospitalInfo;
import com.mdkproject.mdkshiro.entity.UserInfo;
import com.mdkproject.mdkshiro.error.BusinessException;
import com.mdkproject.mdkshiro.error.EmBusinessError;
import com.mdkproject.mdkshiro.modules.service.AreaService;
import com.mdkproject.mdkshiro.modules.service.HospitalService;
import com.mdkproject.mdkshiro.response.CommonReturnType;
import com.mdkproject.mdkshiro.service.UserInfoService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Auther: Liberal-World
 * @Date: 2019-06-18 13:38
 * @Description: 用户登录
 * @Version 1.0
 */
@Controller
@RequestMapping("/login")
@CrossOrigin(origins = "*",allowCredentials = "true")
public class LoginController extends BaseController{
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    EhCacheManager ehCacheManager;
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private UserInfoService userInfoService;





    /**
     * @auther: lizhen
     * @date: 2019-06-18 13:47
     * 功能描述: 用户登录&账号验证
     */
    @RequestMapping(value = "loginUser",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public CommonReturnType loginUser(HttpServletRequest request, String account,String password,String key) throws BusinessException {



        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //判断账号是否存在
        String keys = userInfoService.selectByAccount(account);
        if (keys==null){
            return CommonReturnType.createCommonReturnType("账号不存在","404");
        }
        if (!key.equals(keys)){
            return CommonReturnType.createCommonReturnType("没有权限","400");
        }else{
            //redis中的key是否存在
//        if (!redisTemplate.hasKey(telphone)){
//            throw new BusinessException(EmBusinessError.CODE_LOSE);
//        }
//        //获取redis缓存中的手机号key和验证码value
//        String code = redisTemplate.opsForValue().get(telphone);
//        if (code==null || "".equals(code)){
//            throw new BusinessException(EmBusinessError.CODE_LOSE);
//        }
//        //验证手机号和对应的验证码相符合
//        if (!StringUtils.equals(otpCode,code)){
//            throw new BusinessException(EmBusinessError.CODE_ERROR);
//        }
            //进行账号密码验证
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(account,password);
            //我们可以通过SecurityUtils工具类，获取当前的用户，然后通过调用login方法提交认证
            Subject subject = SecurityUtils.getSubject();
            System.out.println(subject.getSession().getId()+"==================================");
            Cache<Object, Object> cache = ehCacheManager.getCache("data-cache");
            Map<String,Object> map = new HashedMap();
            try {
                //提交实体/凭据信息/完成登录
                subject.login(usernamePasswordToken);
                //获取用户信息。但是它并非是完全认证通过的用户，当你访问需要认证用户的功能时，你仍然需要重新提交认证信息。
                UserInfo userInfo = (UserInfo) subject.getPrincipal();
                String hospitalNum = userInfo.getYuliu3();      //医院编号

                String token = request.getSession().getId();    //sessionId
                Object o = cache.get(userInfo.getUid());            //获取上一个人登录的token
                Object o1 = cache.get(o);
                if (o1==null){
                    cache.put(token,userInfo);                      //token-->userInfo
                    cache.put(userInfo.getUid(),token);             //userId-->token
                }else{
                    cache.remove(o);
                    cache.put(token,userInfo);                      //token-->userInfo
                    cache.put(userInfo.getUid(),token);             //userId-->token
                }
                map.put("token",token);
                map.put("account",account);
                map.put("uid",userInfo.getUid());
                HospitalInfo hospitalInfo = hospitalService.selectByHospitalNum(hospitalNum);
                if (hospitalInfo==null){
                    Area area = areaService.selectByAreaNum(userInfo.getYuliu4());  //区域编号
                    map.put("hospitalName",area.getAreaCity()+area.getAreaDistrict());                      //医院名称
                    map.put("hospitalNum","");                                      //医院编号
                    map.put("areaNum",area.getAreaNum());                               //区域编号
                    map.put("name",userInfo.getName());
                    map.put("nick",area.getYuliu1());                                   //简称
                    map.put("status",userInfo.getStatus());
                    map.put("telphone",userInfo.getTelphone());
                    map.put("sex",userInfo.getSex());
                    return CommonReturnType.createCommonReturnType(map,"200");    //返回用户名token
                }else{
                    Area area = areaService.selectByAreaNum(hospitalInfo.getAreaNum());
                    map.put("hospitalNum",hospitalInfo.getHospitalNum());               //医院编号
                    map.put("areaNum",area.getAreaNum());                               //区域编号
                    map.put("hospitalName",hospitalInfo.getHospitalName());             //医院名称
                    map.put("name",userInfo.getName());
                    map.put("nick",area.getYuliu1());                                   //简称
                    map.put("status",userInfo.getStatus());
                    map.put("telphone",userInfo.getTelphone());
                    map.put("sex",userInfo.getSex());
                    map.put("template",hospitalInfo.getYuliu1());                       //医院模板
                    return CommonReturnType.createCommonReturnType(map,"200");    //返回用户名token
                }

            } catch (UnknownAccountException uae) {
                logger.info("对用户[" + account + "]进行登录验证..验证未通过,未知账户");
                return CommonReturnType.createCommonReturnType("未知账户","100");
            } catch (IncorrectCredentialsException ice){
                logger.info("对用户[" + account + "]进行登录验证..验证未通过,错误的凭证");
                return CommonReturnType.createCommonReturnType("密码不正确","100");
            } catch (LockedAccountException lae){
                logger.info("对用户[" + account + "]进行登录验证..验证未通过,账户已锁定");
                return CommonReturnType.createCommonReturnType("账户已锁定","100");//返回登录页面
            } catch (ExcessiveAttemptsException eae){
                logger.info("对用户[" + account + "]进行登录验证..验证未通过,错误次数过多");
                return CommonReturnType.createCommonReturnType("用户名或密码错误次数过多","100");//返回登录页面
            } catch (AuthenticationException ae){
                //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
                logger.info("对用户[" + account + "]进行登录验证..验证未通过,堆栈轨迹如下");
                ae.printStackTrace();
                return CommonReturnType.createCommonReturnType("用户名或密码不正确","100");//返回登录页面
            }
        }

    }



    /**
     * @auther: lizhen
     * @date: 2019-06-18 14:39
     * 功能描述: 退出登录，清除缓存
     */
    @RequestMapping(value = "logOut",method = RequestMethod.POST,consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public CommonReturnType logOut(String token){
        //登出操作可以通过调用subject.logout()来删除你的登录信息
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        String s = deleteToken(token);//清除缓存
        logger.info("清除缓存："+s);
        return CommonReturnType.createCommonReturnType("退出登录","200");
    }

    /**
     * @auther: lizhen
     * @date: 2019-06-18 14:47
     * 功能描述: 没有权限403
     */
    @RequestMapping(value = "403")
    public CommonReturnType unauthorizedRole(){
        logger.info("-----------没有权限----------");
        return CommonReturnType.createCommonReturnType("没有操作权限","150");
    }


}
