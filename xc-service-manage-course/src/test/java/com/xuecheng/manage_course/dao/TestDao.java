package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.midi.Soundbank;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CourseMapper courseMapper;

    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    CourseMarketRepository courseMarketRepository;

    @Test
    public void testCourseBaseRepository() {
        Optional<CourseBase> optional = courseBaseRepository.findById("402885816240d276016240f7e5000002");
        if (optional.isPresent()) {
            CourseBase courseBase = optional.get();
            System.out.println(courseBase);
        }

    }

    @Test
    public void testCourseMapper() {
        CourseBase courseBase = courseMapper.findCourseBaseById("402885816240d276016240f7e5000002");
        System.out.println(courseBase);

    }

    @Test
    public void testTeachplan() {
        TeachplanNode teachplanNode = teachplanMapper.selectList("4028e581617f945f01617f9dabc40000");
        System.out.println(teachplanNode);
    }

    @Test
    public void testCategory() {
        CategoryNode list = categoryMapper.findList();
        System.out.println(list);
    }

    @Test
    public void testUpdateCourse() {
        Optional<CourseBase> optionalCourseBase = courseBaseRepository.findById("297e7c7c62b888f00162b8a7dec20000");
        if (optionalCourseBase.isPresent()) {
            CourseBase courseBase = optionalCourseBase.get();
            courseBase.setName("test_java基础32");
            CourseBase save = courseBaseRepository.save(courseBase);
            System.out.println(save);
        }
    }

    @Test
    public void testCourseMarket() {

        Optional<CourseMarket> byId = courseMarketRepository.findById("4028858162e0bc0a0162e0bfdf1a0000");
        if (byId.isPresent()) {
            CourseMarket courseMarket = byId.get();
            System.out.println(courseMarket);

        }
    }

}
