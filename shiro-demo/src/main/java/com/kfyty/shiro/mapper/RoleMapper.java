package com.kfyty.shiro.mapper;

import com.kfyty.mybatis.auto.mapper.BaseMapper;
import com.kfyty.mybatis.auto.mapper.annotation.AutoMapper;
import com.kfyty.shiro.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@AutoMapper(entity = Role.class)
public interface RoleMapper extends BaseMapper<Integer, Role> {
    List<Role> findByUserId(Integer userId);
}
