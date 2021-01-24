package com.kfyty.dynamic.service;

import com.kfyty.dynamic.entity.sso.User;
import com.kfyty.dynamic.entity.test.Test;
import com.kfyty.dynamic.mapper.sso.UserMapper;
import com.kfyty.dynamic.mapper.test.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DynamicDataSourceService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TestMapper testMapper;

    @Transactional("ssoTransactionManager")
    public void save() {
        User user = User.newUser();
        Test test = Test.newTest();
        userMapper.insert(user);
        testMapper.insert(test);
    }
}
