package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.midi.SysexMessage;

/**
 * @Author lpt
 * @Date 2019/4/4 9:31
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SysDictionaryTest {

    @Autowired
    SysDictionaryRepository sysDictionaryRepository;

    @Test
    public void testDictinoary() {
        SysDictionary sysDictionary = sysDictionaryRepository.findSysDictionaryByDType("200");
        System.out.println(sysDictionary);
    }


}
