package com.kfyty.demo.codegen;

import com.kfyty.database.generate.configuration.GenerateConfiguration;
import com.kfyty.database.generate.configuration.annotation.BasePackage;
import com.kfyty.database.generate.configuration.annotation.DataBase;
import com.kfyty.database.generate.configuration.annotation.DataBaseMapper;
import com.kfyty.database.generate.configuration.annotation.FilePath;
import com.kfyty.database.generate.configuration.annotation.Table;
import com.kfyty.database.generate.database.MySQLDataBaseMapper;
import com.kfyty.support.autoconfig.annotation.Autowired;
import com.kfyty.support.autoconfig.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@DataBase("test")
@Table("test")
@FilePath("D:/temp/generate")
@BasePackage("com.kfyty")
@DataBaseMapper(MySQLDataBaseMapper.class)
public class GenerateCodeConfig implements GenerateConfiguration {
    @Autowired
    private DataSource dataSource;

    @Override
    public DataSource getDataSource() {
        return this.dataSource;
    }
}
