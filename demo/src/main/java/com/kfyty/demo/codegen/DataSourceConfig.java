package com.kfyty.demo.codegen;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.kfyty.configuration.annotation.Bean;
import com.kfyty.configuration.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 功能描述: 数据源配置
 *
 * @author kfyty725@hotmail.com
 * @date 2019/9/07 21:38
 * @since JDK 1.8
 */
@Slf4j
@Configuration
public class DataSourceConfig {

    private static final String PATH = "/druid.properties";

    @Bean
    public DataSource getDruidDataSource() {
        try {
            Properties properties = new Properties();
            properties.load(DataSourceConfig.class.getResourceAsStream(PATH));
            return DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            log.error("load properties failed:", e);
        }
        return null;
    }
}
