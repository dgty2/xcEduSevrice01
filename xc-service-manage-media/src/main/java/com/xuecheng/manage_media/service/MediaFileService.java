package com.xuecheng.manage_media.service;

import com.netflix.discovery.converters.Auto;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author lpt
 * @Date 2019/4/23 20:08
 * @Version 1.0
 */
@Service
public class MediaFileService {
    @Autowired
    MediaFileRepository mediaFileRepository;

    //查询我的媒资文件
    public QueryResponseResult<MediaFile> findList(int page, int size, QueryMediaFileRequest queryMediaFileRequest) {
        if (queryMediaFileRequest == null) {
            queryMediaFileRequest = new QueryMediaFileRequest();
        }
        //匹配条件值对象
        MediaFile mediaFile = new MediaFile();
        if (StringUtils.isNotEmpty(queryMediaFileRequest.getTag())) {
            mediaFile.setTag(queryMediaFileRequest.getTag());
        }
        if (StringUtils.isNotEmpty(queryMediaFileRequest.getFileOriginalName())) {
            mediaFile.setFileOriginalName(queryMediaFileRequest.getFileOriginalName());
        }
        if (StringUtils.isNotEmpty(queryMediaFileRequest.getProcessStatus())) {
            mediaFile.setProcessStatus(queryMediaFileRequest.getProcessStatus());
        }

        //条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("fileOriginalName", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("tag", ExampleMatcher.GenericPropertyMatchers.contains());
        //定义example条件对象
        Example<MediaFile> mediaFileExample = Example.of(mediaFile, exampleMatcher);
        //分页查询对象
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }

        Pageable pageable = new PageRequest(page, size);
        //分页查询
        Page<MediaFile> all = mediaFileRepository.findAll(mediaFileExample, pageable);
        List<MediaFile> mediaFileList = all.getContent();//数据列表
        long totalElements = all.getTotalElements();//总记录数
        //返回数据集
        QueryResult<MediaFile> queryResult = new QueryResult<>();
        queryResult.setList(mediaFileList);
        queryResult.setTotal(totalElements);

        //返回结果
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);

    }
}
