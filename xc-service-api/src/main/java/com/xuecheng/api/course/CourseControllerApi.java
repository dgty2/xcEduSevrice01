package com.xuecheng.api.course;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author lpt
 * @Date 2019/4/1 21:21
 * @Version 1.0
 */
@Api(value = "课程页面管理接口", description = "课程页面管理接口，提供课程的增、删、改、查", tags = {"课程信息管理"})
public interface CourseControllerApi {
    @ApiOperation("课程计划查询")
    public TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("课程计划添加")
    public ResponseResult addTeachplan(Teachplan teachplan);

    //课程查询
    @ApiOperation("课程查询")
    public QueryResponseResult<CourseInfo> findCourseList(
            int page,
            int size,
            CourseListRequest courseListRequest);

    @ApiOperation("根据课程id查询课程信息")
    public CourseBase getCoursebaseById(String id);

    @ApiOperation("添加课程基础信息")
    public AddCourseResult addCourseBase(CourseBase courseBase);

    @ApiOperation("更新课程基础信息")
    public ResponseResult updateCourseBase(String id, CourseBase courseBase);

    @ApiOperation("更新课程营销信息")
    public ResponseResult updateCourseMarket(String id, CourseMarket courseMarket);

    @ApiOperation("获取课程营销信息")
    public CourseMarket getCourseMarketById(String courseId);

    @ApiOperation("添加课程图片")
    public ResponseResult addCoursePic(String courseId, String pic);

    @ApiOperation("根据课程id获取课程图片信息")
    public CoursePic findCoursePic(String courseId);

    @ApiOperation("删除课程图片")
    public ResponseResult deleteCoursePic(String courseId);

    @ApiOperation("课程视图查询")
    public CourseView courseview(String id);

    @ApiOperation("预览课程")
    public CoursePublishResult preview(String id);

    @ApiOperation("发布课程")
    public CoursePublishResult publish(@PathVariable String id);

    @ApiOperation("保存课程计划与媒资文件关联")
    public ResponseResult savemedia(TeachplanMedia teachplanMedia);


}
