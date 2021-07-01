package com.kfyty.demo.utils;

import com.kfyty.demo.echarts.vo.EchartsNestedPieVo;
import com.kfyty.demo.echarts.vo.EchartsPairDimensionVo;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 描述: echarts 数据转换工具
 *
 * @author kfyty725
 * @date 2021/7/1 13:51
 * @email kfyty725@hotmail.com
 */
public abstract class EchartsUtil {

    public static EchartsPairDimensionVo convertPairDimension(List<Triple<String, String, String>> source) {
        Set<String> outerDimensions = new LinkedHashSet<>();
        Set<String> innerDimensions = new LinkedHashSet<>();
        for (Triple<String, String, String> triple : source) {
            outerDimensions.add(triple.getKey());
            innerDimensions.add(triple.getValue());
        }
        List<Pair<String, List<String>>> data = new ArrayList<>();
        for (String innerDimension : innerDimensions) {
            List<String> items = new ArrayList<>();
            for (String outerDimension : outerDimensions) {
                Optional<Triple<String, String, String>> triple = source.parallelStream().filter(e -> Objects.equals(e.getKey(), outerDimension) && Objects.equals(e.getValue(), innerDimension)).findAny();
                items.add(triple.isPresent() ? triple.get().getTriple() : "");
            }
            data.add(new Pair<>(innerDimension, items));
        }
        return new EchartsPairDimensionVo(new ArrayList<>(innerDimensions), new ArrayList<>(outerDimensions), data);
    }

    public static EchartsNestedPieVo convertNestedPie(List<Triple<String, String, Number>> source) {
        Map<String, List<Triple<String, String, Number>>> groups = source.stream().collect(Collectors.groupingBy(Pair::getKey, LinkedHashMap::new, Collectors.toList()));
        List<String> legend = new ArrayList<>(groups.keySet());
        List<Pair<String, Number>> innerData = new ArrayList<>();
        List<Pair<String, Number>> outerData = new ArrayList<>();
        for (Map.Entry<String, List<Triple<String, String, Number>>> entry : groups.entrySet()) {
            Double sum = source.parallelStream().filter(e -> Objects.equals(e.getKey(), entry.getKey())).map(e -> e.getTriple().doubleValue()).reduce(Double::sum).orElse(0D);
            innerData.add(new Pair<>(entry.getKey(), sum));
            outerData.addAll(entry.getValue().stream().peek(e -> legend.add(e.getValue())).map(e -> new Pair<>(e.getValue(), e.getTriple())).collect(Collectors.toList()));
        }
        return new EchartsNestedPieVo(legend, Arrays.asList(innerData, outerData));
    }
}
