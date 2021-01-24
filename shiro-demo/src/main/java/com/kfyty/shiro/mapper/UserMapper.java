package com.kfyty.shiro.mapper;

import com.kfyty.mybatis.auto.mapper.BaseMapper;
import com.kfyty.mybatis.auto.mapper.annotation.AutoMapper;
import com.kfyty.shiro.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@AutoMapper(entity = User.class)
public interface UserMapper extends BaseMapper<Integer, User> {
    @AutoMapper
    User findByUsername(@Param("username") String username);
}
