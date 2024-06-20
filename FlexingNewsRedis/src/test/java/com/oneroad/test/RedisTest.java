package com.oneroad.test;


import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisTest {
    @Autowired
    private StringRedisTemplate stringredisTemplate;

    @Test
    void redistest(){
        stringredisTemplate.opsForValue().set("name","lisi");
        Object name = stringredisTemplate.opsForValue().get("name");
        System.out.println("name:"+name);
    }

}
