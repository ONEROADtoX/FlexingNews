package com.oneroad.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oneroad.pojo.Type;
import com.oneroad.service.TypeService;
import com.oneroad.mapper.TypeMapper;
import com.oneroad.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author ONEROAD
* @description 针对表【news_type】的数据库操作Service实现
* @createDate 2024-04-13 10:55:44
*/
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type>
    implements TypeService{

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result findAllTypes() {
        //直接调用业务层,查询全部数据
        List<Type> list = null;
        //查缓存
        String json = stringRedisTemplate.opsForValue().get("TypeList");
        //判断缓存是否存在
        if(StrUtil.isNotBlank(json)){
            System.out.println("使用到了类型缓存");
            JSONArray objects = JSONUtil.parseArray(json);
            list = JSONUtil.toList(objects, Type.class);
        }
        else{
            list = this.list();
            stringRedisTemplate.opsForValue().set("TypeList",JSONUtil.toJsonStr(list));
        }
        return  Result.ok(list);
    }
}




