package com.kfyty.elasticsearch.entity.base;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/3/3 10:44
 * @email kfyty725@hotmail.com
 */
@Data
public class Entity {
    @Id
    private Integer id;

    @Field(type = FieldType.Integer)
    private Integer objectId;
}
