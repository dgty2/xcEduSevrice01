package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author lpt
 * @Date 2019/4/13 15:06
 * @Version 1.0
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite, String> {
}
