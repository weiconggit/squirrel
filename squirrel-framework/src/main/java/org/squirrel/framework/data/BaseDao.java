package org.squirrel.framework.data;

import org.slf4j.Logger;
import org.squirrel.framework.SquirrelProperties;
import org.squirrel.framework.response.Rp;
import org.squirrel.framework.response.RpEnum;
import org.squirrel.framework.util.StrUtil;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description 
 * @author weicong
 * @time   2021年7月24日 上午11:23:38
 * @version 1.0
 */
public interface BaseDao<T> extends SquirrelMybatisDao<T>, DataOperator<T> {

	/// 实体字段信息缓存，省略了public static final
	Map<Class<?>, TableCache> tableCacheMap = new ConcurrentHashMap<>();
	String DATA_ID = SquirrelProperties.get("dataId");
	String DATA_IS_DEL = SquirrelProperties.get("dataIsDel");
	String DAO_SUFFIX = SquirrelProperties.get("daoSuffix");
	String SERVICE_SUFFIX = SquirrelProperties.get("serviceSuffix");
	String BEAN_SUFFIX = SquirrelProperties.get("beanSuffix");

	/**
	 * 当前与数据库表对应的实体类型，VO的父类，省略了public abstract
	 * @return 实体名称
	 */
	Class<? super T> getBeanClass();

	/**
	 * 获取实现类的日志对象引用
	 * @return
	 */
	Logger getLog();

	@Override
	default Rp<T> insert(T t) {
		Class<? super T> beanClass = getBeanClass();
		TableCache tableCache = getTableCache(beanClass);
		List<Object> beanValues = null;
		try {
			beanValues = getInsertValues(t, tableCache);
		} catch (IllegalAccessException e) {
			getLog().error("getInsertValues error: ", e);
		}
		if (beanClass != null) {
			int insert = insert(tableCache.getTableName(), tableCache.getKeys(), beanValues);
			if (insert > 0){
				return Rp.success();
			}
		}
		return Rp.failed(RpEnum.FAILED);
	}

