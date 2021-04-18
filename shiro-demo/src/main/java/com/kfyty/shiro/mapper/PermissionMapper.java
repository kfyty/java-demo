package com.kfyty.shiro.mapper;

import com.kfyty.mybatis.auto.mapper.annotation.AutoMapper;
import com.kfyty.shiro.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PermissionMapper {
    @AutoMapper
    List<Permission> findByPid(@Param("pid") Integer pid);

    List<Permission> findByUserId(@Param("userId") Integer userId);

    List<Permission> findByUserIdAndPid(@Param("userId") Integer userId, @Param("pid") Integer pid);
}
