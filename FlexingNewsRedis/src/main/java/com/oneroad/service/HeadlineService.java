package com.oneroad.service;

import com.oneroad.pojo.Headline;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oneroad.pojo.PortalVo;
import com.oneroad.utils.Result;
import org.springframework.transaction.annotation.Transactional;

/**
* @author ONEROAD
* @description 针对表【news_headline】的数据库操作Service
* @createDate 2024-04-13 10:55:44
*/
@Transactional(rollbackFor = RuntimeException.class)
public interface HeadlineService extends IService<Headline> {

    //分页查询头条信息
    Result findNewPage(PortalVo portalVo);

    //查询头条详情
    Result showHeadlineDetail(Integer hid);

    //头条发布
    Result publish(Headline headline, String token);

    //根据id查询详情，修改头条内容回显
    Result findHeadlineByHid(Integer hid);

    //更新头条内容
    Result updateHeadLine(Headline headline);
}
