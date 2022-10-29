package com.kfyty.demo.utils;

import com.kfyty.database.generator.info.AbstractFieldStructInfo;
import com.kfyty.database.generator.info.AbstractTableStructInfo;
import com.kfyty.database.generator.mapper.AbstractDatabaseMapper;
import com.kfyty.database.jdbc.session.Configuration;
import com.kfyty.database.jdbc.session.SqlSessionProxyFactory;
import com.kfyty.core.utils.CommonUtil;
import lombok.Data;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2020/12/17 17:42
 * @email kfyty725@hotmail.com
 */
@Data
public class TableStructContrast {
    private String sourceDatabase;
    private String targetDatabase;
    private DataSource source;
    private DataSource target;
    private Class<? extends AbstractDatabaseMapper> sourceDatabaseMapper;
    private Class<? extends AbstractDatabaseMapper> targetDatabaseMapper;

    public List<Map<String, AbstractTableStructInfo>> doContrast() {
        this.check();
        Map<String, AbstractTableStructInfo> sourceInfo = loadTableInfo(sourceDatabase, source, sourceDatabaseMapper);
        Map<String, AbstractTableStructInfo> targetInfo = loadTableInfo(targetDatabase, target, targetDatabaseMapper);
        for (Iterator<Map.Entry<String, AbstractTableStructInfo>> i = targetInfo.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry<String, AbstractTableStructInfo> target = i.next();
            if(sourceInfo.containsKey(target.getKey())) {
                Map<String, AbstractFieldStructInfo> sourceFieldInfo = convertFieldInfo(sourceInfo.get(target.getKey()));
                Map<String, AbstractFieldStructInfo> targetFieldInfo = convertFieldInfo(target.getValue());
                for (Iterator<Map.Entry<String, AbstractFieldStructInfo>> j = targetFieldInfo.entrySet().iterator(); j.hasNext(); ) {
                    Map.Entry<String, AbstractFieldStructInfo> targetField = j.next();
                    if(sourceFieldInfo.containsKey(targetField.getKey())) {
                        sourceFieldInfo.remove(targetField.getKey());
                        j.remove();
                    }
                }
                if(targetFieldInfo.isEmpty()) {
                    i.remove();
                } else {
                    targetInfo.get(target.getKey()).setFieldInfos(new ArrayList<>(targetFieldInfo.values()));
                }
                if(sourceFieldInfo.isEmpty()) {
                    sourceInfo.remove(target.getKey());
                } else {
                    sourceInfo.get(target.getKey()).setFieldInfos(new ArrayList<>(sourceFieldInfo.values()));
                }
            }
        }
        return Arrays.asList(sourceInfo, targetInfo);
    }

    private Map<String, AbstractTableStructInfo> loadTableInfo(String database, DataSource dataSource, Class<? extends AbstractDatabaseMapper> dataBaseMapper) {
        SqlSessionProxyFactory sqlSessionProxyFactory = new SqlSessionProxyFactory(new Configuration().setDataSource(dataSource));
        AbstractDatabaseMapper mapper = sqlSessionProxyFactory.createProxy(dataBaseMapper);
        List<? extends AbstractTableStructInfo> dataBaseInfo = mapper.findTableInfos(database);
        dataBaseInfo.forEach(e -> e.setFieldInfos(mapper.findFieldInfos(database, e.getTableName())));
        return ((List<AbstractTableStructInfo>) dataBaseInfo)
                .stream()
                .collect(Collectors.toMap(e -> e.getTableName().toUpperCase(), v -> v));
    }

    private Map<String, AbstractFieldStructInfo> convertFieldInfo(AbstractTableStructInfo tableInfo) {
        if(CommonUtil.empty(tableInfo.getFieldInfos())) {
            return new HashMap<>();
        }
        return (((List<AbstractFieldStructInfo>) tableInfo.getFieldInfos())
                .stream()
                .collect(Collectors.toMap(e -> e.getField().toUpperCase(), v -> v)));
    }

    private void check() {
        if(CommonUtil.empty(sourceDatabase)) {
            throw new IllegalArgumentException("source database can't null !");
        }
        if(CommonUtil.empty(targetDatabase)) {
            throw new IllegalArgumentException("target database can't null !");
        }
        if(source == null) {
            throw new IllegalArgumentException("source data source can't null !");
        }
        if(target == null) {
            throw new IllegalArgumentException("target data source can't null !");
        }
        if(sourceDatabaseMapper == null) {
            throw new IllegalArgumentException("source database mapper can't null !");
        }
        if(targetDatabaseMapper == null) {
            throw new IllegalArgumentException("target database mapper can't null !");
        }
    }
}
