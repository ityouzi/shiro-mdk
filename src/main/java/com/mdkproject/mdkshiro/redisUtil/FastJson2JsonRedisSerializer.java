package com.mdkproject.mdkshiro.redisUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * @Auther: Liberal-World
 * @Date: 2019-04-14 20:35
 * @Description:自定义序列化
 * @Version 1.0
 */

//接下来还需要一个序列化操作类，帮助我们完成redis值的序列化，
// 添加FastJson2JsonRedisSerializer.java，实现RedisSerializer接口，实现其中的序列化和反序列化方法。
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private Class<T> clazz;

    public FastJson2JsonRedisSerializer(Class<T> clazz){
        super();
        this.clazz = clazz;
    }



    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t==null){
            return new byte[0];
        }
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);

    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes==null || bytes.length <= 0){
            return null;
        }
        String str = new String(bytes,DEFAULT_CHARSET);
        return JSON.parseObject(str,clazz);
    }
}
