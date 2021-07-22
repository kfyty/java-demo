package com.kfyty.table.struct.export.mapper;

import com.kfyty.mybatis.auto.mapper.BaseMapper;
import com.kfyty.mybatis.auto.mapper.annotation.AutoMapper;
import com.kfyty.table.struct.export.entity.SysBoList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@AutoMapper(primaryKey = "id_")
public interface SysBoListMapper extends BaseMapper<String, SysBoList> {
    @AutoMapper(columns = "id_")
    List<String> findAllId();
}
