package com.xuecheng.learning.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.learning.config.RabbitMQConfig;
import com.xuecheng.learning.service.LearningService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @Author lpt
 * @Date 2019/5/17 12:34
 * @Version 1.0
 */
@Component
public class ChooseCourseTask {

    @Autowired
    LearningService learningService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.XC_LEARNING_ADDCHOOSECOURSE)
    public void receiveChooseCourseTask(XcTask xcTask) {
        String userId = null;
        String courseId = null;
        try {
            String requestBody = xcTask.getRequestBody();
            Map map = JSON.parseObject(requestBody, Map.class);
            userId = (String) map.get("userId");
            courseId = (String) map.get("courseId");
            String valid = (String) map.get("valid");
            Date startTime = null;
            Date endTime = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY‐MM‐dd HH:mm:ss");
            if (map.get("startTime") != null) {
                startTime = dateFormat.parse((String) map.get("startTime"));
            }
            if (map.get("endTime") != null) {
                endTime = dateFormat.parse((String) map.get("endTime"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //添加选课
        ResponseResult addcourse = learningService.addcourse(userId, courseId, null, null, null, xcTask);
        if (addcourse.isSuccess()) {
            //添加选课成功，要向mq发送完成添加选课的消息
            rabbitTemplate.convertAndSend(RabbitMQConfig.EX_LEARNING_ADDCHOOSECOURSE, RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE_KEY, xcTask);
        }


    }

}
