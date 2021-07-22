package com.kfyty.dynamic.mapper.test;

import com.kfyty.dynamic.entity.test.Test;
import com.kfyty.mybatis.auto.mapper.BaseMapper;
import com.kfyty.mybatis.auto.mapper.annotation.SelectKey;

@SelectKey
public interface TestMapper extends BaseMapper<Integer, Test> {
}
