package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author lpt
 * @Date 2019/5/5 20:16
 * @Version 1.0
 */
public interface XcUserRepository extends JpaRepository<XcUser, String> {
    //根据账号查询用户信息
    XcUser findByUsername(String username);
}
