package com.oneroad.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oneroad.pojo.Headline;
import com.oneroad.pojo.PortalVo;
import com.oneroad.pojo.Type;
import com.oneroad.pojo.User;
import com.oneroad.service.HeadlineService;
import com.oneroad.mapper.HeadlineMapper;
import com.oneroad.utils.JwtHelper;
import com.oneroad.utils.RedisCache;
import com.oneroad.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* @author ONEROAD
* @description 针对表【news_headline】的数据库操作Service实现
* @createDate 2024-04-13 10:55:44
*/
@Service
public class HeadlineServiceImpl extends ServiceImpl<HeadlineMapper, Headline>
    implements HeadlineService{

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private HeadlineMapper headlineMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //分页查询头条信息
    @Override
    public Result findNewPage(PortalVo portalVo) {
        //分页参数,(page,pageSize)
        IPage<Map> page = new Page<>(portalVo.getPageNum(),portalVo.getPageSize());

        //分页查询
        //若为热门头条，从缓存中取
        if(portalVo.getKeyWords()==""
                &&portalVo.getPageSize()==5
                &&portalVo.getPageNum()==1){
            System.out.println("使用到了热门缓存: "+"HeatPage"+portalVo.getType());
            String json = stringRedisTemplate.opsForValue().get("HeatPage"+portalVo.getType());
            page=JSONUtil.toBean(json, Page.class);
        }
        else{
            headlineMapper.selectPageMap(page, portalVo);
        }


        //结果封装
        //分页数据封装
        Map<String,Object> pageInfo =new HashMap<>();
        pageInfo.put("pageData",page.getRecords());
        pageInfo.put("pageNum",page.getCurrent());
        pageInfo.put("pageSize",page.getSize());
        pageInfo.put("totalPage",page.getPages());
        pageInfo.put("totalSize",page.getTotal());

        Map<String,Object> pageInfoMap=new HashMap<>();
        pageInfoMap.put("pageInfo",pageInfo);
        // 响应JSON
        return Result.ok(pageInfoMap);
    }

    /**
     * 头条详情查询
     * "headline":{
     * "hid":"1",                     // 新闻id
     * "title":"马斯克宣布 ... ...",   // 新闻标题
     * "article":"... ..."            // 新闻正文
     * "type":"1",                    // 新闻所属类别编号
     * "typeName":"科技",             // 新闻所属类别
     * "pageViews":"40",              // 新闻浏览量
     * "pastHours":"3" ,              // 发布时间已过小时数
     * "publisher":"1" ,              // 发布用户ID
     * "author":"张三"                 // 新闻作者
     * }
     * 注意: 是多表查询 , 需要更新浏览量+1
     *
     * @param hid
     * @return
     */
    @Override
    public Result showHeadlineDetail(Integer hid) {

        //1.实现根据id的查询(多表
//        Map headLineDetail = headlineMapper.selectDetailMap(hid);
        Map headLineDetail =redisCache.queryWithPassThrouth("Headline"+hid,hid,Map.class,headlineMapper::selectDetailMap,60L);


        //2.拼接头条对象(阅读量和version)进行数据更新
        Headline headline = new Headline();
        headline.setHid(hid);
        headline.setPageViews((Integer) headLineDetail.get("pageViews")+1); //阅读量+1
        headline.setVersion((Integer) headLineDetail.get("version")); //设置版本
        headlineMapper.updateById(headline);

        //3.返回响应体
        Map<String,Object> pageInfoMap=new HashMap<>();
        pageInfoMap.put("headline",headLineDetail);
        return Result.ok(pageInfoMap);
    }

    /**
     * 发布数据
     * @param headline
     * @return
     */
    @Override
    public Result publish(Headline headline, String token) {
        int userId = jwtHelper.getUserId(token).intValue();
        headline.setPublisher(userId);
        headline.setCreateTime(new Date());
        headline.setUpdateTime(new Date());
        headline.setPageViews(0);
        headlineMapper.insert(headline);
        return Result.ok(null);
    }

    /**
     * 根据id查询详情，修改头条内容回显
     * @param hid
     * @return
     */
    @Override
    public Result findHeadlineByHid(Integer hid) {
//        Headline headline = headlineMapper.selectById(hid);
        Headline headline =redisCache.queryWithPassThrouth("Headline"+hid,hid,Headline.class,headlineMapper::selectById,60L);
        Map<String,Object> pageInfoMap=new HashMap<>();
        pageInfoMap.put("headline",headline);
        return Result.ok(pageInfoMap);
    }

    /**
     * 更新头条内容
     * 1.查询version版本
     * 2.补全属性,修改时间 , 版本!
     *
     * @param headline
     * @return
     */
    @Override
    public Result updateHeadLine(Headline headline) {

        //读取版本
        Integer version = headlineMapper.selectById(headline.getHid()).getVersion();
//        Integer version =redisCache.query("Headline"+headline.getHid(),headline.getHid(),Headline.class,headlineMapper::selectById,60L).getVersion();
        headline.setVersion(version);
        headline.setUpdateTime(new Date());

        headlineMapper.updateById(headline);
        //更新后删除该条旧缓存
        redisCache.delete("Headline"+headline.getHid());
        //更新整体的热门缓存
        redisCache.delete("HeatPage0");
        PortalVo portalVo0=new PortalVo();
        portalVo0.setType(0);
        portalVo0.setKeyWords("");
        portalVo0.setPageNum(1);
        portalVo0.setPageSize(5);
        IPage<Map> page = new Page<>(portalVo0.getPageNum(),portalVo0.getPageSize());
        headlineMapper.selectPageMap(page, portalVo0);
        stringRedisTemplate.opsForValue().set("HeatPage0", JSONUtil.toJsonStr(page));
        //更新对应类型的热门缓存
        redisCache.delete("HeatPage"+headline.getType());
        PortalVo portalVo1=new PortalVo();
        portalVo1.setType(headline.getType());
        portalVo1.setKeyWords("");
        portalVo1.setPageNum(1);
        portalVo1.setPageSize(5);
        page = new Page<>(portalVo1.getPageNum(),portalVo1.getPageSize());
        headlineMapper.selectPageMap(page, portalVo1);
        stringRedisTemplate.opsForValue().set("HeatPage"+headline.getType(), JSONUtil.toJsonStr(page));

        return Result.ok(null);
    }

}




