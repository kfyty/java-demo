package com.kfyty.dynamic.mapper.sso;

import com.kfyty.dynamic.entity.sso.User;
import com.kfyty.mybatis.auto.mapper.BaseMapper;
import com.kfyty.mybatis.auto.mapper.annotation.AutoMapper;
import com.kfyty.mybatis.auto.mapper.annotation.SelectKey;

@SelectKey
@AutoMapper(entity = User.class)
public interface UserMapper extends BaseMapper<Integer, User> {
}
