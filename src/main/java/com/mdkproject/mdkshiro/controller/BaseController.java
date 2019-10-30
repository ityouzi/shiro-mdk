package com.mdkproject.mdkshiro.controller;

import com.mdkproject.mdkshiro.entity.SysPermission;
import com.mdkproject.mdkshiro.entity.UserInfo;
import com.mdkproject.mdkshiro.error.BusinessException;
import com.mdkproject.mdkshiro.error.EmBusinessError;
import com.mdkproject.mdkshiro.response.CommonReturnType;
import com.mdkproject.mdkshiro.service.SysRoleService;
import com.mdkproject.mdkshiro.utils.SortList;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.UnknownSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.*;

/**
 * @Auther: Liberal-World
 * @Date: 2019-06-17 11:22
 * @Description:  统一日志和统一返回格式处理,用来提供日志接口以及返回model的固定参数格式
 * @Version 1.0
 */
public abstract class BaseController{


    @Autowired
    private EhCacheManager ehCacheManager;
    @Autowired
    private SysRoleService sysRoleService;


    //日志
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * @auther: lizhen
     * @date: 2019-06-18 9:45
     * 功能描述: 定义exceptionhandler解决未被controller层吸收的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, HttpServletResponse httpServletResponse,Object handler, Exception ex){
        Map<String,Object> responseData = new HashMap<>();
        logger.info(ex.getMessage());
        if (ex instanceof BusinessException){                                           //自定义异常
            BusinessException businessException = (BusinessException) ex;
            responseData.put("errCode",businessException.getErrCode());
            responseData.put("errMsg",businessException.getErrMsg());
        }else if (ex instanceof UnauthorizedException){                                 //没有权限
            responseData.put("errCode", EmBusinessError.ROLE_NOT_MANAGE.getErrCode());
            responseData.put("errMsg", EmBusinessError.ROLE_NOT_MANAGE.getErrMsg());
        }else if (ex instanceof UnknownSessionException) {                              //session过期
            responseData.put("errCode", EmBusinessError.SESSION_NOT_EXIT.getErrCode());
            responseData.put("errMsg", EmBusinessError.SESSION_NOT_EXIT.getErrMsg());
        }else if(ex instanceof UnauthenticatedException) {                               //token认证过期
            responseData.put("errCode", EmBusinessError.SESSION_NOT_EXIT.getErrCode());
            responseData.put("errMsg", EmBusinessError.SESSION_NOT_EXIT.getErrMsg());
        }else if (ex instanceof UndeclaredThrowableException){
            responseData.put("errCode",EmBusinessError.REPEATED_REQ.getErrCode());
            responseData.put("errMsg",EmBusinessError.REPEATED_REQ.getErrMsg());
        }else {                                                                         //服务器异常
            responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg", EmBusinessError.UNKNOWN_ERROR.getErrMsg());
        }
        return CommonReturnType.createCommonReturnType(responseData,"fail");
    }


    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String getUuid(){
        String uuid = UUID.randomUUID().toString();//获取UUID并转化为String对象
        uuid = uuid.replace("-","");
        return uuid;
    }



    /**
     * @auther: lizhen
     * @date: 2019-06-20 17:32
     * 功能描述: 获取当前登陆用户信息
     */

    public UserInfo getCurrentUser(HttpServletRequest request){
        String token = request.getParameter("token");
        if (token==null){
            return null;
        }else{
            Cache<Object, Object> cache = ehCacheManager.getCache("data-cache");
            UserInfo ui = (UserInfo) cache.get(token);
            if (ui==null){
                return null;
            }
            return ui;
        }
    }

    /**
     * @auther: lizhen
     * @date: 2019-06-21 11:02
     * 功能描述: 清除token缓存对象
     */
    public String deleteToken(String token){
        //非空判断
        if (StringUtils.isEmpty(token)){
            return "0";//没有token
        }else {
            Cache<Object, Object> cache = ehCacheManager.getCache("data-cache");
            UserInfo userInfo = (UserInfo) cache.get(token);
            if (userInfo==null){
                logger.info("清除缓存成功："+token);
            }
            cache.remove(token);
            return "1";//清除缓存成功
        }
    }


    /**
     * 资源叔&获取当前登录用户的权限
     */
    public JSONArray getCurrentUserPermission(UserInfo ui){
        List<SysPermission> allList = sysRoleService.findByUid(ui.getUid());
        System.out.println("------处理资源树前--------------:"+allList.size());
        List<SysPermission> newAllList = new ArrayList<>();
        for (SysPermission sysPermission : allList){
            if (sysPermission.getAvailable()){
                //去除已经禁止的资源(不执行添加功能)
                continue;
            }
            if (sysPermission.getResourceType().equals("menu")){
                if (!newAllList.contains(sysPermission)){
                    newAllList.add(sysPermission);
                }
            }else {
                if ("roleInfo/getAllData".equals( sysPermission.getUrl() )){
                    SecurityUtils.getSubject().getSession().setAttribute("isSearchAllData",true);
                }
            }
        }
        System.out.println("------处理剩余结果后--------------"+newAllList.size());

        //进行list对象某属性排序
        SortList<SysPermission> sortList = new SortList<>();
        sortList.Sort(newAllList,"getNumberSort");
        JsonConfig config = new JsonConfig();
        config.setExcludes(new String[]{"roles"});
        JSONArray jsonArray = JSONArray.fromObject(newAllList, config);
        return jsonArray;
    }





}
