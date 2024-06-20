package com.oneroad.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.oneroad.pojo.Headline;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oneroad.pojo.PortalVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
* @author ONEROAD
* @description 针对表【news_headline】的数据库操作Mapper
* @createDate 2024-04-13 10:55:44
* @Entity com.oneroad.pojo.Headline
*/
@Mapper
public interface HeadlineMapper extends BaseMapper<Headline> {

    //自定义方法 分页查询
    IPage<Map> selectPageMap(IPage<Map> page, PortalVo portalVo);

    //自定义方法 根据头条id查询内容
    Map selectDetailMap(Integer hid);
}




