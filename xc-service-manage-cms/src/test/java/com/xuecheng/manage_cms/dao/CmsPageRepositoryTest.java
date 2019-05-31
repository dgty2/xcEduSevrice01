package com.xuecheng.manage_cms.dao;


import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsPageParam;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.SocketTimeoutException;
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
public class CmsPageRepositoryTest {
    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Test
    public void testFindAll() {
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);
    }

    //分页
    @Test
    public void testFindPage() {
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageRequest);
        System.out.println(all);
    }

    @Test
    public void testInsert() {
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId("s01");
        cmsPage.setTemplateId("t01");
        cmsPage.setPageName("测试页面");
        cmsPage.setPageCreateTime(new Date());
        List<CmsPageParam> cmsPageParams = new ArrayList<>();
        CmsPageParam cmsPageParam = new CmsPageParam();
        cmsPageParam.setPageParamName("param1");
        cmsPageParam.setPageParamValue("value1");
        cmsPageParams.add(cmsPageParam);
        cmsPage.setPageParams(cmsPageParams);
        cmsPageRepository.save(cmsPage);
        System.out.println(cmsPage);

    }

    @Test
    public void testUpdate() {
        //查询对象
        Optional<CmsPage> optional = cmsPageRepository.findById("5c9593a701dbe422bc2f2e48");
        if (optional.isPresent()) {
            CmsPage cmsPage = optional.get();
            cmsPage.setPageName("test");
            CmsPage save = cmsPageRepository.save(cmsPage);
            System.out.println(save);

        }

    }


    //spring data 自定义方法
    @Test
    public void testFindByPageName() {
        CmsPage test = cmsPageRepository.findByPageName("test");
        System.out.println(test);

    }

    //自定义条件查询
    @Test
    public void testFindAllByExample() {
        int page = 0;
        int size = 10;
        Pageable pageRequest = PageRequest.of(page, size);
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageAliase("课程详情");
        cmsPage.setPageName("10101.html");
        ExampleMatcher matching = ExampleMatcher.matching();
        //模糊匹配需要定义条件匹配器
        //模糊后精确查询
        ExampleMatcher exampleMatcher = matching.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains()).withMatcher("pageName", ExampleMatcher.GenericPropertyMatchers.exact());
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageRequest);
        List<CmsPage> content = all.getContent();
        System.out.println(content);
        System.out.println(all.getTotalElements());
    }

}
