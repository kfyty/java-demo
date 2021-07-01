package com.kfyty.demo.echarts.vo;

import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 描述: 二级嵌套饼图表数据
 *
 * @author kfyty725
 * @date 2021/7/1 14:39
 * @email kfyty725@hotmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EchartsNestedPieVo {
    private List<String> legend;
    private List<List<Pair<String, Number>>> series;
}
