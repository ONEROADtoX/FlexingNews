package com.oneroad.service;

import com.oneroad.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oneroad.utils.Result;
import org.springframework.transaction.annotation.Transactional;

/**
* @author ONEROAD
* @description 针对表【news_user】的数据库操作Service
* @createDate 2024-04-13 10:55:44
*/
@Transactional(rollbackFor = RuntimeException.class)
public interface UserService extends IService<User> {

    Result login(User user);

    Result getUserInfo(String token);

    Result checkUserName(String username);

    Result regist(User user);
}
