package com.xuecheng.framework.domain.learning.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author lpt
 * @Date 2019/4/26 20:10
 * @Version 1.0
 */
@Data
@ToString
@NoArgsConstructor//远程调用
public class GetMediaResult extends ResponseResult {
    //视频播放地址
    String fileUrl;

    public GetMediaResult(ResultCode resultCode, String fileUrl) {
        super(resultCode);
        this.fileUrl = fileUrl;
    }
}
