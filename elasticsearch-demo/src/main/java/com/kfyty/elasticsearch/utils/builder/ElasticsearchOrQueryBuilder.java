package com.kfyty.elasticsearch.utils.builder;

import com.kfyty.elasticsearch.utils.SerializableFunction;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Pageable;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/3/3 17:01
 * @email kfyty725@hotmail.com
 */
public class ElasticsearchOrQueryBuilder<T> extends ElasticsearchQueryBuilder<T> {
    public ElasticsearchOrQueryBuilder(Class<T> clazz) {
        super(clazz);
    }

    public ElasticsearchOrQueryBuilder<T> or(ElasticsearchQueryBuilder<T> query) {
        return this.or(query.boolQueryBuilder);
    }

    public ElasticsearchOrQueryBuilder<T> or(QueryBuilder query) {
        return this.buildQuery(query);
    }

    @Override
    protected ElasticsearchOrQueryBuilder<T> buildQuery(QueryBuilder query) {
        boolQueryBuilder.should(query);
        return this;
    }

    @Override
    public ElasticsearchOrQueryBuilder<T> withPageable(Pageable pageable) {
        super.withPageable(pageable);
        return this;
    }

    @Override
    public ElasticsearchOrQueryBuilder<T> idsIn(String... value) {
        super.idsIn(value);
        return this;
    }

    @Override
    public ElasticsearchOrQueryBuilder<T> in(SerializableFunction<T, ?> field, Object... value) {
        super.in(field, value);
        return this;
    }

    @Override
    public ElasticsearchOrQueryBuilder<T> eq(SerializableFunction<T, ?> field, Object value) {
        super.eq(field, value);
        return this;
    }

    @Override
    public ElasticsearchOrQueryBuilder<T> contains(SerializableFunction<T, ?> field, Object value) {
        super.contains(field, value);
        return this;
    }

    @Override
    public ElasticsearchOrQueryBuilder<T> gt(SerializableFunction<T, ?> field, Object value) {
        super.gt(field, value);
        return this;
    }

    @Override
    public ElasticsearchOrQueryBuilder<T> gte(SerializableFunction<T, ?> field, Object value) {
        super.gte(field, value);
        return this;
    }

    @Override
    public ElasticsearchOrQueryBuilder<T> lt(SerializableFunction<T, ?> field, Object value) {
        super.lt(field, value);
        return this;
    }

    @Override
    public ElasticsearchOrQueryBuilder<T> lte(SerializableFunction<T, ?> field, Object value) {
        super.lte(field, value);
        return this;
    }

    @Override
    public ElasticsearchOrQueryBuilder<T> between(SerializableFunction<T, ?> field, Object start, Object end) {
        super.between(field, start, end);
        return this;
    }
}
