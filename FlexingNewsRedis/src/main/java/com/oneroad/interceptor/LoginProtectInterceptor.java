package com.oneroad.interceptor;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneroad.utils.JwtHelper;
import com.oneroad.utils.Result;
import com.oneroad.utils.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginProtectInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtHelper jwtHelper;

    //token检查，前置拦截器
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //从请求头获取token
        String token = request.getHeader("token");
        //判断token
        if (StringUtils.isEmpty(token) || jwtHelper.isExpiration(token)){
            //拦截
            //创建返回结果NOTLOGIN
            Result result = Result.build(null, ResultCodeEnum.NOTLOGIN);
            //Result对象 转 json
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(result);
            //返回json
            response.getWriter().print(json);
            return false;
        }else{
            //放行
            return true;
        }
    }
}