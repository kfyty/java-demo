package com.kfyty.security.mapper;

import com.kfyty.mybatis.auto.mapper.BaseMapper;
import com.kfyty.mybatis.auto.mapper.annotation.AutoMapper;
import com.kfyty.security.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@AutoMapper(entity = UserRole.class)
public interface UserRoleMapper extends BaseMapper<Integer, UserRole> {
    @AutoMapper
    void deleteByUserId(@Param("userId") Integer userId);
}
