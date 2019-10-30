package com.mdkproject.mdkshiro.base.config;


import com.mdkproject.mdkshiro.redisUtil.FastJson2JsonRedisSerializer;
import com.mdkproject.mdkshiro.redisUtil.RedisTemplate;
import com.mdkproject.mdkshiro.redisUtil.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @Auther: Liberal-World
 * @Date: 2019-06-17 14:33
 * @Description: 读取redis配置文件
 * @Version 1.0
 */
@Configuration
@PropertySource("classpath:redis.properties")
@Slf4j
public class RedisConfig {

    @Value("${redis.hostName}")     //#redis服务器地址
    private String hostName;

    @Value("${redis.password}")
    private String password;

    @Value("${redis.port}")
    private Integer port;

    @Value("${redis.maxIdle}")
    private Integer maxIdle;         //#连接池中的最大空闲连接（默认8）

    @Value("${redis.timeout}")
    private Integer timeout;         //#客户端超时时间单位是毫秒 默认是2000

    @Value("${redis.maxTotal}")
    private Integer maxTotal;        //#控制一个pool可分配多少个jedis实例,用来替换上面的redis.maxActive,如果是jedis 2.4以后用该属性

    @Value("${redis.maxWaitMillis}")
    private Integer maxWaitMillis;   //#最大建立连接等待时间。如果超过此时间将接到异常。设为-1表示无限制。

    @Value("${redis.minEvictableIdleTimeMillis}")
    private Integer minEvictableIdleTimeMillis;//#连接的最小空闲时间 默认1800000毫秒(30分钟)

    @Value("${redis.numTestsPerEvictionRun}")
    private Integer numTestsPerEvictionRun;//#每次释放连接的最大数目,默认3

    @Value("${redis.timeBetweenEvictionRunsMillis}")
    private Integer timeBetweenEvictionRunsMillis;//#逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1

    @Value("${redis.testOnBorrow}")
    private boolean testOnBorrow;//#是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个

    @Value("${redis.testWhileIdle}")
    private boolean testWhileIdle;//#在空闲时检查有效性, 默认false


    /**
     * jedis配置
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory(){
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(hostName);
        redisStandaloneConfiguration.setPort(port);
        //由于我们使用了动态配置库,所以此处省略
        //redisStandaloneConfiguration.setDatabase(database);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration =
                JedisClientConfiguration.builder();
        jedisClientConfiguration.connectTimeout(Duration.ofMillis(timeout));
        JedisConnectionFactory factory = new JedisConnectionFactory(redisStandaloneConfiguration,
                jedisClientConfiguration.build());
        return factory;
    }

    /**
     * 实例化RedisTemplate对象
     */
    @Bean
    public RedisTemplate functionDomainRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        log.info("RedisTemplate实例化成功！");
        RedisTemplate redisTemplate = new RedisTemplate();
        initDomainRedisTemplate(redisTemplate,redisConnectionFactory);
        return redisTemplate;
    }
    //设置数据存入 redis 的序列化方式,并开启事务
    private void initDomainRedisTemplate(RedisTemplate redisTemplate,RedisConnectionFactory factory){
        //如果不配置Serializer，那么存储的时候缺省使用String，如果用User类型存储，那么会提示错误User can't cast to String！
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(fastJson2JsonRedisSerializer());

        //开启事务
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setConnectionFactory(factory);
    }


    /**
     * 注入封装RedisTemplate
     */
    @Bean(name = "redisUtil")
    public RedisUtil redisUtil(RedisTemplate redisTemplate){
        log.info("RedisUtil注入成功");
        RedisUtil redisUtil = new RedisUtil();
        redisUtil.setRedisTemplate(redisTemplate);
        return redisUtil;
    }
    //引入自定义序列化
    //org.springframework.data.redis.serializer.RedisSerializer
    @Bean
    public RedisSerializer fastJson2JsonRedisSerializer(){
        return new FastJson2JsonRedisSerializer<Object>(Object.class);
    }

}
