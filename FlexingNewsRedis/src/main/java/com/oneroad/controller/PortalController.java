package com.oneroad.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oneroad.pojo.PortalVo;
import com.oneroad.pojo.Type;
import com.oneroad.service.HeadlineService;
import com.oneroad.service.TypeService;
import com.oneroad.utils.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("portal")
@CrossOrigin
@Tag(name="首页模块")
public class PortalController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private HeadlineService headlineService;





    /**
     * 查询全部类别信息，用MyBatis-Plus在Service层直接查询数据库
     * @return
     */
    @GetMapping("findAllTypes")
    public Result findAllTypes(){
        Result result=typeService.findAllTypes();
        return  result;
    }

    /**
     * 首页分页查询，json
     * @return
     */
    @PostMapping("findNewsPage")
    public Result findNewPage(@RequestBody PortalVo portalVo){
        Result result = headlineService.findNewPage(portalVo);
        return result;
    }

    /**
     * 查询头条详情
     * @param hid
     * @return
     */
    @PostMapping("showHeadlineDetail")
    public Result showHeadlineDetail(Integer hid){
        Result result = headlineService.showHeadlineDetail(hid);
        return result;
    }

}