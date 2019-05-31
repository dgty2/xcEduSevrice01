package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Author lpt
 * @Date 2019/3/23 10:20
 * @Version 1.0
 */
@Service
public class PageService {
    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    CmsConfigRepository cmsConfigRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    CmsSiteRepository cmsSiteRepository;


    /**
     * 页面查询方法
     *
     * @param page             页码，从1开始计数
     * @param size             每页记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }
        //定义条件匹配器
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        //条件值对象
        CmsPage cmsPage = new CmsPage();
        //设置条件站点ID,模板ID，页面别名为查询条件
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        Example<CmsPage> example = Example.of(cmsPage, matcher);
        if (page <= 0) {
            page = 1;
        }
        //分页参数下标从0开始
        page = page - 1;
        if (size <= 0) {
            size = 10;

        }
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);//实现自定义查询并分页
        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());//数据列表
        queryResult.setTotal(all.getTotalElements());//数据记录总数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;


    }

    public CmsPageResult add(CmsPage cmsPage) {
        if (cmsPage == null) {
            //非法参数异常
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);

        }
        //校验页面是否存在，根据页面名称、站点Id、页面webpath
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (cmsPage1 != null) {
            //页面已经存在
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }

        if (cmsPage1 == null) {
            cmsPage.setPageId(null);//添加页面主键由spring data 自动生成
            cmsPageRepository.save(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
        }
        return new CmsPageResult(CommonCode.FAIL, cmsPage);
    }

    public CmsPage getById(String id) {
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if (optional.isPresent()) {
            CmsPage cmsPage = optional.get();
            return cmsPage;
        }
        return null;
    }

    public CmsPageResult update(String id, CmsPage cmsPage) {
        CmsPage cmsPage1 = this.getById(id);
        if (cmsPage1 != null) {
            cmsPage1.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            cmsPage1.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            cmsPage1.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            cmsPage1.setPageName(cmsPage.getPageName());
            //更新访问路径
            cmsPage1.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            cmsPage1.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            cmsPage1.setDataUrl(cmsPage.getDataUrl());
            //执行更新
            cmsPageRepository.save(cmsPage1);
            return new CmsPageResult(CommonCode.SUCCESS, cmsPage1);
        }
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    //根据id删除页面
    public ResponseResult delete(String id) {
        //查询
        Optional<CmsPage> optinal = cmsPageRepository.findById(id);
        if (optinal.isPresent()) {
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    //根据id查询cmsConfig
    public CmsConfig getConfigById(String id) {
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        if (optional.isPresent()) {
            CmsConfig cmsConfig = optional.get();
            return cmsConfig;
        }
        return null;
    }

    //页面静态化方法
    public String getPageHtml(String pageId) {
        Map model = getModelByPageId(pageId);
        if (model == null) {
            //数据模型获取不到
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        //获取页面模板信息
        String template = getTemplateByPageId(pageId);
        if (StringUtils.isEmpty(template)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //执行静态化
        String html = generateHtml(template, model);
        return html;

    }

    //执行静态化
    private String generateHtml(String template, Map model) {
        //创建配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //创建模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template", template);
        //configuration配置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        //获取模板
        try {
            Template template1 = configuration.getTemplate("template");
            //执行静态化
            String s = FreeMarkerTemplateUtils.processTemplateIntoString(template1, model);
            return s;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    //获取数据模型
    private Map getModelByPageId(String PageId) {
        CmsPage cmsPage = this.getById(PageId);
        //页面不存在
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);

        }
        String dataUrl = cmsPage.getDataUrl();
        //dataUrl不存在
        if (StringUtils.isEmpty(dataUrl)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);

        }
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;

    }

    //获取页面模板
    public String getTemplateByPageId(String PageId) {
        CmsPage cmsPage = this.getById(PageId);
        //页面不存在
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);

        }
        //获取页面模板Id
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);

        }
        //查询模板信息
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if (optional.isPresent()) {
            CmsTemplate cmsTemplate = optional.get();
            //获取模板文件id
            String templateFileId = cmsTemplate.getTemplateFileId();
            //从GridFS中去模板内容
            //根据文件ID查询文件
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            //打开下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //创建gridFsResource，用于获取流对象
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
            //获取流数据
            try {
                String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //页面发布
    public ResponseResult post(String pageId) {
        //执行页面静态化
        String pageHtml = this.getPageHtml(pageId);
        //页面静态化存储到gridFS中
        CmsPage cmsPage = this.saveHtml(pageId, pageHtml);
        this.sendPostPage(pageId);
        return new ResponseResult(CommonCode.SUCCESS);

    }


    private void sendPostPage(String pageId) {
        CmsPage cmsPage = this.getById(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //创建消息对象
        Map<String, String> msg = new HashMap<>();
        /**todo
         * pageid??????
         */
        msg.put("pageId", pageId);
        //转json串
        String jsonString = JSON.toJSONString(msg);
        //向mq发送消息
        String siteId = cmsPage.getSiteId();
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE, siteId, jsonString);
    }


    private CmsPage saveHtml(String pageId, String htmlContent) {
        CmsPage cmsPage = this.getById(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        ObjectId objectId = null;
        try {
            InputStream inputStream = IOUtils.toInputStream(htmlContent, "utf-8");
            objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());

        } catch (IOException e) {
            e.printStackTrace();
        }
        //将HTML文件id跟新到cmspage中
        cmsPage.setHtmlFileId(objectId.toHexString());
        cmsPageRepository.save(cmsPage);
        return cmsPage;
    }

    //添加页面，如果已存在则更新页面
    public CmsPageResult save(CmsPage cmsPage) {
        //校验页面是否存在，根据页面名称、站点Id、页面webpath查询
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (cmsPage1 != null) {
            //更新
            return this.update(cmsPage1.getPageId(), cmsPage);
        } else {
            //添加
            return this.add(cmsPage);
        }
    }

    //一键发布页面
    public CmsPostPageResult postPageQuick(CmsPage cmsPage) {
        //页面保存
        CmsPageResult save = this.save(cmsPage);
        if (!save.isSuccess()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //页面发布
        CmsPage cmsPage1 = save.getCmsPage();
        String pageId = cmsPage1.getPageId();
        //执行页面发布（先静态化、保存GridFS，向MQ发送消息）
        ResponseResult responseResult = this.post(pageId);
        if (!responseResult.isSuccess()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //得到页面的url
        //页面url=站点域名+站点webpath+页面webpath+页面名称
        String siteId = cmsPage1.getSiteId();
        CmsSite cmsSite = findCmsSiteById(siteId);
        //站点域名
        String siteDomain = cmsSite.getSiteDomain();
        //站点web路径
        String siteWebPath = cmsSite.getSiteWebPath();
        //页面web路径
        String pageWebPath = cmsPage1.getPageWebPath();
        //页面名称
        String pageName = cmsPage1.getPageName();
        //页面的web访问地址
        String pageUrl = siteDomain + siteWebPath + pageWebPath + pageName;
        return new CmsPostPageResult(CommonCode.SUCCESS, pageUrl);
    }

    //根据id查询站点信息
    public CmsSite findCmsSiteById(String siteId) {
        Optional<CmsSite> optional = cmsSiteRepository.findById(siteId);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }
}
