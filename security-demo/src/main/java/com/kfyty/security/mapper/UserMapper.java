package com.kfyty.security.mapper;

import com.kfyty.mybatis.auto.mapper.BaseMapper;
import com.kfyty.mybatis.auto.mapper.annotation.AutoMapper;
import com.kfyty.security.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<Integer, User> {
    @AutoMapper
    User findByUsername(@Param("username") String username);
}
