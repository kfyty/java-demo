package com.kfyty.security.mapper;

import com.kfyty.mybatis.auto.mapper.BaseMapper;
import com.kfyty.security.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Integer, Permission> {
    List<Permission> findByUserId(@Param("userId") Integer userId);
}
