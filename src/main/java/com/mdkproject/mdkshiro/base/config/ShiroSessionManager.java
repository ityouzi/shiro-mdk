package com.mdkproject.mdkshiro.base.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 *  传统结构项目中，shiro从cookie中读取sessionId以此来维持会话，
 * 在前后端分离的项目中（也可在移动APP项目使用），我们选择在ajax的请求头中传递sessionId，
 * 因此需要重写shiro获取sessionId的方式。
 * 自定义ShiroSessionManager类继承DefaultWebSessionManager类，重写getSessionId方法，
 */

/**
 * @Auther: Liberal-World
 * @Date: 2019-06-25 14:55
 * @Description:shiro框架 自定义session获取方式,可自定义session获取规则。这里采用ajax请求头authToken携带sessionId的方式
 * @Version 1.0
 */
@Slf4j
public class ShiroSessionManager extends DefaultWebSessionManager {
    //授权标记
    private static final String TOKEN = "token";
    //无状态请求
    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";


    public ShiroSessionManager(){
        super();
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response){
        String id = WebUtils.toHttp(request).getParameter(TOKEN);
        log.info("token:"+id);
        if (StringUtils.isEmpty(id)){
            //如果没有携带id参数则按照父类的方式在cookie进行获取
            log.info("super："+super.getSessionId(request, response));
            return super.getSessionId(request,response);
        }
        //如果请求头中有 authToken 则其值为sessionId
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,REFERENCED_SESSION_ID_SOURCE);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID,id);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID,Boolean.TRUE);
        return id;
    }

}
