package com.mdkproject.mdkshiro.base.aop;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mdkproject.mdkshiro.controller.BaseController;
import com.mdkproject.mdkshiro.error.BusinessException;
import com.mdkproject.mdkshiro.error.EmBusinessError;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @program: mdk-shiro->RepeatSubmitAspect
 * @description:
 * @author: lizhen
 * @create: 2019-08-05 13:36
 **/


@Aspect
@Configuration
@Slf4j
public class RepeatSubmitAspect extends BaseController {

    private static final Cache<String,Object> caches = CacheBuilder.newBuilder()
            //最大缓存100个
            .maximumSize(100)
            //设置缓存过期时间为5s
            .expireAfterWrite(20, TimeUnit.SECONDS)
            .build();


    @Around("execution(public * *(..)) && @annotation(com.mdkproject.mdkshiro.base.aop.NoRepeatSumit)")
    public Object interceptor(ProceedingJoinPoint pjp) throws BusinessException {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        NoRepeatSumit noRepeatSumit = method.getAnnotation(NoRepeatSumit.class);
        String key = getCacheKey(noRepeatSumit, pjp.getArgs());
        if (!StringUtils.isEmpty(key)){
            if (caches.getIfPresent(key) != null){
                throw new BusinessException(EmBusinessError.REPEATED_REQ);

            }
            //如果是第一次请求，就将key存入缓存中
            caches.put(key,key);
        }
        try {
            return pjp.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
        }
    }






    /**
     * Cache key生成策略，这里可以自定义实现，比如再Submit注解中添加需要从request中获取字段名称，在此方法中通过反射获取，拼接为最终的Cache key
     * 本方法Cache key使用最简单的策略：prefix + request参数的toString，这里只做展示使用，一般不会使用这种策略，一是会导致cache key过长，浪费存储空间，
     * 二是，如果请求参数没有实现toString方法，对于相同的请求参数，依然会被认为是两个不同的请求
     */
    private String getCacheKey(NoRepeatSumit noRepeatSumit, Object[] args) {
        String prefix = noRepeatSumit.prefix();
        return prefix + args[0];
    }


}


