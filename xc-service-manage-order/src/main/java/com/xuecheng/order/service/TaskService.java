package com.xuecheng.order.service;

import com.netflix.discovery.converters.Auto;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.order.dao.XcTaskHisRepository;
import com.xuecheng.order.dao.XcTaskRepository;
import org.hibernate.engine.jdbc.Size;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author lpt
 * @Date 2019/5/15 10:39
 * @Version 1.0
 */
@Service
public class TaskService {

    @Autowired
    XcTaskRepository xcTaskRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    XcTaskHisRepository xcTaskHisRepository;


    //取出前n条任务,取出指定时间之前处理的任务
    public List<XcTask> findTaskList(Date updateTime, int Size) {
        //设置分页
        Pageable pageRequest = new PageRequest(0, Size);
        //查询前n前纪录
        Page<XcTask> all = xcTaskRepository.findByUpdateTimeBefore(pageRequest, updateTime);
        return all.getContent();
    }


    public void publish(XcTask xcTask, String ex, String routingKey) {
        Optional<XcTask> optional = xcTaskRepository.findById(xcTask.getId());
        if (optional.isPresent()) {
            rabbitTemplate.convertAndSend(ex, routingKey, xcTask);
            //更新任务时间
            XcTask xcTaskUpdate = optional.get();
            xcTaskUpdate.setUpdateTime(new Date());
            xcTaskRepository.save(xcTaskUpdate);
        }

    }

    @Transactional
    public int getTask(String taskId, int version) {
        return xcTaskRepository.updateTaskVersion(taskId, version);
    }

    @Transactional
    public void finishTask(String taskId) {
        Optional<XcTask> optional = xcTaskRepository.findById(taskId);
        if (optional.isPresent()) {
            XcTask xcTask = optional.get();
            XcTaskHis xcTaskHis = new XcTaskHis();
            BeanUtils.copyProperties(xcTask, xcTaskHis);
            xcTaskHisRepository.save(xcTaskHis);
            xcTaskRepository.delete(xcTask);

        }

    }


}
