package com.oneroad.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oneroad.pojo.User;
import com.oneroad.service.UserService;
import com.oneroad.mapper.UserMapper;
import com.oneroad.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
* @author ONEROAD
* @description 针对表【news_user】的数据库操作Service实现
* @createDate 2024-04-13 10:55:44
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private  UserMapper userMapper;

    /**
     * 登录需求
     * 地址: /user/login
     * 方式: post
     * 参数:
     *    {
     *     "username":"zhangsan", //用户名
     *     "userPwd":"123456"     //明文密码
     *    }
     * 返回:
     *   {
     *    "code":"200",         // 成功状态码
     *    "message":"success"   // 成功状态描述
     *    "data":{
     *         "token":"... ..." // 用户id的token
     *     }
     *  }
     *
     * 大概流程:
     *    1. 账号进行数据库查询 返回用户对象
     *    2. 对比用户密码(md5加密)
     *    3. 成功,根据userId生成token -> map key=token value=token值 - result封装
     *    4. 失败,判断账号还是密码错误,封装对应的枚举错误即可
     */
    @Override
    public Result login(User user) {

        //根据账号查询
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,user.getUsername());//用User类的username查询，查询值为user.username
        System.out.println(queryWrapper.getSqlSelect());
//        User loginUser = userMapper.selectOne(queryWrapper);
        User loginUser =redisCache.query("User"+user.getUsername(),queryWrapper,User.class,userMapper::selectOne,60L);

        //账号判断
        if (loginUser == null) {
            //账号错误，返回自定义枚举结果USERNAME_ERROR，值为501
            return Result.build(null, ResultCodeEnum.USERNAME_ERROR);
        }

        //判断密码，密码不为空且加密后与数据库中的密文密码匹配
        if (!StringUtils.isEmpty(user.getUserPwd())
                && loginUser.getUserPwd().equals(MD5Util.encrypt(user.getUserPwd())))
        {
            //账号密码正确，根据用户唯一标识生成token
            String token = jwtHelper.createToken(Long.valueOf(loginUser.getUid()));

            Map data = new HashMap();
            data.put("token",token);

            return Result.ok(data);
        }

        //密码错误
        return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
    }

    /**
     * 查询用户数据
     * @param token
     * @return result封装
     */
    @Override
    public Result getUserInfo(String token) {

        //1.判定是否有效期
        if (jwtHelper.isExpiration(token)) {
            //true过期,直接返回未登录
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }

        //2.获取token对应的用户
        int userId = jwtHelper.getUserId(token).intValue();

        //3.利用Reids查询数据
//        User user = userMapper.selectById(userId);
        User user =redisCache.query("User"+userId,userId,User.class,userMapper::selectById,60L);

        if (user != null) {
            user.setUserPwd(null);
            Map data = new HashMap();
            data.put("loginUser",user);
            return Result.ok(data);
        }

        return Result.build(null,ResultCodeEnum.NOTLOGIN);
    }


    /**
     * 用户名查重，检查账号是否可以注册
     *
     * @param username 账号信息
     * @return
     */
    @Override
    public Result checkUserName(String username) {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username);
//        User user = userMapper.selectOne(queryWrapper);
        User user =redisCache.query("User"+username,queryWrapper,User.class,userMapper::selectOne,60L);
        if (user != null){
            return Result.build(null,ResultCodeEnum.USERNAME_USED);
        }

        return Result.ok(null);
    }

    //用户注册
    @Override
    public Result regist(User user) {
        //检查用户名是否占用
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,user.getUsername());
//        User user = userMapper.selectOne(queryWrapper);
        User tempuser =redisCache.query("User"+user.getUsername(),queryWrapper,User.class,userMapper::selectOne,60L);
        if (tempuser != null){
            return Result.build(null,ResultCodeEnum.USERNAME_USED);
        }

        //密码加密后加入数据库
        user.setUserPwd(MD5Util.encrypt(user.getUserPwd()));
        int rows = userMapper.insert(user);
        System.out.println("rows = " + rows);
        return Result.ok(null);
    }
}




