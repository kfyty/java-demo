package com.kfyty.elasticsearch.utils.builder;

import lombok.SneakyThrows;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/3/3 10:28
 * @email kfyty725@hotmail.com
 */
public class UpdateBuilder extends UpdateQueryBuilder {
    private final Document document;

    public UpdateBuilder(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        this.document = clazz.getAnnotation(Document.class);
        if(document == null) {
            throw new IllegalArgumentException("class must have Document annotation !");
        }
        this.withClass(clazz)
                .withIndexName(document.indexName())
                .withType(document.type());
    }

    public static UpdateBuilder builder(Class<?> clazz) {
        return new UpdateBuilder(clazz);
    }

    @SneakyThrows
    public UpdateBuilder withEntity(Object entity) {
        Objects.requireNonNull(entity);
        StringBuilder id = new StringBuilder();
        UpdateRequest updateRequest = new UpdateRequest(document.indexName(), document.type(), null);
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder().startObject();
        ReflectionUtils.doWithFields(entity.getClass(), field -> {
            field.setAccessible(true);
            Object value = ReflectionUtils.getField(field, entity);
            if(value == null) {
                return;
            }
            try {
                xContentBuilder.field(field.getName(), value);
                if(field.isAnnotationPresent(Id.class)) {
                    id.append(value);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        xContentBuilder.endObject();
        updateRequest
                .id(id.toString())
                .doc(xContentBuilder);
        this.withId(updateRequest.id()).withUpdateRequest(updateRequest);
        return this;
    }
}
