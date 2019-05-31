package com.xuecheng.govern.gateway.service;

import com.xuecheng.framework.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author lpt
 * @Date 2019/5/8 19:46
 * @Version 1.0
 */
@Service
public class AuthService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //取jwt令牌从header中
    public String getJwtFromHeader(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)) {
            return null;
        }
        if (!authorization.startsWith("Bearer ")) {
            return null;
        }
        String jwt = authorization.substring(7);
        return jwt;
    }

    //从cookie取出token
    //查询身份令牌
    public String getTokenFromCookie(HttpServletRequest request) {
        Map<String, String> cookieMap = CookieUtil.readCookie(request, "uid");
        String access_token = cookieMap.get("uid");
        if (StringUtils.isEmpty(access_token)) {
            return null;
        }
        return access_token;
    }

    //查询令牌的有效期
    public long getExpire(String access_token) {
        //key
        String key = "user_token:" + access_token;
        Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expire;
    }

}
