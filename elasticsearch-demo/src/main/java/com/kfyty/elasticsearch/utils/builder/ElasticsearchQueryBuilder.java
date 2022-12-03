package com.kfyty.elasticsearch.utils.builder;

import com.kfyty.core.wrapper.function.SerializableFunction;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.Objects;

import static com.kfyty.core.utils.SerializableLambdaUtil.resolveFieldName;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/3/2 19:00
 * @email kfyty725@hotmail.com
 */
public abstract class ElasticsearchQueryBuilder<T> {
    protected final String indexName;
    protected final String type;
    protected final BoolQueryBuilder boolQueryBuilder;

    private Pageable pageable = Pageable.unpaged();

    public ElasticsearchQueryBuilder(Class<T> clazz) {
        Objects.requireNonNull(clazz);
        Document document = clazz.getAnnotation(Document.class);
        if(document == null) {
            throw new IllegalArgumentException("class must have Document annotation !");
        }
        this.indexName = document.indexName();
        this.type = document.type();
        this.boolQueryBuilder = QueryBuilders.boolQuery();
    }

    public static <T> ElasticsearchAndQueryBuilder<T> and(Class<T> clazz) {
        return new ElasticsearchAndQueryBuilder<>(clazz);
    }

    public static <T> ElasticsearchOrQueryBuilder<T> or(Class<T> clazz) {
        return new ElasticsearchOrQueryBuilder<>(clazz);
    }

    public final NativeSearchQuery build() {
        return new NativeSearchQueryBuilder().withQuery(this.boolQueryBuilder).withPageable(pageable).build();
    }

    public ElasticsearchQueryBuilder<T> withPageable(Pageable pageable) {
        if(pageable != null) {
            this.pageable = pageable;
        }
        return this;
    }

    public ElasticsearchQueryBuilder<T> idsIn(String ... value) {
        if(value == null || value.length < 1) {
            return this;
        }
        return this.buildQuery(QueryBuilders.idsQuery(type).addIds(value));
    }

    public ElasticsearchQueryBuilder<T> in(SerializableFunction<T, ?> field, Object ... value) {
        if(value == null || value.length < 1) {
            return this;
        }
        return this.buildQuery(QueryBuilders.termsQuery(resolveFieldName(field), value));
    }

    public ElasticsearchQueryBuilder<T> eq(SerializableFunction<T, ?> field, Object value) {
        if(value == null) {
            return this;
        }
        return this.buildQuery(QueryBuilders.matchPhraseQuery(resolveFieldName(field), value));
    }

    public ElasticsearchQueryBuilder<T> contains(SerializableFunction<T, ?> field, Object value) {
        if(value == null) {
            return this;
        }
        return this.buildQuery(QueryBuilders.wildcardQuery(resolveFieldName(field), "*" + value + "*"));
    }

    public ElasticsearchQueryBuilder<T> gt(SerializableFunction<T, ?> field, Object value) {
        if(value == null) {
            return this;
        }
        return this.buildQuery(QueryBuilders.rangeQuery(resolveFieldName(field)).gt(value));
    }

    public ElasticsearchQueryBuilder<T> gte(SerializableFunction<T, ?> field, Object value) {
        if(value == null) {
            return this;
        }
        return this.buildQuery(QueryBuilders.rangeQuery(resolveFieldName(field)).gte(value));
    }

    public ElasticsearchQueryBuilder<T> lt(SerializableFunction<T, ?> field, Object value) {
        if(value == null) {
            return this;
        }
        return this.buildQuery(QueryBuilders.rangeQuery(resolveFieldName(field)).lt(value));
    }

    public ElasticsearchQueryBuilder<T> lte(SerializableFunction<T, ?> field, Object value) {
        if(value == null) {
            return this;
        }
        return this.buildQuery(QueryBuilders.rangeQuery(resolveFieldName(field)).lte(value));
    }

    public ElasticsearchQueryBuilder<T> between(SerializableFunction<T, ?> field, Object start, Object end) {
        if(start == null || end == null) {
            return this;
        }
        return this.buildQuery(QueryBuilders.rangeQuery(resolveFieldName(field)).from(start).to(end));
    }

    protected abstract ElasticsearchQueryBuilder<T> buildQuery(QueryBuilder query);
}
