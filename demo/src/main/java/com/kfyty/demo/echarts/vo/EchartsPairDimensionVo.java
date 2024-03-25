package com.kfyty.demo.echarts.vo;

import com.kfyty.core.support.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 描述: 双维度图表数据
 *
 * @author kfyty725
 * @date 2021/7/1 14:39
 * @email kfyty725@hotmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EchartsPairDimensionVo {
    private List<String> legend;
    private List<String> xAxis;
    private List<Pair<String, List<String>>> series;
}
