package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.Category;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.dao.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author lpt
 * @Date 2019/4/3 14:17
 * @Version 1.0
 */
@Service
public class CategoryService {

    @Autowired
    CategoryMapper categoryMapper;


    public CategoryNode findList() {
        return categoryMapper.findList();

    }
}
