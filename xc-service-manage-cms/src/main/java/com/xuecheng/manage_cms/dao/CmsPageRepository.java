package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author lpt
 * @Date 2019/3/22 19:33
 * @Version 1.0
 */
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {
    //根据页面名称查询
    // 自定义一定要按规则写
    CmsPage findByPageName(String pageName);

    //根据页面名称、站点ID、页面webpath查询
    CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName, String siteId, String pageWebPath);

}
