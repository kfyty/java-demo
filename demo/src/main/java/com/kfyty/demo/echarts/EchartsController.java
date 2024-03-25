package com.kfyty.demo.echarts;

import com.kfyty.core.support.Pair;
import com.kfyty.demo.echarts.vo.EchartsNestedPieVo;
import com.kfyty.demo.echarts.vo.EchartsPairDimensionVo;
import com.kfyty.demo.utils.EchartsUtil;
import com.kfyty.demo.utils.Triple;
import com.kfyty.mvc.annotation.GetMapping;
import com.kfyty.mvc.annotation.RequestMapping;
import com.kfyty.mvc.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 描述: 提供 echarts demo 数据
 *
 * @author kfyty725
 * @date 2021/7/1 14:05
 * @email kfyty725@hotmail.com
 */
@RestController
@RequestMapping("echarts")
public class EchartsController {
    /**
     * 简单的单维度图表数据
     */
    @GetMapping("simple")
    public List<Pair<String, String>> simple() {
        return Stream.iterate(1, i -> i + 1).limit(10).map(e -> new Pair<>("name" + e, e.toString())).collect(Collectors.toList());
    }

    /**
     * 双维度图表数据
     */
    @GetMapping("complex")
    public EchartsPairDimensionVo complex() {
        List<Triple<String, String, String>> data = new ArrayList<>();
        data.add(new Triple<>("银河系", "男", "300"));
        data.add(new Triple<>("银河系", "女", "200"));
        data.add(new Triple<>("仙女座星系", "男", "200"));
        data.add(new Triple<>("仙女座星系", "女", "500"));
        data.add(new Triple<>("仙男座星系", "男", "250"));
        data.add(new Triple<>("仙男座星系", "女", "100"));
        return EchartsUtil.convertPairDimension(data);
    }

    /**
     * 二级嵌套饼图表数据
     */
    @GetMapping("nestedPie")
    public EchartsNestedPieVo nestedPie() {
        List<Triple<String, String, Number>> data = new ArrayList<>();
        data.add(new Triple<>("直达", "直达", 335));
        data.add(new Triple<>("营销广告", "邮件营销", 100));
        data.add(new Triple<>("营销广告", "联盟广告", 200));
        data.add(new Triple<>("营销广告", "视频广告", 300));
        data.add(new Triple<>("搜索引擎", "百度", 500));
        data.add(new Triple<>("搜索引擎", "谷歌", 300));
        data.add(new Triple<>("搜索引擎", "必应", 200));
        return EchartsUtil.convertNestedPie(data);
    }
}
