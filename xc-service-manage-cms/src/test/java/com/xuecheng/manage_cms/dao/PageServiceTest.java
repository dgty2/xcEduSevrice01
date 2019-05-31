package com.xuecheng.manage_cms.dao;


import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsPageParam;
import com.xuecheng.manage_cms.service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author lpt
 * @Date 2019/3/22 19:40
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class PageServiceTest {
    @Autowired
    PageService pageService;

    @Test
    public void testGetPageHtml() {
        String pageHtml = pageService.getPageHtml("5c98957b01dbe428084e5230");
        System.out.println(pageHtml);

    }




}
