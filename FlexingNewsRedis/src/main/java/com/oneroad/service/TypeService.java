package com.oneroad.service;

import com.oneroad.pojo.Type;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oneroad.utils.Result;
import org.springframework.transaction.annotation.Transactional;

/**
* @author ONEROAD
* @description 针对表【news_type】的数据库操作Service
* @createDate 2024-04-13 10:55:44
*/
@Transactional(rollbackFor = RuntimeException.class)
public interface TypeService extends IService<Type> {
    Result findAllTypes();
}
