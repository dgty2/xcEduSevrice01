package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.dao.SysDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author lpt
 * @Date 2019/4/4 9:15
 * @Version 1.0
 */
@Service
public class SysDictionaryService {

    @Autowired
    SysDictionaryRepository sysDictionaryRepository;

    public SysDictionary getByType(String type) {
        SysDictionary sysDictionary = sysDictionaryRepository.findSysDictionaryByDType(type);
        return sysDictionary;
    }
}
