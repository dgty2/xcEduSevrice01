package com.xuecheng.framework.domain.cms.response;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;

@Data
public class CourseMarketResult extends ResponseResult {
    CourseMarket courseMarket;

    public CourseMarketResult(ResultCode resultCode, CourseMarket courseMarket) {
        super(resultCode);
        this.courseMarket = courseMarket;
    }
}
