package com.xuecheng.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author lpt
 * @Date 2019/4/2 10:25
 * @Version 1.0
 */
@Service
public class CourseService {
    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    TeachplanRepository teachplanRepository;

    @Autowired
    CourseBaseRepository courseBaseRepository;

    @Autowired
    CourseMarketRepository courseMarketRepository;

    @Autowired
    CoursePicRepository coursePicRepository;

    @Autowired
    CoursePubRepository coursePubRepository;

    @Autowired
    CmsPageClient cmsPageClient;

    @Autowired
    TeachplanMediaRepository teachplanMediaRepository;

    @Autowired
    TeachplanMediaPubRepository teachplanMediaPubRepository;

    @Autowired
    CourseMapper courseMapper;

    @Value("${course‐publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course‐publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course‐publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course‐publish.siteId}")
    private String publish_siteId;
    @Value("${course‐publish.templateId}")
    private String publish_templateId;
    @Value("${course‐publish.previewUrl}")
    private String previewUrl;

    //查询课程计划
    public TeachplanNode findTeachplanList(String courseId) {

        return teachplanMapper.selectList(courseId);
    }

    //添加课程计划
    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan) {
        if (teachplan == null || StringUtils.isEmpty(teachplan.getCourseid()) || StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        String courseid = teachplan.getCourseid();
        String parentid = teachplan.getParentid();
        if (StringUtils.isEmpty(parentid)) {
            parentid = this.getTeachplanRoot(courseid);
        }
        Optional<Teachplan> optional = teachplanRepository.findById(parentid);
        Teachplan parentNode = optional.get();
        //父结点的级别
        String grade = parentNode.getGrade();
        //新结点
        Teachplan teachplanNew = new Teachplan();
        //将页面提交的teachplan信息拷贝到teachplanNew对象中
        BeanUtils.copyProperties(teachplan, teachplanNew);
        teachplanNew.setParentid(parentid);
        teachplanNew.setCourseid(courseid);
        if (grade.equals("1")) {
            teachplanNew.setGrade("2");//级别，根据父结点的级别来设置
        } else {
            teachplanNew.setGrade("3");
        }

        teachplanRepository.save(teachplanNew);

        return new ResponseResult(CommonCode.SUCCESS);


    }

    //查询课程的根结点，如果查询不到要自动添加根结点
    private String getTeachplanRoot(String courseid) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseid);
        if (!optional.isPresent()) {
            return null;
        }
        //课程信息
        CourseBase courseBase = optional.get();
        //查询课程根结点
        List<Teachplan> teachplans = teachplanRepository.findByCourseidAndParentid(courseid, "0");
        if (teachplans == null || teachplans.size() <= 0) {
            Teachplan teachplan = new Teachplan();
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setPname(courseBase.getName());
            teachplan.setCourseid(courseid);
            teachplan.setStatus("0");
            teachplanRepository.save(teachplan);
            return teachplan.getId();
        }
        //返回根结点id
        return teachplans.get(0).getId();

    }

    //查询课程
    public QueryResponseResult<CourseInfo> findCourseList(String companyId, int page, int size, CourseListRequest courseListRequest) {
        if (courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }
        if (page <= 0) {
            page = 0;
        }
        if (size <= 0) {
            size = 10;
        }
        courseListRequest.setCompanyId(companyId);
        //分页
        PageHelper.startPage(page, size);
        //调用dao
        Page<CourseInfo> courseListPage = courseMapper.findCourseListPage(courseListRequest);
        List<CourseInfo> list = courseListPage.getResult();
        long total = courseListPage.getTotal();
        QueryResult<CourseInfo> courseInfoQueryResult = new QueryResult<CourseInfo>();
        courseInfoQueryResult.setList(list);
        courseInfoQueryResult.setTotal(total);
        return new QueryResponseResult<CourseInfo>(CommonCode.SUCCESS, courseInfoQueryResult);
    }

    //查询课程基本信息
    public CourseBase getCoursebaseById(String id) {
        Optional<CourseBase> optional = courseBaseRepository.findById(id);
        if (optional.isPresent()) {
            CourseBase courseBase = optional.get();
            return courseBase;
        }
        return null;
    }

    //修改课程基本信息
    @Transactional
    public ResponseResult updateCourseBase(String id, CourseBase courseBase) {
        Optional<CourseBase> optionalCourseBase = courseBaseRepository.findById(id);
        if (optionalCourseBase.isPresent()) {
            //得到数据
            CourseBase courseBase1 = optionalCourseBase.get();
            courseBase1.setName(courseBase.getName());
            courseBase1.setUsers(courseBase.getUsers());
            courseBase1.setMt(courseBase.getMt());
            courseBase1.setGrade(courseBase.getGrade());
            courseBase1.setStudymodel(courseBase.getStudymodel());
            courseBase1.setDescription(courseBase.getDescription());
            courseBaseRepository.save(courseBase1);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return null;
    }

    //查询课程营销信息
    public CourseMarket getCourseMarketById(String courseId) {
        Optional<CourseMarket> optional = courseMarketRepository.findById(courseId);
        if (optional.isPresent()) {
            CourseMarket courseMarket = optional.get();
            return courseMarket;
        }
        return null;

    }

    //修改课程营销信息
    @Transactional
    public CourseMarket updateCourseMarket(String id, CourseMarket courseMarket) {
        //得到课程营销信息保存数据
        CourseMarket courseMarket1 = this.getCourseMarketById(id);
        if (courseMarket1 != null) {
            courseMarket1.setCharge(courseMarket.getCharge());
            courseMarket1.setValid(courseMarket.getValid());
            courseMarket1.setQq(courseMarket.getQq());
            courseMarket1.setPrice(courseMarket.getPrice());
            courseMarket1.setStartTime(courseMarket.getStartTime());
            courseMarket1.setEndTime(courseMarket.getEndTime());
            //保存返回结果
            courseMarketRepository.save(courseMarket1);

        } else {
            courseMarket1 = new CourseMarket();
            BeanUtils.copyProperties(courseMarket, courseMarket1);
            courseMarket1.setId(id);
            courseMarketRepository.save(courseMarket1);
        }
        return courseMarket1;
    }

    //添加课程图片
    @Transactional
    public ResponseResult addCoursePic(String courseId, String pic) {
        //查询课程图片
        CoursePic coursePic = null;
        Optional<CoursePic> picOptional = coursePicRepository.findById(courseId);
        if (picOptional.isPresent()) {
            coursePic = picOptional.get();
        }
        //没有课程图片则新建对象
        if (coursePic == null) {
            coursePic = new CoursePic();
        }
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //查询课程图片信息
    public CoursePic findCoursepic(String courseId) {
        Optional<CoursePic> picOptional = coursePicRepository.findById(courseId);
        if (picOptional.isPresent()) {
            CoursePic coursePic = picOptional.get();
            return coursePic;
        }
        return null;
    }

    //删除课程图片
    @Transactional
    //dml中cud需要事物
    public ResponseResult deleteCoursePic(String courseId) {
        long result = coursePicRepository.deleteCoursePicByCourseid(courseId);
        if (result > 0) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    //课程视图查询
    public CourseView getCoruseView(String id) {
        CourseView courseView = new CourseView();

        //查询课程基本信息
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(id);
        if (courseBaseOptional.isPresent()) {
            CourseBase courseBase = courseBaseOptional.get();
            courseView.setCourseBase(courseBase);
        }
        //查询课程图片
        Optional<CoursePic> picOptional = coursePicRepository.findById(id);
        if (picOptional.isPresent()) {
            CoursePic coursePic = picOptional.get();
            courseView.setCoursePic(coursePic);
        }

        //课程营销信息
        Optional<CourseMarket> marketOptional = courseMarketRepository.findById(id);
        if (marketOptional.isPresent()) {
            CourseMarket courseMarket = marketOptional.get();
            courseView.setCourseMarket(courseMarket);
        }

        //课程计划信息
        TeachplanNode teachplanNode = teachplanMapper.selectList(id);
        courseView.setTeachplanNode(teachplanNode);

        return courseView;

    }

    //根据id查询课程基本信息
    public CourseBase findCourseBaseById(String courseId) {
        Optional<CourseBase> baseOptional = courseBaseRepository.findById(courseId);
        if (baseOptional.isPresent()) {
            CourseBase courseBase = baseOptional.get();
            return courseBase;
        }
        ExceptionCast.cast(CourseCode.COURSE_GET_NOTEXISTS);
        return null;
    }

    //课程预览
    public CoursePublishResult preview(String id) {
        //查询课程
        CourseBase courseBaseById = this.findCourseBaseById(id);
        //请求cms添加页面
        //准备cmsPage信息
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);//站点id
        cmsPage.setDataUrl(publish_dataUrlPre + id);//数据模型url
        cmsPage.setPageName(id + ".html");//页面名称
        cmsPage.setPageAliase(courseBaseById.getName());//页面别名，就是课程名称
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);//页面物理路径
        cmsPage.setPageWebPath(publish_page_webpath);//页面webpath
        cmsPage.setTemplateId(publish_templateId);//页面模板id

        //远程调用cms
        CmsPageResult cmsPageResult = cmsPageClient.saveCmsPage(cmsPage);
        if (!cmsPageResult.isSuccess()) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }

        CmsPage cmsPage1 = cmsPageResult.getCmsPage();
        String pageId = cmsPage1.getPageId();
        //拼装页面预览的url
        String url = previewUrl + pageId;
        //返回CoursePublishResult对象（当中包含了页面预览的url）
        return new CoursePublishResult(CommonCode.SUCCESS, url);
    }

    //课程发布
    @Transactional
    public CoursePublishResult publish(String id) {
        CourseBase courseBaseById = this.findCourseBaseById(id);

        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);//站点id
        cmsPage.setDataUrl(publish_dataUrlPre + id);//数据模型url
        cmsPage.setPageName(id + ".html");//页面名称
        cmsPage.setPageAliase(courseBaseById.getName());//页面别名，就是课程名称
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);//页面物理路径
        cmsPage.setPageWebPath(publish_page_webpath);//页面webpath
        cmsPage.setTemplateId(publish_templateId);//页面模板id

        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        if (!cmsPostPageResult.isSuccess()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //更新课程发布状态
        CourseBase courseBase = saveCoursePubState(id);
        if (courseBase == null) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //保存课程索引信息
        //先创建一个coursePub对象
        CoursePub coursePub = createCoursePub(id);
        //将coursePub对象保存到数据库
        saveCoursePub(id, coursePub);

        //页面url
        String pageUrl = cmsPostPageResult.getPageUrl();
        //向teachplanMediaPub中保存课程媒资信息
        saveTeachplanMediaPub(id);
        return new CoursePublishResult(CommonCode.SUCCESS, pageUrl);
    }

    //向teachplanMediaPub中保存课程媒资信息
    private void saveTeachplanMediaPub(String id) {
        //先删除teachplanMediaPub中的数据
        teachplanMediaPubRepository.deleteByCourseId(id);
        //从teachplanMedia中查询
        List<TeachplanMedia> teachplanMediaList = teachplanMediaRepository.findByCourseId(id);
        List<TeachplanMediaPub> teachplanMediaPubList = new ArrayList<>();
        for (TeachplanMedia teachplanMedia : teachplanMediaList) {
            //将teachplanMediaList数据放到teachplanMediaPubs中
            TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
            BeanUtils.copyProperties(teachplanMedia, teachplanMediaPub);
            //添加时间戳
            teachplanMediaPub.setTimestamp(new Date());
            teachplanMediaPubList.add(teachplanMediaPub);
        }
        //将teachplanMediaList插入到teachplanMediaPub
        teachplanMediaPubRepository.saveAll(teachplanMediaPubList);
    }


    //将coursePub对象保存到数据库
    private CoursePub saveCoursePub(String id, CoursePub coursePub) {
        CoursePub coursePubNew = null;
        //根据课程id查询coursePub
        Optional<CoursePub> coursePubOptional = coursePubRepository.findById(id);
        if (coursePubOptional.isPresent()) {
            coursePubNew = coursePubOptional.get();
        } else {
            coursePubNew = new CoursePub();
        }

        //coursePub对象信息保存到coursePubNew中
        BeanUtils.copyProperties(coursePub, coursePubNew);
        coursePubNew.setId(id);
        //时间戳,给logstach使用
        coursePubNew.setTimestamp(new Date());
        //发布时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        coursePubNew.setPubTime(date);
        coursePubRepository.save(coursePubNew);
        return coursePubNew;

    }

    //创建coursePub对象
    private CoursePub createCoursePub(String id) {
        CoursePub coursePub = new CoursePub();
        //根据课程id查询course_base
        Optional<CourseBase> baseOptional = courseBaseRepository.findById(id);
        if (baseOptional.isPresent()) {
            CourseBase courseBase = baseOptional.get();
            //将courseBase属性拷贝到CoursePub中
            BeanUtils.copyProperties(courseBase, coursePub);
        }

        //查询课程图片
        Optional<CoursePic> picOptional = coursePicRepository.findById(id);
        if (picOptional.isPresent()) {
            CoursePic coursePic = picOptional.get();
            BeanUtils.copyProperties(coursePic, coursePub);
        }

        //课程营销信息
        Optional<CourseMarket> marketOptional = courseMarketRepository.findById(id);
        if (marketOptional.isPresent()) {
            CourseMarket courseMarket = marketOptional.get();
            BeanUtils.copyProperties(courseMarket, coursePub);
        }
        //课程计划信息
        TeachplanNode teachplanNode = teachplanMapper.selectList(id);
        String jsonString = JSON.toJSONString(teachplanNode);
        coursePub.setTeachplan(jsonString);
        return coursePub;
    }

    //更新课程发布状态
    private CourseBase saveCoursePubState(String courseId) {
        CourseBase courseBase = this.findCourseBaseById(courseId);
        //更新发布状态
        courseBase.setStatus("202002");
        return courseBaseRepository.save(courseBase);

    }

    //保存课程计划与媒资文件关联
    public ResponseResult savemedia(TeachplanMedia teachplanMedia) {
        if (teachplanMedia == null || StringUtils.isEmpty(teachplanMedia.getTeachplanId())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //检验课程计划是否三级
        String teachplanId = teachplanMedia.getTeachplanId();
        //查询课程计划
        Optional<Teachplan> optional = teachplanRepository.findById(teachplanId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //查询到教学计划
        Teachplan teachplan = optional.get();
        //取出等级
        String grade = teachplan.getGrade();
        if (StringUtils.isEmpty(grade) || !grade.equals("3")) {
            //只允许选择第三级的课程计划关联视频
            ExceptionCast.cast(CourseCode.COURSE_MEDIA_TEACHPLAN_GRADEERROR);
        }
        //查询teachplanMedia
        Optional<TeachplanMedia> mediaOptional = teachplanMediaRepository.findById(teachplanId);
        TeachplanMedia one = null;
        if (mediaOptional.isPresent()) {
            one = mediaOptional.get();
        } else {
            one = new TeachplanMedia();
        }

        //将one保存到数据库
        one.setCourseId(teachplan.getCourseid());//课程id
        one.setMediaId(teachplanMedia.getMediaId());//媒资文件的id
        one.setMediaFileOriginalName(teachplanMedia.getMediaFileOriginalName());//媒资文件的原始名称
        one.setMediaUrl(teachplanMedia.getMediaUrl());//媒资文件的url
        one.setTeachplanId(teachplanId);
        teachplanMediaRepository.save(one);

        return new ResponseResult(CommonCode.SUCCESS);

    }

    //添加课程提交
    @Transactional
    public AddCourseResult addCourseBase(CourseBase courseBase) {
        //课程状态默认为未发布
        courseBase.setStatus("202001");
        courseBaseRepository.save(courseBase);
        return new AddCourseResult(CommonCode.SUCCESS, courseBase.getId());
    }
}
