package com.kfyty.dynamic.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.kfyty.dynamic.mapper.sso", sqlSessionTemplateRef = "ssoSqlSessionTemplate")
public class SsoDataSourceConfig {

    @Bean("ssoDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid.sso")
    public DataSource ssoDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean("ssoSqlSessionFactory")
    public SqlSessionFactory ssoSqlSessionFactory(@Qualifier("ssoDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "ssoSqlSessionTemplate")
    public SqlSessionTemplate ssoSqlSessionTemplate(@Qualifier("ssoSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "ssoTransactionManager")
    public DataSourceTransactionManager ssoTransactionManager(@Qualifier("ssoDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
