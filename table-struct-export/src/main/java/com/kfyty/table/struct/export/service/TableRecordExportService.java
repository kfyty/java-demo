package com.kfyty.table.struct.export.service;

import com.kfyty.mybatis.auto.mapper.BaseMapper;
import com.kfyty.mybatis.auto.mapper.struct.TableFieldStruct;
import com.kfyty.mybatis.auto.mapper.struct.TableStruct;
import com.kfyty.support.utils.CommonUtil;
import com.kfyty.support.utils.ReflectUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class TableRecordExportService<PrimaryKey, T, Mapper extends BaseMapper<PrimaryKey, T>> {
    private static final String DEFAULT_COLUMN_SUFFIX = "";
    private static final String DEFAULT_SAVE_PATH = "D:\\temp\\table\\record";
    private static final String DEFAULT_FILE_NAME = System.currentTimeMillis() + "_export.sql";
    private static final Map<String, Boolean> BIG_TEXT_TYPE = new LinkedCaseInsensitiveMap<>();

    static {
        BIG_TEXT_TYPE.put("clob", false);
        BIG_TEXT_TYPE.put("nclob", false);
        BIG_TEXT_TYPE.put("blob", true);
    }

    private String savePath;

    private String fileName;

    private Function<T, String> fileNameFun;

    private String database;

    private Mapper mapper;

    private String columnSuffix;

    private boolean append;

    private boolean sameStruct;

    private TableStruct cacheTableStruct;

    public TableRecordExportService(String database, Mapper mapper) {
        this.savePath = DEFAULT_SAVE_PATH;
        this.fileName = DEFAULT_FILE_NAME;
        this.database = database;
        this.mapper = mapper;
        this.columnSuffix = DEFAULT_COLUMN_SUFFIX;
        this.append = false;
        this.sameStruct = true;
    }

    public TableRecordExportService<PrimaryKey, T, Mapper> savePath(String savePath) {
        this.savePath = savePath;
        return this;
    }

    public TableRecordExportService<PrimaryKey, T, Mapper> fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public TableRecordExportService<PrimaryKey, T, Mapper> fileName(Function<T, String> fileNameFun) {
        this.fileNameFun = fileNameFun;
        return this;
    }

    public TableRecordExportService<PrimaryKey, T, Mapper> database(String database) {
        this.database = database;
        return this;
    }

    public TableRecordExportService<PrimaryKey, T, Mapper> mapper(Mapper mapper) {
        this.mapper = mapper;
        return this;
    }

    public TableRecordExportService<PrimaryKey, T, Mapper> columnSuffix(String columnSuffix) {
        this.columnSuffix = columnSuffix;
        return this;
    }

    public TableRecordExportService<PrimaryKey, T, Mapper> append(boolean append) {
        this.append = append;
        return this;
    }

    public TableRecordExportService<PrimaryKey, T, Mapper> sameStruct(boolean sameStruct) {
        this.sameStruct = sameStruct;
        return this;
    }

    public TableRecordExportService<PrimaryKey, T, Mapper> putBigTextType(String type) {
        BIG_TEXT_TYPE.put(type, null);
        return this;
    }

    public void recordExport(PrimaryKey pk, boolean insert) throws Exception {
        if(insert) {
            this.recordExportInsert(pk);
            return;
        }
        this.recordExportUpdate(pk);
    }

    public void recordExportInsert(PrimaryKey pk) throws Exception {
        ExportInfo info = this.initExportInfo(pk);
        if(info == null) {
            return;
        }
        String sql = String.format("declare\n %s begin\n %s INSERT INTO %s ( %s ) values (%s);\n end;\n / \n\n",
                        info.getDeclareSQL(),
                        info.getAssignmentSQL(),
                        info.getTableStruct().getTableName(),
                        String.join(", ", info.getFields()),
                        String.join(", ", info.getValues()));
        this.doSaveSQL(sql, info.getOut());
    }

    public void recordExportUpdate(PrimaryKey pk) throws Exception {
        ExportInfo info = this.initExportInfo(pk);
        if(info == null) {
            return;
        }
        Map<String, String> valueMapping = new HashMap<>();
        Set<String> pkSet = info.getPrimaryKey().stream().map(e -> CommonUtil.convert2Hump(e.getFieldName())).collect(Collectors.toSet());
        for (int i = 0; i < info.getFields().size(); i++) {
            valueMapping.put(info.getFields().get(i), info.getValues().get(i));
        }
        String setSQL = valueMapping.entrySet().stream().map(e -> e.getKey() + " = " + e.getValue()).collect(Collectors.joining(", "));
        String pkConditionSQL = valueMapping.entrySet().stream().filter(e -> pkSet.contains(e.getKey())).map(e -> e.getKey() + " = " + e.getValue()).collect(Collectors.joining(" and "));
        String sql = String.format("declare\n %s begin\n %s update %s set %s where %s;\n end;\n / \n\n",
                info.getDeclareSQL(),
                info.getAssignmentSQL(),
                info.getTableStruct().getTableName(),
                setSQL,
                pkConditionSQL);
        if(CommonUtil.empty(pkConditionSQL)) {
            throw new RuntimeException("No primary key condition found !");
        }
        this.doSaveSQL(sql, info.getOut());
    }

    private void doSaveSQL(String sql, File file) throws IOException {
        try(BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file, append))) {
            out.write(sql.getBytes(StandardCharsets.UTF_8));
            out.flush();
            log.info("generate sql for {} success !", fileName);
        }
    }

    private ExportInfo initExportInfo(PrimaryKey pk) throws Exception {
        this.beforeExportCheck();
        T entity = this.mapper.findByPk(pk);
        if(entity == null) {
            log.error("No record found of pk: " + pk + " !");
            return null;
        }
        File file = new File(this.savePath);
        if(!file.exists() && !file.mkdirs()) {
            throw new RuntimeException("Mkdirs file failed !");
        }
        if(this.fileNameFun != null) {
            this.fileName = this.fileNameFun.apply(entity) + ".sql";
        }
        file = new File(savePath, this.fileName);
        if(file.exists() && !append && !file.delete()) {
            throw new RuntimeException("Delete file failed !");
        }
        return new ExportInfo(entity, file);
    }

    private void beforeExportCheck() {
        if(CommonUtil.empty(savePath)) {
            this.savePath = DEFAULT_SAVE_PATH;
        }
        if(CommonUtil.empty(fileName)) {
            this.fileName = DEFAULT_FILE_NAME;
        }
        if(CommonUtil.empty(database)) {
            throw new RuntimeException("Please set database for export table !");
        }
        if(this.mapper == null) {
            throw new RuntimeException("Before export please set mapper !");
        }
    }

    private String convertSQL(String sql) {
        return sql.replace("'", "''");
    }

    private String bytesToHexString(byte[] src){
        if (src == null || src.length <= 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : src) {
            int v = b & 0xff;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    @Getter
    private class ExportInfo {
        private final T entity;
        private final File out;
        private final TableStruct tableStruct;
        private final List<TableFieldStruct> primaryKey;
        private final Map<String, String> bigTextVar;
        private final Map<String, String> bigTextSQL;
        private final Map<String, TableFieldStruct> bigTextField;
        private final Map<String, Field> fieldMap;
        private final StringBuilder declareSQL;
        private final StringBuilder assignmentSQL;
        private final List<String> fields;
        private final List<String> values;

        public ExportInfo(T entity, File out) throws Exception {
            this.entity = entity;
            this.out = out;
            this.tableStruct = sameStruct && cacheTableStruct != null
                                    ? cacheTableStruct : mapper.findTableStruct(database);
            this.primaryKey = new ArrayList<>();
            this.bigTextVar = new HashMap<>();
            this.bigTextSQL = new HashMap<>();
            this.bigTextField = new HashMap<>();
            this.fieldMap = ReflectUtil.getFieldMap(entity.getClass());
            this.declareSQL = new StringBuilder();
            this.assignmentSQL = new StringBuilder();
            this.fields = new ArrayList<>();
            this.values = new ArrayList<>();
            this.init();
        }

        private void init() throws Exception {
            cacheTableStruct = this.tableStruct;
            List<TableFieldStruct> fieldStruct = cacheTableStruct.getFieldStruct();
            for (TableFieldStruct struct : fieldStruct) {
                if(struct.primaryKey()) {
                    this.primaryKey.add(struct);
                }
                if(BIG_TEXT_TYPE.containsKey(struct.getFieldType())) {
                    Field field = fieldMap.get(CommonUtil.convert2Hump(struct.getFieldName()).replace(columnSuffix, ""));
                    if(field == null) {
                        throw new RuntimeException("Can't found field of column: " + struct.getFieldName() + " !");
                    }
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    if(value == null) {
                        continue;
                    }
                    if(BIG_TEXT_TYPE.get(struct.getFieldType())) {
                        value = bytesToHexString((byte[]) value);
                    }
                    bigTextVar.put(field.getName(), field.getName() + System.nanoTime());
                    bigTextSQL.put(field.getName(), convertSQL(value.toString()));
                    bigTextField.put(field.getName(), struct);
                }
            }
            for (String column : this.getBigTextVar().keySet()) {
                this.declareSQL.append(String.format("%s %s;\n", this.getBigTextVar().get(column), this.getBigTextField().get(column).getFieldType()));
            }
            for (String column : this.getBigTextVar().keySet()) {
                Boolean blob = BIG_TEXT_TYPE.get(this.bigTextField.get(column).getFieldType());
                this.assignmentSQL.append(this.getBigTextVar().get(column)).append(":=");
                if(blob) {
                    this.assignmentSQL.append("hextoraw(");
                }
                this.assignmentSQL.append("'").append(this.getBigTextSQL().get(column)).append("'");
                if(blob) {
                    this.assignmentSQL.append(")");
                }
                this.assignmentSQL.append("; \n");
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Map.Entry<String, Field> entry : this.fieldMap.entrySet()) {
                entry.getValue().setAccessible(true);
                Object o = entry.getValue().get(this.entity);
                fields.add(CommonUtil.convert2Underline(entry.getKey()) + columnSuffix);
                if(o == null) {
                    values.add(null);
                } else if(o instanceof Date) {
                    values.add(String.format("TO_DATE('%s', 'SYYYY-MM-DD HH24:MI:SS')", dateFormat.format((Date) o)));
                } else if(this.bigTextVar.containsKey(entry.getKey())) {
                    values.add(this.bigTextVar.get(entry.getKey()));
                } else {
                    values.add("'" + convertSQL(o.toString()) + "'");
                }
            }
        }
    }
}
