package com.kfyty.demo.utils;

import javafx.util.Pair;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 描述: 三个值
 *
 * @author kfyty725
 * @date 2021/7/01 13:53
 * @email kfyty725@hotmail.com
 */
@EqualsAndHashCode(callSuper = true)
public class Triple<K, V, TP> extends Pair<K, V> {
    @Getter
    private final TP triple;

    /**
     * Creates a new pair
     *
     * @param key   The key for this pair
     * @param value The value to use for this pair
     */
    public Triple(K key, V value) {
        this(key, value, null);
    }

    public Triple(K key, V value, TP triple) {
        super(key, value);
        this.triple = triple;
    }

    @Override
    public String toString() {
        return super.toString() + "(triple=" + this.triple + ")";
    }
}
