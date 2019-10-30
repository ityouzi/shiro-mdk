package com.mdkproject.mdkshiro;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther: lizhen
 * @date: 2019-06-17 15:34
 * 功能描述: 添加继承 SpringBootServletInitializer，并重写configure方法
 */
@Slf4j
@SpringBootApplication(scanBasePackages = {"com.mdkproject.mdkshiro"})
@EnableCaching
@RestController
@MapperScan("com.mdkproject.mdkshiro.dao")
public class MdkShiroApplication extends SpringBootServletInitializer {


    public static void main(String[] args) {
        SpringApplication.run(MdkShiroApplication.class, args);
        log.info("MDK-----------启动成功！！！！永无BUG");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
        System.out.println("外部tomcat,chapter启动!");
        return application.sources(MdkShiroApplication.class);
    }




}
