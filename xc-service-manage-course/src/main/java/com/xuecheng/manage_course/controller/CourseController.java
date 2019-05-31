package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.Oauth2Util;
import com.xuecheng.framework.utils.XcOauth2Util;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Author lpt
 * @Date 2019/4/2 10:24
 * @Version 1.0
 */
@RestController
@RequestMapping("/course")
public class CourseController extends BaseController implements CourseControllerApi {
    @Autowired
    CourseService courseService;

    //添加课程图片
    @Override
    @PostMapping("/coursepic/add")
    public ResponseResult addCoursePic(String courseId, String pic) {
        return courseService.addCoursePic(courseId, pic);
    }

    //查询课程图片
    //当用户拥有course_pic_list权限时候方可访问此方法
    @PreAuthorize("hasAuthority('course_pic_list')")
    @Override
    @GetMapping("/coursepic/list/{courseId}")
    public CoursePic findCoursePic(@PathVariable("courseId") String courseId) {
        return courseService.findCoursepic(courseId);
    }

    //删除课程图片
    @Override
    @DeleteMapping("/coursepic/delete")
    public ResponseResult deleteCoursePic(@RequestParam("courseId") String courseId) {
        return courseService.deleteCoursePic(courseId);
    }

    //添加计划
    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        return courseService.addTeachplan(teachplan);
    }

    //查询课程计划
    @PreAuthorize("hasAuthority('course_teachplan_list')")
    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        return courseService.findTeachplanList(courseId);
    }

    //修改课程营销信息
    @Override
    @PutMapping("/coursemarket/update/{id}")
    public ResponseResult updateCourseMarket(@PathVariable("id") String id, @RequestBody CourseMarket courseMarket) {
        CourseMarket courseMarket1 = courseService.updateCourseMarket(id, courseMarket);
        if (courseMarket1 != null) {
            return new ResponseResult(CommonCode.SUCCESS);
        } else {
            return new ResponseResult(CommonCode.FAIL);
        }

    }

    //查询课程营销信息
    @Override
    @GetMapping("/coursemarket/get/{courseid}")
    public CourseMarket getCourseMarketById(@PathVariable("courseid") String courseId) {
        return courseService.getCourseMarketById(courseId);
    }

    //查询我的课程列表
    @GetMapping("/coursebase/list/{page}/{size}")
    @Override
    public QueryResponseResult<CourseInfo> findCourseList(@PathVariable("page") int page, @PathVariable("size") int size, CourseListRequest courseListRequest) {
        XcOauth2Util xcOauth2Util = new XcOauth2Util();
        XcOauth2Util.UserJwt userJwtFromHeader = xcOauth2Util.getUserJwtFromHeader(request);
        String companyId = userJwtFromHeader.getCompanyId();
        return courseService.findCourseList(companyId, page, size, courseListRequest);
    }

    //根据id查询课程信息
    @Override
    @GetMapping("/coursebase/get/{id}")
    public CourseBase getCoursebaseById(@PathVariable("id") String id) {
        return courseService.getCoursebaseById(id);
    }

    //修改课程信息
    @Override
    @PutMapping("/coursebase/update/{id}")
    public ResponseResult updateCourseBase(@PathVariable("id") String id, @RequestBody CourseBase courseBase) {

        return courseService.updateCourseBase(id, courseBase);
    }

    //课程视图查询
    @Override
    @GetMapping("/courseview/{id}")
    public CourseView courseview(@PathVariable("id") String id) {
        return courseService.getCoruseView(id);
    }

    //课程预览
    @Override
    @PostMapping("/preview/{id}")
    public CoursePublishResult preview(@PathVariable("id") String id) {
        return courseService.preview(id);
    }

    //课程发布
    @Override
    @PostMapping("/publish/{id}")
    public CoursePublishResult publish(@PathVariable String id) {
        return courseService.publish(id);
    }

    //保存课程计划与媒资文件关联
    @Override
    @PostMapping("/savemedia")
    public ResponseResult savemedia(@RequestBody TeachplanMedia teachplanMedia) {
        return courseService.savemedia(teachplanMedia);
    }


    //添加课程基础信息
    @Override
    @PostMapping("/coursebase/add")
    public AddCourseResult addCourseBase(@RequestBody CourseBase courseBase) {
        return courseService.addCourseBase(courseBase);
    }
}
