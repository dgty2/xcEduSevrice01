package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author lpt
 * @Date 2019/4/4 9:04
 * @Version 1.0
 */

public interface SysDictionaryRepository extends MongoRepository<SysDictionary, String> {

    //根据字典分类查询
    SysDictionary findSysDictionaryByDType(String type);


}
