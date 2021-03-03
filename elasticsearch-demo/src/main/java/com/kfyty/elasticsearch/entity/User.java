package com.kfyty.elasticsearch.entity;

import com.kfyty.elasticsearch.entity.base.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/3/2 14:19
 * @email kfyty725@hotmail.com
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Document(indexName = "test", type = "user")
public class User extends Entity {

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Keyword)
    private String createTime;
}
