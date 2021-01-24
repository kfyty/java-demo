package com.kfyty.demo.codegen;

import com.kfyty.configuration.annotation.AutoWired;
import com.kfyty.configuration.annotation.Configuration;
import com.kfyty.generate.configuration.GenerateConfiguration;
import com.kfyty.generate.configuration.annotation.BasePackage;
import com.kfyty.generate.configuration.annotation.DataBase;
import com.kfyty.generate.configuration.annotation.DataBaseMapper;
import com.kfyty.generate.configuration.annotation.FilePath;
import com.kfyty.generate.configuration.annotation.Table;
import com.kfyty.generate.database.MySQLDataBaseMapper;

import javax.sql.DataSource;

@Configuration
@DataBase("test")
@Table("test")
@FilePath("D:/temp/generate")
@BasePackage("com.kfyty")
@DataBaseMapper(MySQLDataBaseMapper.class)
public class GenerateCodeConfig implements GenerateConfiguration {
    @AutoWired
    private DataSource dataSource;

    @Override
    public DataSource getDataSource() {
//        return this.dataSource;
        return new DataSourceConfig().getDruidDataSource();
    }
}
