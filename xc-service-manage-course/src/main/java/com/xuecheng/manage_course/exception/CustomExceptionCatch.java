package com.xuecheng.manage_course.exception;

import com.xuecheng.framework.exception.ExceptionCatch;
import com.xuecheng.framework.model.response.CommonCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @Author lpt
 * @Date 2019/5/11 15:18
 * @Version 1.0
 */
@ControllerAdvice
public class CustomExceptionCatch extends ExceptionCatch {

    static {
        builder.put(AccessDeniedException.class, CommonCode.UNAUTHORISE);
    }

}