	@Override
	default Rp<List<T>> insertBatch(List<T> list) {
		Class<? super T> beanClass = getBeanClass();
		TableCache tableCache = getTableCache(beanClass);
		List<List<Object>> beanValues = null;
		try {
			beanValues = getInsertValues(list, tableCache);
		} catch (IllegalAccessException e) {
			getLog().error("getInsertValues error: ", e);
		}
		int insert = insertBatch(tableCache.getTableName(), tableCache.getKeys(), beanValues);
		if (insert < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<T> update(T t) {
		Class<? super T> beanClass = getBeanClass();
		TableCache tableCache = getTableCache(beanClass);
		Map<String, Object> updateMap = null;
		try {
			updateMap = getUpdateMap(t, tableCache);
		} catch (IllegalAccessException e) {
			getLog().error("getUpdateMap error: ", e);
		}
		if (updateMap != null) {
			// 确保数据ID存在
			Object dataId = updateMap.remove(DATA_ID);
			if (dataId == null){
				return Rp.failed(RpEnum.ERROR_PARAMETER);
			}
			// 此处where条件仅有一个id
			Map<String, Object> whereMap = new Hashtable<>();
			whereMap.put(DATA_ID, dataId);
			int update = update(tableCache.getTableName(), updateMap, whereMap);
			if (update > 0){
				return Rp.success();
			}
		}
		return Rp.failed(RpEnum.FAILED);
	}

	@Override
	default Rp<List<T>> updateBatch(List<T> list) {
		// TODO
		return Rp.success();
	}

	@Override
	default Rp<T> delete(Map<String, Object> map){
		Class<? super T> beanClass = getBeanClass();
		TableCache tableCache = getTableCache(beanClass);
		int i = delete(tableCache.getTableName(), map);
		if (i < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<T> deleteByIds(Set<String> ids){
		Class<? super T> beanClass = getBeanClass();
		TableCache tableCache = getTableCache(beanClass);
		int i = deleteByIds(tableCache.getTableName(), ids);
		if (i < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<T> selectById(String id) {
		Class<? super T> beanClass = getBeanClass();
		TableCache tableCache = getTableCache(beanClass);
		List<T> ts = selectByIds(tableCache.getTableName(), tableCache.getKeys(), Collections.singletonList(id), null);
		if (ts != null && !ts.isEmpty()){
			return Rp.success(ts.get(0));
		}
		return Rp.success();
	}

	@Override
	default Rp<List<T>> selectByIds(Set<String> ids, LinkedHashMap<String, String> sortMap) {
		Class<? super T> beanClass = getBeanClass();
		TableCache tableCache = getTableCache(beanClass);
		List<T> ts = selectByIds(tableCache.getTableName(), tableCache.getKeys(), ids, sortMap);
		return Rp.success(ts);
	}

	@Override
	default Rp<List<T>> select(Map<String, Object> map, LinkedHashMap<String, String> sortMap){
		Class<? super T> beanClass = getBeanClass();
		TableCache tableCache = getTableCache(beanClass);
		List<T> select = select(tableCache.getTableName(), tableCache.getKeys(), map, sortMap);
		return Rp.success(select);
	}

	@Override
	default Rp<BasePage<T>> page(Map<String, Object> map, Integer current, Integer limit, LinkedHashMap<String, String> sortMap) {
		Class<? super T> beanClass = getBeanClass();
		TableCache paramCache = getTableCache(beanClass);
		BasePage<T> basePage = new BasePage<>(current, limit);
		List<T> pageList = this.page(basePage, paramCache.getTableName(), paramCache.getKeys(), map, sortMap);
		basePage.setList(pageList);
		return Rp.success(basePage);
	}

	/**
	 * 获取表信息缓存，省略了public前缀
	 * @param beanClass 实体类
	 * @return
	 */
	static TableCache getTableCache(Class<?> beanClass) {
		TableCache tableCache = tableCacheMap.get(beanClass);
		// 初始化实体字段sql信息
		if (tableCache == null) {
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
			String simpleName = beanClass.getSimpleName();
			String tableName = StrUtil.humpToUnderLine(simpleName);
			/// TODO 暂时，实体对象未于表对应
//			tableCache = new TableCache(tableName, declaredFields, keys, fieldKey);
			tableCache = new TableCache("sys_user", declaredFields, keys, fieldKey);
			tableCacheMap.put(beanClass, tableCache);
		}
		return tableCache;
	}

	/**
	 * 获取更新值对象
	 * @param t 更新对象
	 * @param <T>
	 * @return
	 */
	static <T> Map<String, Object> getUpdateMap(T t, TableCache tableCache) throws IllegalAccessException {
		Map<String, String> fieldKey = tableCache.getFieldKey();
		Field[] fields = tableCache.getFields();
		Map<String, Object> map = new HashMap<>();
		for (Field field : fields) {
			Object object = field.get(t);
			String name = field.getName();
			// isDel字段的特殊处理
			if (DATA_IS_DEL.equals(name)) {
				continue;
			}
			String key = fieldKey.get(name);
			map.put(key, object);
		}
		return map;
	}


	/**
	 * 获取多个新增值对象
	 * @param ts 入库对象列表
	 * @param <T>
	 * @return
	 */
	static <T> List<List<Object>> getInsertValues(List<T> ts, TableCache tableCache) throws IllegalAccessException {
		Field[] fields = tableCache.getFields();
		// 数据参数构造
		List<List<Object>> list = new ArrayList<>();
		for (T t : ts) {
			List<Object> insertValues = getInsertValues(t, tableCache);
			list.add(insertValues);
		}
		return list;
	}

	/**
	 * 获取新增值对象
	 * @param t 入库对象
	 * @param <T>
	 * @return
	 */
	static <T> List<Object> getInsertValues(T t, TableCache tableCache) throws IllegalAccessException {
		Field[] fields = tableCache.getFields();
		// 数据参数构造
		List<Object> list = new ArrayList<>();
		for (Field field : fields) {
			// isDel字段的特殊处理
			if (DATA_IS_DEL.equals(field.getName())) {
				list.add(true);
			} else {
				Object object = field.get(t);
				list.add(object);
			}
		}
		return list;
	}
}
