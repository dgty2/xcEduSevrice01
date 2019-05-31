package com.xuecheng.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedis {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //创建jwt令牌
    @Test
    public void testRedis() {
        //定义key
        String key = "48d145d6-143a-41c3-b9ac-05ac0e3248ce";
        //定义value
        Map<String, String> value = new HashMap<>();
        value.put("jwt", "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6IlhjV2ViQXBwIiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOm51bGwsInV0eXBlIjpudWxsLCJpZCI6bnVsbCwiZXhwIjoxNTU2NDgwNzc5LCJqdGkiOiI0OGQxNDVkNi0xNDNhLTQxYzMtYjlhYy0wNWFjMGUzMjQ4Y2UiLCJjbGllbnRfaWQiOiJYY1dlYkFwcCJ9.b1T2F0wyIT1vhLrVrzm1tq6fF78pi6T9W3fsRqmwbX7QYL0fX_Lxk50OGICv8jJDXk0NPCbv_u3TMBQ6NYRnwVIVIKfWKqZIwVgrZRx0CiLdi5PQvGcnusro7xBUleEFo9T-XSEAMPk62kRDdhb4itZ9TJcFCyevFo2XfdMaDeAV9u-xiIJjdE-yQeOW8sYSM2349wxkk0WAFiXpSveCR5qrplh6FTn77cSlQ4JXxr9UzbS6iwHhi7HxO5JI0mUhxKSp1j6A6GaKpnQ2LB2BawiT9rPZRKX6k5Ff0715OWKVzBDXlCVKTO59R8jOBGXuPuh36xNcNgLhlrPULYegaw");
        value.put("refresh_token", "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6IlhjV2ViQXBwIiwic2NvcGUiOlsiYXBwIl0sImF0aSI6IjQ4ZDE0NWQ2LTE0M2EtNDFjMy1iOWFjLTA1YWMwZTMyNDhjZSIsIm5hbWUiOm51bGwsInV0eXBlIjpudWxsLCJpZCI6bnVsbCwiZXhwIjoxNTU2NDgwNzc5LCJqdGkiOiIzNTI0MjBiMC1mZTljLTQ3ZTAtOTQ4OC0wMzNiNDdjN2JlMzYiLCJjbGllbnRfaWQiOiJYY1dlYkFwcCJ9.brCgZuANh1big59vrzU06JiYQp8aUKMTh2haCeaGf3kiJ7c2P1DQJdnjuG5FR4k_hff0pA_Qb_ZipcdaRSvNLEADnK6vq5--oIrm0eHgmGZ41GtPmz1bg30FvXbeF_Et_IT4-3wBoU284CxSNqBMqJXYu1Z7Sp7A9tsRgwBgpvvuYPgs_gq-ujAr8j_f6isQkWnIQlfW4h-wc2ddbfof3MthaBpPuZmzkHU9bJm52xFloj2ZTQpzGDcQkszvV17l2Vio8DSTRFZu7n-8cbMvMPYFfQMYzLoEpTwVcnsGIgnGcpF4U9hWP_lsv3hpJiPHbgUcn1Po1w9SciWKN1RXGQ");
        String jsonString = JSON.toJSONString(value);
        //校验key是否存在，如果不存在则返回-2
        Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        System.out.println(expire);
        //存储数据
        stringRedisTemplate.boundValueOps(key).set(jsonString, 30, TimeUnit.SECONDS);
        //获取数据
        String string = stringRedisTemplate.opsForValue().get(key);
        System.out.println(string);


    }


}
