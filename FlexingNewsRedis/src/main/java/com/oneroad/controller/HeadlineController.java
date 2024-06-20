package com.oneroad.controller;

import com.oneroad.pojo.Headline;
import com.oneroad.service.HeadlineService;
import com.oneroad.utils.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("headline")
@CrossOrigin
@Tag(name="头条模块")
public class HeadlineController {

    @Autowired
    private HeadlineService headlineService;

    /**
     * 实现步骤:
     *   1. token获取userId [无需校验,拦截器会校验]
     *   2. 封装headline数据
     *   3. 插入数据即可
     */
    @PostMapping("publish")
    public Result publish(@RequestBody Headline headline, @RequestHeader String token){
        Result result = headlineService.publish(headline,token);
        return result;
    }

    //根据id查询详情，修改头条内容回显
    @PostMapping("findHeadlineByHid")
    public Result findHeadlineByHid(Integer hid){
        Result result = headlineService.findHeadlineByHid(hid);
        return result;
    }

    //更新头条内容
    @PostMapping("update")
    public Result update(@RequestBody Headline headline){
        Result result = headlineService.updateHeadLine(headline);
        return result;
    }

    //删除头条内容
    @PostMapping("removeByHid")
    public Result removeById(Integer hid){
        //本质不删数据，只将 is_deleted 改为1
        headlineService.removeById(hid);
        return Result.ok(null);
    }

}
