package com.oneroad.initializer;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oneroad.mapper.HeadlineMapper;
import com.oneroad.pojo.PortalVo;
import com.oneroad.pojo.Type;
import com.oneroad.service.TypeService;
import com.oneroad.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CacheInitializer implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    HeadlineMapper headlineMapper;

    @Autowired
    RedisCache redisCache;

    @Autowired
    TypeService typeService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        //缓存头条分类
        List<Type> list = typeService.list();
        stringRedisTemplate.opsForValue().set("TypeList", JSONUtil.toJsonStr(list));
        //缓存每个类别中的热门头条
        PortalVo portalVo0=new PortalVo();
        portalVo0.setType(0);
        portalVo0.setKeyWords("");
        portalVo0.setPageNum(1);
        portalVo0.setPageSize(5);
        IPage<Map> page = new Page<>(portalVo0.getPageNum(),portalVo0.getPageSize());
        headlineMapper.selectPageMap(page, portalVo0);
        stringRedisTemplate.opsForValue().set("HeatPage0", JSONUtil.toJsonStr(page));

        PortalVo portalVo1=new PortalVo();
        portalVo1.setType(1);
        portalVo1.setKeyWords("");
        portalVo1.setPageNum(1);
        portalVo1.setPageSize(5);
        page = new Page<>(portalVo1.getPageNum(),portalVo1.getPageSize());
        headlineMapper.selectPageMap(page, portalVo1);
        stringRedisTemplate.opsForValue().set("HeatPage1", JSONUtil.toJsonStr(page));

        PortalVo portalVo2=new PortalVo();
        portalVo2.setType(2);
        portalVo2.setKeyWords("");
        portalVo2.setPageNum(1);
        portalVo2.setPageSize(5);
        page = new Page<>(portalVo2.getPageNum(),portalVo2.getPageSize());
        headlineMapper.selectPageMap(page, portalVo2);
        stringRedisTemplate.opsForValue().set("HeatPage2", JSONUtil.toJsonStr(page));

        PortalVo portalVo3=new PortalVo();
        portalVo3.setType(3);
        portalVo3.setKeyWords("");
        portalVo3.setPageNum(1);
        portalVo3.setPageSize(5);
        page = new Page<>(portalVo3.getPageNum(),portalVo3.getPageSize());
        headlineMapper.selectPageMap(page, portalVo3);
        stringRedisTemplate.opsForValue().set("HeatPage3", JSONUtil.toJsonStr(page));

        PortalVo portalVo4=new PortalVo();
        portalVo4.setType(4);
        portalVo4.setKeyWords("");
        portalVo4.setPageNum(1);
        portalVo4.setPageSize(5);
        page = new Page<>(portalVo4.getPageNum(),portalVo4.getPageSize());
        headlineMapper.selectPageMap(page, portalVo4);
        stringRedisTemplate.opsForValue().set("HeatPage4", JSONUtil.toJsonStr(page));

        PortalVo portalVo5=new PortalVo();
        portalVo5.setType(5);
        portalVo5.setKeyWords("");
        portalVo5.setPageNum(1);
        portalVo5.setPageSize(5);
        page = new Page<>(portalVo5.getPageNum(),portalVo5.getPageSize());
        headlineMapper.selectPageMap(page, portalVo5);
        stringRedisTemplate.opsForValue().set("HeatPage5", JSONUtil.toJsonStr(page));



    }
}
