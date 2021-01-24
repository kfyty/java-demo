package com.kfyty.upload.mapper;

import com.kfyty.mybatis.auto.mapper.BaseMapper;
import com.kfyty.mybatis.auto.mapper.annotation.AutoMapper;
import com.kfyty.mybatis.auto.mapper.annotation.SelectKey;
import com.kfyty.upload.pojo.FilePojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@SelectKey
@AutoMapper(entity = FilePojo.class)
public interface FileMapper extends BaseMapper<Integer, FilePojo> {
    @AutoMapper
    int updateByIdSetPathAndSize(@Param("id") Integer id, @Param("path") String path, @Param("size") Long size);

    @AutoMapper
    int deleteByParent(@Param("parent") Integer parent);

    @AutoMapper
    List<Integer> findPatchIndexByParent(@Param("parent") Integer parent);

    @AutoMapper
    FilePojo findByMd5(@Param("md5") String md5);

    @AutoMapper
    FilePojo findByParentAndMd5(@Param("parent") Integer parent, @Param("md5") String md5);

    @AutoMapper
    List<FilePojo> findByParentOrderByPatchIndexAsc(@Param("parent") Integer parent);
}
