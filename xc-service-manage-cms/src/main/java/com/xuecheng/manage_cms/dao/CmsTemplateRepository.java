package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author lpt
 * @Date 2019/3/22 19:33
 * @Version 1.0
 */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate, String> {


}
