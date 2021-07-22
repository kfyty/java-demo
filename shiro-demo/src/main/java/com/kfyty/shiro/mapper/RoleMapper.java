package com.kfyty.shiro.mapper;

import com.kfyty.mybatis.auto.mapper.BaseMapper;
import com.kfyty.shiro.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Integer, Role> {
    List<Role> findByUserId(Integer userId);
}
