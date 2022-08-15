package org.squirrel.framework.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.squirrel.framework.util.StrUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description 数据转换
 * @author weicong
 * @time   2022年1月20日
 * @version 1.0
 */
public final class DataOperatorFactory {

    private static final Logger log = LoggerFactory.getLogger(DataOperatorFactory.class);
    /// 实体字段信息缓存
    private static final Map<Class<?>, TableCache> paramCacheMap = new ConcurrentHashMap<>();

    private DataOperatorFactory(){}

    /**
     * 获取更新值对象
     * @param t
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> getUpdateValues(T t, TableCache tableCache){
        Map<String, String> fieldKey = tableCache.getFieldKey();
        Field[] fields = tableCache.getFields();
        Map<String, Object> map = new HashMap<>();
        try {
            for (Field field : fields) {
                Object object = field.get(t);
                String name = field.getName();
                if (DataConstant.DATA_IS_DEL.equals(name)) {
                    continue;
                }
                String key = fieldKey.get(name);
                map.put(key, object);
            }
        } catch (Exception e) {
            log.error("getUpdateValues error", e);
        }
        return map;
    }

    /**
     * 获取新增值对象
     * @param t
     * @param <T>
     * @return
     */
    public static <T> List<Object> getInsertValues(T t, TableCache tableCache){
        Field[] fields = tableCache.getFields();
        // 数据参数构造
        List<Object> list = new ArrayList<>();
        try {
            for (Field field : fields) {
                // XXX isDel字段的特殊处理
                if (DataConstant.DATA_IS_DEL.equals(field.getName())) {
                    list.add(true);
                } else {
                    Object object = field.get(t);
                    list.add(object);
                }
            }
        } catch (Exception e) {
            log.error("getBeanVaules error", e);
        }
        return list;
    }

    /**
     * 获取多个新增值对象
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<List<Object>> getInsertValues(List<T> list, TableCache tableCache){
        Field[] fields = tableCache.getFields();
        // 数据参数构造
        List<List<Object>> lists = new ArrayList<>();
        try {
            for (T t : list) {
                List<Object> oneObjVals = new ArrayList<>();
                for (Field field : fields) {
                    // XXX isDel字段的特殊处理
                    if (DataConstant.DATA_IS_DEL.equals(field.getName())) {
                        oneObjVals.add(true);
                    } else {
                        Object object = field.get(t);
                        oneObjVals.add(object);
                    }
                }
                lists.add(oneObjVals);
            }
        } catch (Exception e) {
            log.error("getBeanVaules error", e);
        }
        return lists;
    }

	/**
	  *  获取缓存
	 * @param beanClass 实体，如：SysUser.class
	 * @return
	 */
	public static TableCache getTableCache(Class<?> beanClass) {
		TableCache paramCache = paramCacheMap.get(beanClass);
        // 初始化实体字段sql信息
        if (paramCache == null) {
            Field[] declaredFields = beanClass.getDeclaredFields();
            List<String> keys = new ArrayList<>();
            Map<String, String> fieldKey = new HashMap<>();
            for (int i = 0, size = declaredFields.length; i < size; i++) {
                Field declaredField = declaredFields[i];
                declaredField.setAccessible(true);
                String name = declaredField.getName();
				String fieldName = StrUtil.humpToUnderLine(name);
				keys.add(fieldName);
                fieldKey.put(name, fieldName);
            }
            /// TODO 暂时，实体对象未于表对应
            String simpleName = beanClass.getSimpleName();
            String tableName = StrUtil.humpToUnderLine(simpleName);
            paramCache = new TableCache("sys_user", declaredFields, keys, fieldKey);
            paramCacheMap.put(beanClass, paramCache);
        }
		return paramCache;
	}


    /**
     * @description 表字段信息缓存
     * @author weicong
     * @time   2022年1月23日
     * @version 1.0
     */
    static class TableCache {
    	
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

}
