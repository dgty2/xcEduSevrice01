package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author lpt
 * @Date 2019/4/3 14:37
 * @Version 1.0
 */
@Mapper
public interface CategoryMapper {

    CategoryNode findList();
}
