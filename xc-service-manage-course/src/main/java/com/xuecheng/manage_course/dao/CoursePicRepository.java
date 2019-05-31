package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author lpt
 * @Date 2019/4/9 9:32
 * @Version 1.0
 */
public interface CoursePicRepository extends JpaRepository<CoursePic, String> {

    long deleteCoursePicByCourseid(String courseId);
}
