package com.xuecheng.filesystem.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author lpt
 * @Date 2019/4/8 14:49
 * @Version 1.0
 */
public interface FileSystemRepository extends MongoRepository<FileSystem, String> {
}
