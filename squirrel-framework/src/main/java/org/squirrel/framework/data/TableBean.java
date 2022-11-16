package org.squirrel.framework.data;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @description 表信息
 * @author weicong
 * @time   2022年1月23日
 * @version 1.0
 */
public class TableBean {

    private final String name; // sys_user
    private final Field[] fields; // 实体对象的字段信息
    private final List<String> fieldNames; // 顺序表字段：sys_id,sys_name,...
    private final Map<String, String> fieldTypeMap; // 字段类型，sysName -> java.lang.Integer
    private final Map<String, String> fieldNameMap; // sysName -> sys_name

    public TableBean(String name, Field[] fields, List<String> fieldNames, Map<String, String> fieldTypeMap, Map<String, String> fieldNameMap) {
        this.name = name;
        this.fields = fields;
        this.fieldNames = fieldNames;
        this.fieldTypeMap = fieldTypeMap;
        this.fieldNameMap = fieldNameMap;
    }

    public String getName() {
        return name;
    }
    public Field[] getFields() {
        return fields;
    }
    public List<String> getFieldNames() {
        return fieldNames;
    }

    public Map<String, String> getFieldTypeMap() {
        return fieldTypeMap;
    }

    public Map<String, String> getFieldNameMap() {
        return fieldNameMap;
    }
}
