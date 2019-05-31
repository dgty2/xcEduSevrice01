package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义异常类型
 *
 * @Author lpt
 * @Date 2019/3/26 20:15
 * @Version 1.0
 */
public class CustomException extends RuntimeException {
    //错误代码
    ResultCode resultCode;

    public CustomException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

}
