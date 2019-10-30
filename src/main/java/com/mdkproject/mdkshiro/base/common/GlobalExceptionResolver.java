package com.mdkproject.mdkshiro.base.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.mdkproject.mdkshiro.response.CommonReturnType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Auther: Liberal-World
 * @Date: 2019-06-25 17:20
 * @Description: 全局异常处理
 * @Version 1.0
 */
@Slf4j
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception ex) {
        ModelAndView mv;
        //进行异常判断。如果捕获异常请求跳转。
        if(ex instanceof UnauthorizedException){
            mv = new ModelAndView("/login/403");            //没有权限
            return mv;
        }else {
            mv = new ModelAndView();
            FastJsonJsonView view = new FastJsonJsonView();
            CommonReturnType result = new CommonReturnType();
            result.setData("服务器异常");
            ex.printStackTrace();
            log.error(ExceptionUtils.getFullStackTrace(ex));
            Map<String,Object> map;
            String beanString = JSON.toJSONString(result);
            map = JSON.parseObject(beanString,Map.class);
            view.setAttributesMap(map);
            mv.setView(view);
            return mv;

        }

    }
}
