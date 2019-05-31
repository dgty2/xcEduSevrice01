package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * @Author lpt
 * @Date 2019/3/26 20:53
 * @Version 1.0
 */
public class ExceptionCast {
    public static void cast(ResultCode resultCode) {
        throw new CustomException(resultCode);
    }
}
