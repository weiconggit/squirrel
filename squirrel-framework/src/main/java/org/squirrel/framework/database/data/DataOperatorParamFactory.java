package org.squirrel.framework.database.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.squirrel.framework.util.StrUtil;

/**
 * @description 数据参数工厂
 * @author weicong
 * @time   2022年1月20日
 * @version 1.0
 */
public final class DataOperatorParamFactory {

    private static final Logger log = LoggerFactory.getLogger(DataOperatorParamFactory.class);
    /// 实体字段信息缓存
    private static final Map<Class<?>, ParamCache> paramCacheMap = new ConcurrentHashMap<>();

    private DataOperatorParamFactory(){}

    /**
     * 并列查询
     * @param beanClass
     * @param ids
     * @return
     */
    public static @Nonnull DataOperatorParam createSelect(@Nonnull Class<?> beanClass, @Nullable Map<String, Object> query) {
    	ParamCache paramCache = getParamCache(beanClass);
        DataOperatorParam param = new DataOperatorParam();
        param.setTableName(paramCache.getTableName());
        param.setSelectKeys(paramCache.getKeys());
        if (query != null) {
			Iterator<Entry<String, Object>> iterator = query.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> next = iterator.next();
				param.addWhereKeyValues(next.getKey(), next.getValue());
			}
		}
        return param;
    }
    
    /**
     * 删除
     * @param beanClass
     * @param ids
     * @return
     */
    public static @Nonnull DataOperatorParam createDelete(@Nonnull Class<?> beanClass, @Nonnull Set<String> ids) {
    	ParamCache paramCache = getParamCache(beanClass);
    	DataOperatorParam param = new DataOperatorParam();
    	param.setTableName(paramCache.getTableName());
    	for (String id : ids) {
    		param.addWhereKeyValues(DataConstant.DATA_ID, id);
    	}
    	return param;
    }
    
    /**
     * 更新
     * @param t
     * @return
     */
    public static @Nonnull <T> DataOperatorParam createUpdate(@Nonnull T t) {
    	Class<?> superclass = t.getClass().getSuperclass();
    	ParamCache paramCache = getParamCache(superclass);
    	Map<String, String> fieldKey = paramCache.getFieldKey();
    	Field[] fields = paramCache.getFields();
    	
        DataOperatorParam param = new DataOperatorParam();
        try {
            for (Field field : fields) {
            	Object object = field.get(t);
                String name = field.getName();
                if (DataConstant.DATA_IS_DEL.equals(name)) {
					continue;
				}
                // XXX id字段的特殊处理
                if (DataConstant.DATA_ID.equals(name)) {
                	String key = fieldKey.get(name);
                	param.addWhereKeyValues(key, object);
				} else {
					String key = fieldKey.get(name);
					param.addSetKeyValue(key, object);
				}
            }
        } catch (Exception e) {
            log.error("create update data operate error", e);
        }
        return param;
    }

    /**
     * 新增
     * @param t
     * @return
     */
    public static @Nonnull <T> DataOperatorParam createInsert(@Nonnull T t) {
        return createInsert(Collections.singletonList(t));
    }

    public static @Nonnull <T> DataOperatorParam createInsert(@Nonnull List<T> list){
        Class<?> superclass = list.get(0).getClass().getSuperclass();
        ParamCache parmCache = getParamCache(superclass);
        Field[] fields = parmCache.getFields();
        // 数据参数构造
        DataOperatorParam param = new DataOperatorParam();
        param.setTableName(parmCache.getTableName());
        param.setInsertKeys(parmCache.getKeys());
        param.setValues(new ArrayList<>());
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
                param.getValues().add(oneObjVals);
            }
        } catch (Exception e) {
            log.error("create insert error", e);
        }
        return  param;
    }

	/**
	  *  获取缓存
	 * @param beanClass 实体，如：SysUser.class
	 * @return
	 */
	private static ParamCache getParamCache(Class<?> beanClass) {
		ParamCache paramCache = paramCacheMap.get(beanClass);
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
            paramCache = new ParamCache("sys_user", declaredFields, keys, fieldKey);
            paramCacheMap.put(beanClass, paramCache);
        }
		return paramCache;
	}


    /**
     * @description 参数字段信息缓存
     * @author weicong
     * @time   2022年1月23日
     * @version 1.0
     */
    private static class ParamCache {
    	
        private final String tableName; // sys_user
        private final Field[] fields; // 实体对象的字段信息
        private final List<String> keys; // 顺序表字段：sys_id,sys_name,...
        private final Map<String, String> fieldKey; // sysName -> sys_name

        public ParamCache(String tableName, Field[] fields, List<String> keys, Map<String, String> fieldKey) {
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
