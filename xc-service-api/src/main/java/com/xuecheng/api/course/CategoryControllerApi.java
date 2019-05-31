package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author lpt
 * @Date 2019/4/3 14:01
 * @Version 1.0
 */

@Api(value = "课程分类管理", description = "课程分类管理", tags = {"课程分类管理"})
public interface CategoryControllerApi {
    @ApiOperation("查询分类")
    public CategoryNode findList();
}



