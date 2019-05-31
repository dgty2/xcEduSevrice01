package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CategoryControllerApi;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lpt
 * @Date 2019/4/3 14:08
 * @Version 1.0
 */
@RequestMapping("/category")
@RestController
public class CategoryController implements CategoryControllerApi {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/list")
    @Override
    public CategoryNode findList() {
        return categoryService.findList();
    }
}
