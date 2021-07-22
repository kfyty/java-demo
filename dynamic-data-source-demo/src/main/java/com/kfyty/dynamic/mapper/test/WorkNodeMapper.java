package com.kfyty.dynamic.mapper.test;

import com.baidu.fsg.uid.worker.entity.WorkerNodeEntity;
import com.kfyty.mybatis.auto.mapper.BaseMapper;
import com.kfyty.mybatis.auto.mapper.annotation.AutoMapper;
import com.kfyty.mybatis.auto.mapper.annotation.SelectKey;
import org.apache.ibatis.annotations.Param;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/7/22 14:01
 * @email kfyty725@hotmail.com
 */
@SelectKey
@AutoMapper(entity = WorkerNodeEntity.class, suffix = "Entity")
public interface WorkNodeMapper extends BaseMapper<Long, WorkerNodeEntity> {
    @AutoMapper
    WorkerNodeEntity findByHostAndPort(@Param("host") String host, @Param("port") String port);
}
