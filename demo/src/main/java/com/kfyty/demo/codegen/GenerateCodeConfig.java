package com.kfyty.demo.codegen;

import com.kfyty.database.generator.config.GeneratorConfigurationSupport;
import com.kfyty.database.generator.config.annotation.BasePackage;
import com.kfyty.database.generator.config.annotation.Database;
import com.kfyty.database.generator.config.annotation.DatabaseMapper;
import com.kfyty.database.generator.config.annotation.FilePath;
import com.kfyty.database.generator.config.annotation.Table;
import com.kfyty.database.generator.mapper.MySQLDatabaseMapper;
import com.kfyty.support.autoconfig.annotation.Autowired;
import com.kfyty.support.autoconfig.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@Database("test")
@Table("test")
@FilePath("D:/temp/generate")
@BasePackage("com.kfyty")
@DatabaseMapper(MySQLDatabaseMapper.class)
public class GenerateCodeConfig implements GeneratorConfigurationSupport {
    @Autowired
    private DataSource dataSource;

    @Override
    public DataSource getDataSource() {
        return this.dataSource;
    }
}
