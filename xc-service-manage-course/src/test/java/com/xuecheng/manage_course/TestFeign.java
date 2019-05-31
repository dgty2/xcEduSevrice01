package com.xuecheng.manage_course;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_course.client.CmsPageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFeign {
    //接口代理对象，由Feign生成代理对象
    @Autowired
    CmsPageClient cmsPageClient;

    @Test
    public void testFeign() {
        //发起远程调用
        CmsPage cmsPage = cmsPageClient.findCmsPageById("5af942190e661827d8e2f5e3");
        System.out.println(cmsPage);

    }


}
