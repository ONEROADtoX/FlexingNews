package com.oneroad.controller;

import com.alibaba.druid.util.StringUtils;
import com.oneroad.pojo.User;
import com.oneroad.utils.JwtHelper;
import com.oneroad.utils.Result;
import com.oneroad.service.UserService;
import com.oneroad.utils.ResultCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@CrossOrigin //用于跨域
@Slf4j
@Tag(name="用户模块")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;

    /**
     * 登录需求
     * 地址: /user/login
     * 方式: post
     * 参数:
     *    { "username":"zhangsan", //用户名
     *     "userPwd":"123456"     //明文密码
     *    }
     * 返回:
     *   { "code":"200",         // 成功状态码
     *    "message":"success"   // 成功状态描述
     *    "data":{
     *         "token":"... ..." // 用户id的token
     *     }
     *   }
     *     大概流程:
     *    1. 账号进行数据库查询 返回用户对象
     *    2. 对比用户密码(md5加密)
     *    3. 成功,根据userId生成token -> map key=token value=token值 - result封装
     *    4. 失败,判断账号还是密码错误,封装对应的枚举错误即可
     */
    @PostMapping("login")
    @Parameters(@Parameter(name="username",required = true))
    public Result login(@RequestBody User user){
        Result result = userService.login(user);
        return result;
    }

    /**
     * 根据token获取用户数据，请求头
     * 地址: user/getUserInfo
     * 方式: get
     * 请求头: token = token内容
     * 返回:
     *    {
     *     "code": 200,
     *     "message": "success",
     *     "data": {
     *         "loginUser": {
     *             "uid": 1,
     *             "username": "zhangsan",
     *             "userPwd": "",
     *             "nickName": "张三"
     *         }
     *      }
     *   }
     *
     * 大概流程:
     *    1.获取token,解析token对应的userId
     *    2.根据userId,查询用户数据
     *    3.将用户数据的密码置空,并且把用户数据封装到结果中key = loginUser
     *    4.失败返回504 (本次先写到当前业务,后期提取到拦截器和全局异常处理器)
     */
    @GetMapping("getUserInfo")
    public Result userInfo(@RequestHeader String token){
        Result result = userService.getUserInfo(token);
        return result;
    }

    /**
     * 用户名查重，param
     * url地址：user/checkUserName
     * 请求方式：POST
     * 请求参数：param形式
     * username=zhangsan
     * 响应数据:
     * {
     *    "code":"200",
     *    "message":"success"
     *    "data":{}
     * }
     *
     * 实现步骤:
     *   1. 获取账号数据
     *   2. 根据账号进行数据库查询
     *   3. 结果封装
     */
    @PostMapping("checkUserName")
    public Result checkUserName(String username){
        Result result = userService.checkUserName(username);
        return result;
    }

    /**
     * 用户注册，json
     * url地址：user/regist
     * 请求方式：POST
     * 请求参数：
     * {
     *     "username":"zhangsan",
     *     "userPwd":"123456",
     *     "nickName":"张三"
     * }
     * 响应数据：
     * {
     *    "code":"200",
     *    "message":"success"
     *    "data":{}
     * }
     *
     * 实现步骤:
     *   1. 将密码加密
     *   2. 将数据插入
     *   3. 判断结果,成 返回200 失败 505
     */
    @PostMapping("regist")
    public Result regist(@RequestBody User user){
        Result result = userService.regist(user);
        return result;
    }

    //登陆检查
    @GetMapping("checkLogin")
    public Result checkLogin(@RequestHeader String token){
        if (StringUtils.isEmpty(token) || jwtHelper.isExpiration(token)){
            //没有传 或者 过期，返回 未登录
            return Result.build(null, ResultCodeEnum.NOTLOGIN);
        }
        //已登录
        return Result.ok(null);
    }
}