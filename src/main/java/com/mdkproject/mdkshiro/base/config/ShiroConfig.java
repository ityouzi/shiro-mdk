package com.mdkproject.mdkshiro.base.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Auther: Liberal-World
 * @Date: 2019-06-17 14:58
 * @Description: 这里需要配置shiro的过滤规则
 * Apache Shiro 核心通过 Filter 来实现，就好像SpringMvc 通过DispachServlet 来主控制一样。
 * 既然是使用 Filter 一般也就能猜到，是通过URL规则来进行过滤和权限校验，所以我们需要定义一系列关于URL的规则和访问权限。
 * @Version 1.0
 */
@Slf4j
@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        log.info("ShiroConfiguration.shirFilter()-------------------启动成功");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        filterChainDefinitionMap.put("logout","logout");

        //过滤链定义：从上向下顺序执行，一般将/**放在最为下边，这是一个坑，一不小心代码就不好使了;
        //authc:所有url都必须认证通过才可以访问
        //anon:所有url都可以匿名访问
        filterChainDefinitionMap.put("/static/templates/**","authc");


        //如果不设置默认会自动寻找web工程根目录下的login.jsp页面
        //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        shiroFilterFactoryBean.setLoginUrl("/login/loginUser");

        //未授权界面
        shiroFilterFactoryBean.setUnauthorizedUrl("/login/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    /**
     * 凭证匹配器
     * 由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");   //散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2);          //散列的次数，比如散列两次，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }

    /**
     * shiro 用户数据注入
     */
    @Bean
    public ShiroRealm myShiroRealm(){
        ShiroRealm shiroRealm = new ShiroRealm();
        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return shiroRealm;
    }




    /**
     * @auther: lizhen
     * @date: 2019-06-17 15:19
     * 功能描述: 开启shiro aop注解支持,使用代理方式;所以需要开启代码支持;
     * 一定要写入上面advisorAutoProxyCreator（）自动代理。不然AOP注解不会生效
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor ass = new AuthorizationAttributeSourceAdvisor();
        ass.setSecurityManager(securityManager);
        return ass;
    }


    /**
     * 安全管理控制
     */
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        //自定义缓存实现
        securityManager.setCacheManager(ehCacheManager());
        //自定义session实现
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    /**
     * 开启缓存
     * shiro-ehcache实现
     */
    @Bean
    public EhCacheManager ehCacheManager(){
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        log.info("EhCacheManager-------------开启缓存");
        return ehCacheManager;
    }

    /**
     * @auther: lizhen
     * @date: 2019-06-25 15:06
     * 功能描述: 自定义的SessionManager
     */
    @Bean
    public SessionManager sessionManager(){
        ShiroSessionManager shiroSessionManager = new ShiroSessionManager();
        //这里可以不设置。Shiro有默认的session管理。如果缓存为Redis则需改用Redis的管理

        shiroSessionManager.setSessionDAO(new EnterpriseCacheSessionDAO());
        shiroSessionManager.setGlobalSessionTimeout(1800000L);                //30分钟
//        shiroSessionManager.getGlobalSessionTimeout();                        //30
//        shiroSessionManager.setGlobalSessionTimeout(30 * MILLIS_PER_MINUTE);    //30
        log.info("SessionManager-----------注入成功");
        return shiroSessionManager;

    }



//
//    @Bean
//    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
//        return new RestTemplate(factory);
//    }
//
//    @Bean
//    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
//        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//        return factory;
//    }


}
