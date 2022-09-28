package org.squirrel.framework.data;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @description 表字段信息缓存
 * @author weicong
 * @time   2022年1月23日
 * @version 1.0
 */
public class TableCache {

    private final String tableName; // sys_user
    private final Field[] fields; // 实体对象的字段信息
    private final List<String> keys; // 顺序表字段：sys_id,sys_name,...
    private final Map<String, String> fieldKey; // sysName -> sys_name

    public TableCache(String tableName, Field[] fields, List<String> keys, Map<String, String> fieldKey) {
        this.tableName = tableName;
        this.fields = fields;
        this.keys = keys;
        this.fieldKey = fieldKey;
    }

    public String getTableName() {
        return tableName;
    }
    public Field[] getFields() {
        return fields;
    }
    public Map<String, String> getFieldKey() {
        return fieldKey;
    }
    public List<String> getKeys() {
        return keys;
    }
}
