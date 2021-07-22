package com.kfyty.table.struct.export.mapper;

import com.kfyty.mybatis.auto.mapper.BaseMapper;
import com.kfyty.mybatis.auto.mapper.annotation.AutoMapper;
import com.kfyty.table.struct.export.entity.ActGeBytearray;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@AutoMapper(primaryKey = "id_")
public interface ActGeBytearrayMapper extends BaseMapper<String, ActGeBytearray> {
}
