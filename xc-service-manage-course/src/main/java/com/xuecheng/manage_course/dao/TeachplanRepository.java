package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author lpt
 * @Date 2019/4/2 14:49
 * @Version 1.0
 */
public interface TeachplanRepository extends JpaRepository<Teachplan, String> {
    //根据课程id和parentid
    public List<Teachplan> findByCourseidAndParentid(String courseid, String parentid);

}
