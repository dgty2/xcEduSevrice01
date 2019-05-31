package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author lpt
 * @Date 2019/4/2 9:40
 * @Version 1.0
 */
@Mapper
public interface TeachplanMapper {
    //课程计划查询
    public TeachplanNode selectList(String courseId);
}
