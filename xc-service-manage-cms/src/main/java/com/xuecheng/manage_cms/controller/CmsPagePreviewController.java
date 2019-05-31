package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

/**
 * @Author lpt
 * @Date 2019/3/29 20:03
 * @Version 1.0
 */
@Controller
public class CmsPagePreviewController extends BaseController {

    @Autowired
    PageService pageService;

    //接收到页面id
    @RequestMapping(value = "/cms/preview/{pageId}", method = RequestMethod.GET)
    public void preview(@PathVariable("pageId") String pageId) {
        //执行静态化
        String pageHtml = pageService.getPageHtml(pageId);
        //通过response对象将内容输出
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            response.setHeader("Content-type", "text/html;charset=utf-8");
            outputStream.write(pageHtml.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
