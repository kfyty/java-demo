package com.kfyty.elasticsearch.utils.builder;

import com.kfyty.core.wrapper.function.SerializableFunction;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Pageable;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/3/3 16:59
 * @email kfyty725@hotmail.com
 */
public class ElasticsearchAndQueryBuilder<T> extends ElasticsearchQueryBuilder<T> {
    public ElasticsearchAndQueryBuilder(Class<T> clazz) {
        super(clazz);
    }

    public ElasticsearchAndQueryBuilder<T> and(ElasticsearchQueryBuilder<T> query) {
        return this.and(query.boolQueryBuilder);
    }

    public ElasticsearchAndQueryBuilder<T> and(QueryBuilder query) {
        return this.buildQuery(query);
    }

    @Override
    protected ElasticsearchAndQueryBuilder<T> buildQuery(QueryBuilder query) {
        boolQueryBuilder.must(query);
        return this;
    }

    @Override
    public ElasticsearchAndQueryBuilder<T> withPageable(Pageable pageable) {
        super.withPageable(pageable);
        return this;
    }

    @Override
    public ElasticsearchAndQueryBuilder<T> idsIn(String... value) {
        super.idsIn(value);
        return this;
    }

    @Override
    public ElasticsearchAndQueryBuilder<T> in(SerializableFunction<T, ?> field, Object... value) {
        super.in(field, value);
        return this;
    }

    @Override
    public ElasticsearchAndQueryBuilder<T> eq(SerializableFunction<T, ?> field, Object value) {
        super.eq(field, value);
        return this;
    }

    @Override
    public ElasticsearchAndQueryBuilder<T> contains(SerializableFunction<T, ?> field, Object value) {
        super.contains(field, value);
        return this;
    }

    @Override
    public ElasticsearchAndQueryBuilder<T> gt(SerializableFunction<T, ?> field, Object value) {
        super.gt(field, value);
        return this;
    }

    @Override
    public ElasticsearchAndQueryBuilder<T> gte(SerializableFunction<T, ?> field, Object value) {
        super.gte(field, value);
        return this;
    }

    @Override
    public ElasticsearchAndQueryBuilder<T> lt(SerializableFunction<T, ?> field, Object value) {
        super.lt(field, value);
        return this;
    }

    @Override
    public ElasticsearchAndQueryBuilder<T> lte(SerializableFunction<T, ?> field, Object value) {
        super.lte(field, value);
        return this;
    }

    @Override
    public ElasticsearchAndQueryBuilder<T> between(SerializableFunction<T, ?> field, Object start, Object end) {
        super.between(field, start, end);
        return this;
    }
}
