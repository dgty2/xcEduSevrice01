package com.xuecheng.order.dao;

import com.xuecheng.framework.domain.task.XcTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * @Author lpt
 * @Date 2019/5/15 10:36
 * @Version 1.0
 */
public interface XcTaskRepository extends JpaRepository<XcTask, String> {
    //取出指定时间之前的记录
    Page<XcTask> findByUpdateTimeBefore(Pageable pageable, Date updateTime);

    //更新updateTime
    @Modifying
    @Query("update XcTask t set t.updateTime= :updateTime where t.id = :id")
    public int updateTaskTime(@Param("id") String id, @Param("updateTime") Date updateTime);

    @Modifying
    @Query("update XcTask t set t.version = :version+1 where t.id = :id and t.version = :version")
    public int updateTaskVersion(@org.springframework.data.repository.query.Param(value = "id") String id, @Param(value = "version") int version);

}
