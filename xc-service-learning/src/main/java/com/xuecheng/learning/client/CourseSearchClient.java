package com.xuecheng.learning.client;

import com.xuecheng.api.search.EsCourseControllerApi;
import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author lpt
 * @Date 2019/4/27 15:29
 * @Version 1.0
 */
@FeignClient(value = "xc‐service‐search")
public interface CourseSearchClient {
    //根据课程计划id查询课程媒资
    @GetMapping("/search/course/getmedia/{teachplanId}")
    public TeachplanMediaPub getmedia(@PathVariable("teachplanId") String teachplanId);
}
