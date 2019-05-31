package com.xuecheng.manage_course;

import com.netflix.discovery.converters.Auto;
import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.swing.tree.RowMapper;
import java.util.Map;

/**
 * @Author lpt
 * @Date 2019/4/10 19:48
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class testRibbon {
    @Autowired
    RestTemplate restTemplate;

    //负载均衡调用
    @Test
    public void testRibbon() {
        //服务id
        String serviceId = "XC‐SERVICE‐MANAGE‐CMS";
       /* for (int i = 0; i < 10; i++) {
            //通过服务id调用
            ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://" + serviceId
                    + "/cms/page/get/5c98957b01dbe428084e5230", Map.class);
            Map map = forEntity.getBody();
            System.out.println(map);W
        }*/
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/page/get/5c98957b01dbe428084e5230", Map.class);
        System.out.println(forEntity);


    }


}
