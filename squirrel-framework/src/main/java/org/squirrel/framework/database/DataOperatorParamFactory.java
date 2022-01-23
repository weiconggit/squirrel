package org.squirrel.framework.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.squirrel.framework.database.bean.DataOperatorParam;
import org.squirrel.framework.util.StrUtil;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description 数据参数工厂
 * @author weicong
 * @time   2022年1月20日
 * @version 1.0
 */
public final class DataOperatorParamFactory {

    private static final Logger log = LoggerFactory.getLogger(DataOperatorParamFactory.class);

    /** 实体字段信息缓存 */
    private static final Map<String, BeanSqlCache> beanSqlCacheMap = new ConcurrentHashMap<>();

    private DataOperatorParamFactory(){}

    /**
     * 创建DataOperatorParam对象
     */
    public static @Nonnull <T> DataOperatorParam create(@Nonnull T t) {
        return create(Collections.singletonList(t));
    }

    /**
     * 创建DataOperatorParam对象
     */
    public static @Nonnull <T> DataOperatorParam create(@Nonnull List<T> list){
        Class<?> superclass = list.get(0).getClass().getSuperclass();
        String simpleName = superclass.getSimpleName();
        BeanSqlCache beanSqlCache = beanSqlCacheMap.get(simpleName);
        // 初始化实体字段sql信息
        if (beanSqlCache == null) {
            Field[] declaredFields = superclass.getDeclaredFields();
            StringBuilder insertKeySqlBuilder = new StringBuilder("(");
            for (int i = 0, size = declaredFields.length; i < size; i++) {
                Field declaredField = declaredFields[i];
                declaredField.setAccessible(true);
                String fieldName = StrUtil.humpToUnderLine(declaredField.getName());
                if (i == 0){
                    insertKeySqlBuilder.append(fieldName);
                } else {
                    insertKeySqlBuilder.append(",").append(fieldName);
                }
            }
            insertKeySqlBuilder.append(")");
            /// TODO 暂时，实体对象未于表对应
            beanSqlCache = new BeanSqlCache("sys_user", declaredFields, insertKeySqlBuilder.toString());
            beanSqlCacheMap.put(simpleName, beanSqlCache);
        }

        // 数据参数构造
        Field[] fields = beanSqlCache.getFields();
        DataOperatorParam param = new DataOperatorParam();
        param.setTableName(beanSqlCache.getTableName());
        param.setInsertKeysSql(beanSqlCache.getInsertKeysSql());
        try {
            // TODO isdel 字段的特殊处理
            for (T t : list) {
                List<Object> oneObjVals = new ArrayList<>();
                for (Field field : fields) {
                    Object object = field.get(t);
                    oneObjVals.add(object);
                }
                param.getValues().add(oneObjVals);
            }
        } catch (Exception e) {
            log.error("create error", e);
        }
        return  param;
    }


    /**
     * @description 实体信息及部分sql缓存
     * @author weicong
     * @time   2022年1月23日
     * @version 1.0
     */
    private static class BeanSqlCache {
    	
        private final String tableName; // sys_user
        private final Field[] fields; // 实体对象的字段信息

        private final String insertKeysSql; // 插入的部分sql (name1, name2, ...)

        public BeanSqlCache(String tableName, Field[] fields, String insertKeysSql) {
            this.tableName = tableName;
            this.fields = fields;
            this.insertKeysSql = insertKeysSql;
        }

        public String getTableName() {
            return tableName;
        }
        public Field[] getFields() {
            return fields;
        }
        public String getInsertKeysSql() {
            return insertKeysSql;
        }
    }

    /**
     * @description 参数构建
     * @author weicong
     * @time   2022年1月23日 下午5:11:13
     * @version 1.0
     */
    private static class ParamBuilder {

        private String key;
        private String value;

        /**
         * @param key
         * @param value
         */
        public ParamBuilder(String key, String value) {
            super();
            this.key = key;
            this.value = value;
        }

        public final String getKey() {
            return key;
        }

        public final void setKey(String key) {
            this.key = key;
        }

        public final String getValue() {
            return value;
        }

        public final void setValue(String value) {
            this.value = value;
        }
    }
}
