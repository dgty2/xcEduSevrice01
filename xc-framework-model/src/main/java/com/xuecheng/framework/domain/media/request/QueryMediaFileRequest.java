package com.xuecheng.framework.domain.media.request;

import com.xuecheng.framework.model.request.RequestData;
import lombok.Data;

@Data
public class QueryMediaFileRequest extends RequestData {
    //文件原始名称
    private String fileOriginalName;
    //文件处理状态
    private String processStatus;
    //文件管理标签
    private String tag;
}
