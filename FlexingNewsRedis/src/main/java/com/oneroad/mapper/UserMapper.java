package com.oneroad.mapper;

import com.oneroad.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author ONEROAD
* @description 针对表【news_user】的数据库操作Mapper
* @createDate 2024-04-13 10:55:44
* @Entity com.oneroad.pojo.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




