package com.kfyty.security.mapper;

import com.kfyty.mybatis.auto.mapper.BaseMapper;
import com.kfyty.mybatis.auto.mapper.annotation.AutoMapper;
import com.kfyty.security.entity.RolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@AutoMapper(entity = RolePermission.class)
public interface RolePermissionMapper extends BaseMapper<Integer, RolePermission> {
    @AutoMapper
    void deleteByRoleId(@Param("roleId") Integer roleId);
}
