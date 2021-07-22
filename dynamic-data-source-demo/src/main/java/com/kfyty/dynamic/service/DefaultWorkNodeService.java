package com.kfyty.dynamic.service;

import com.baidu.fsg.uid.worker.dao.WorkerNodeService;
import com.baidu.fsg.uid.worker.entity.WorkerNodeEntity;
import com.kfyty.dynamic.mapper.test.WorkNodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/7/22 14:54
 * @email kfyty725@hotmail.com
 */
@Service
public class DefaultWorkNodeService implements WorkerNodeService {
    @Autowired
    private WorkNodeMapper workNodeMapper;

    @Override
    public int addWorkNode(WorkerNodeEntity workerNodeEntity) {
        return this.workNodeMapper.insert(workerNodeEntity);
    }

    @Override
    public WorkerNodeEntity findByHostPort(String host, String port) {
        return this.workNodeMapper.findByHostAndPort(host, port);
    }
}
