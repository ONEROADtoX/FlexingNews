package com.oneroad.utils;

import cn.hutool.json.JSONUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class RedisCache {
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate redisTemplate;

    public RedisCache(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisTemplate = stringRedisTemplate;
    }

    //存数据，默认以分钟为单位
    public void set(String key,Object value, Long time){
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), time, TimeUnit.MINUTES);
    }
    public <R,ID> R query(String key, ID parameter, Class<R> type, Function<ID,R> myFun, Long time){
        //parameter表示用于配合myFun进行查询的参数
        //查缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        //判断缓存是否存在
        if(JSONUtil.isTypeJSON(json)){
            //存在，直接返回
            return JSONUtil.toBean(json,type);
        }
        else if(json==null){
            //不存在则查询
            R r = myFun.apply(parameter);
            if(r==null){
                return null;
            }
            else{
                //MySQL中存在，Redis缓存具体值
                this.set(key,JSONUtil.toJsonStr(r),60L);
                return r;
            }
        }
        //缓存查询结果为""，返回空
        return null;
    }
    public <R,ID> R queryWithPassThrouth(String key, ID parameter, Class<R> type, Function<ID,R> myFun, Long time){
        //parameter表示用于配合myFun进行查询的参数
        //查缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        //判断缓存是否存在
        if(JSONUtil.isTypeJSON(json)){
            //存在，直接返回
            return JSONUtil.toBean(json,type);
        }
        else if(json==null){
            //不存在则查询
            R r = myFun.apply(parameter);
            if(r==null){
                //MySQL中不存在，Redis缓存空值，防止缓存穿透
                stringRedisTemplate.opsForValue().set(key,"",60,TimeUnit.MINUTES);
                return null;
            }
            else{
                //MySQL中存在，Redis缓存具体值
                this.set(key,JSONUtil.toJsonStr(r),60L);
                return r;
            }
        }
        //缓存查询结果为""，返回空
        return null;
    }
    public void delete(String key){
        try{
            redisTemplate.delete(key);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
