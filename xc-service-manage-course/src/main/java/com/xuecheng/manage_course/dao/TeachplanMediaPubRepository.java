package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author lpt
 * @Date 2019/4/25 15:27
 * @Version 1.0
 */
public interface TeachplanMediaPubRepository extends JpaRepository<TeachplanMediaPub, String> {

    long deleteByCourseId(String courseId);
}
