package com.xuecheng.framework.domain.cms.response;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;

@Data
public class CourseBaseResult extends ResponseResult {
    CourseBase courseBase;

    public CourseBaseResult(ResultCode resultCode, CourseBase courseBase) {
        super(resultCode);
        this.courseBase = courseBase;
    }
}
