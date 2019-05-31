package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author lpt
 * @Date 2019/3/28 9:05
 * @Version 1.0
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfig, String> {



}
