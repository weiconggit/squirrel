package org.squirrel.framework.data;

import org.squirrel.framework.SquirrelProperties;
import org.squirrel.framework.response.BizException;
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
	Map<Class<?>, TableBean> tableBeanMap = new ConcurrentHashMap<>();

	/**
	 * 当前与数据库表对应的实体类型，VO的父类，省略了public abstract
	 * @return 实体名称
	 */
	Class<? super T> getEntityClass();

	@Override
	default Rp<T> insert(T t) {
		Class<? super T> entityClass = getEntityClass();
		if (entityClass == null) {
			throw new BizException("getBeanClass() must return a entity class");
		}
		TableBean tableBean = getTableBean(entityClass);
		List<Object> beanValues;
		try {
			beanValues = getInsertValues(t, tableBean);
		} catch (IllegalAccessException e) {
			throw new BizException(RpEnum.ERROR_SYSTEM, "getInsertValues error: " + e.getMessage(), e);
		}
		int insert = insert(tableBean.getName(), tableBean.getFieldNames(), beanValues);
		if (insert < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<List<T>> insertBatch(List<T> list) {
		Class<? super T> entityClass = getEntityClass();
		if (entityClass == null) {
			throw new BizException("getBeanClass() must return a entity class");
		}
		TableBean tableBean = getTableBean(entityClass);
		List<List<Object>> beanValues;
		try {
			beanValues = getInsertValues(list, tableBean);
		} catch (IllegalAccessException e) {
			throw new BizException(RpEnum.ERROR_SYSTEM, "getInsertValues error: " + e.getMessage(), e);
		}
		int insert = insertBatch(tableBean.getName(), tableBean.getFieldNames(), beanValues);
		if (insert < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<T> update(T t) {
		Class<? super T> entityClass = getEntityClass();
		if (entityClass == null) {
			throw new BizException("getBeanClass() must return a entity class");
		}
		TableBean tableBean = getTableBean(entityClass);
		Map<String, Object> updateMap;
		try {
			updateMap = getUpdateMap(t, tableBean);
		} catch (IllegalAccessException e) {
			throw new BizException(RpEnum.ERROR_SYSTEM, "getUpdateMap error: " + e.getMessage(), e);
		}
		// 确保数据ID存在
		Object dataId = updateMap.remove(DATA_ID);
		if (dataId == null){
			return Rp.failed(RpEnum.ERROR_PARAMETER);
		}
		// 此处where条件仅有一个id
		List<WhereBean> whereBeans = Collections.singletonList(new WhereBean(DATA_ID, dataId));
		int update = update(tableBean.getName(), updateMap, whereBeans);
		if (update < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<T> logicDeleteByIds(Set<String> ids) {
		Class<? super T> entityClass = getEntityClass();
		if (entityClass == null) {
			throw new BizException("getBeanClass() must return a entity class");
		}
		TableBean tableBean = getTableBean(entityClass);
		List<WhereBean> whereBeans = Collections.singletonList(new WhereBean(DATA_ID, ids, WhereBean.IN));
		int logicDelete = logicDelete(tableBean.getName(), DATA_IS_DEL, true, whereBeans);
		if (logicDelete < 1) {
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<T> logicDelete(List<WhereBean> whereBeans) {
		Class<? super T> entityClass = getEntityClass();
		if (entityClass == null) {
			throw new BizException("getBeanClass() must return a entity class");
		}
		TableBean tableBean = getTableBean(entityClass);
		int logicDelete = logicDelete(tableBean.getName(), DATA_IS_DEL, true, whereBeans);
		if (logicDelete < 1) {
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<T> deleteByIds(Set<String> ids){
		Class<? super T> entityClass = getEntityClass();
		if (entityClass == null) {
			throw new BizException("getBeanClass() must return a entity class");
		}
		TableBean tableCache = getTableBean(entityClass);
		List<WhereBean> whereBeans = Collections.singletonList(new WhereBean(DATA_ID, ids, WhereBean.IN));
		int delete = delete(tableCache.getName(), whereBeans);
		if (delete < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<T> delete(List<WhereBean> whereBeans){
		Class<? super T> entityClass = getEntityClass();
		if (entityClass == null) {
			throw new BizException("getBeanClass() must return a entity class");
		}
		TableBean tableBean = getTableBean(entityClass);
		int i = delete(tableBean.getName(), whereBeans);
		if (i < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<T> selectById(String id) {
		Class<? super T> entityClass = getEntityClass();
		if (entityClass == null) {
			throw new BizException("getBeanClass() must return a entity class");
		}
		TableBean tableCache = getTableBean(entityClass);
		List<WhereBean> whereBeans = Collections.singletonList(new WhereBean(DATA_ID, id));
		List<T> ts = select(tableCache.getName(), tableCache.getFieldNames(), whereBeans, null);
		if (ts == null || ts.isEmpty()){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success(ts.get(0));
	}

	@Override
	default Rp<List<T>> selectByIds(Set<String> ids, List<OrderBean> orderBeans) {
		Class<? super T> entityClass = getEntityClass();
		if (entityClass == null) {
			throw new BizException("getBeanClass() must return a entity class");
		}
		TableBean tableCache = getTableBean(entityClass);
		List<WhereBean> whereBeans = Collections.singletonList(new WhereBean(DATA_ID, ids, WhereBean.IN));
		List<T> ts = select(tableCache.getName(), tableCache.getFieldNames(), whereBeans, orderBeans);
		if (ts == null || ts.isEmpty()){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success(ts);
	}

	@Override
	default Rp<List<T>> select(List<WhereBean> whereBeans, List<OrderBean> orderBeans){
		Class<? super T> entityClass = getEntityClass();
		if (entityClass == null) {
			throw new BizException("getBeanClass() must return a entity class");
		}
		TableBean tableBean = getTableBean(entityClass);
		List<T> select = select(tableBean.getName(), tableBean.getFieldNames(), whereBeans, orderBeans);
		return Rp.success(select);
	}

	@Override
	default Rp<BasePage<T>> page(List<WhereBean> whereBeans, Integer current, Integer limit, List<OrderBean> orderBeans) {
		Class<? super T> entityClass = getEntityClass();
		if (entityClass == null) {
			throw new BizException("getBeanClass() must return a entity class");
		}
		TableBean tableBean = getTableBean(entityClass);
		BasePage<T> basePage = new BasePage<>(current, limit);
		List<T> pageList = this.page(basePage, tableBean.getName(), tableBean.getFieldNames(), whereBeans, orderBeans);
		basePage.setList(pageList);
		return Rp.success(basePage);
	}

	/**
	 * 获取或初始化表信息缓存，省略了public前缀
	 * @param beanClass 实体类
	 * @return
	 */
	static TableBean getTableBean(Class<?> beanClass) {
		TableBean tableBean = tableBeanMap.get(beanClass);
		// 初始化实体字段sql信息
		if (tableBean == null) {
			Field[] declaredFields = beanClass.getDeclaredFields();
			List<String> keys = new ArrayList<>();
			Map<String, String> fieldNameMap = new HashMap<>();
			Map<String, String> fieldTypeMap = new HashMap<>();
			for (int i = 0, size = declaredFields.length; i < size; i++) {
				Field declaredField = declaredFields[i];
				declaredField.setAccessible(true);
				String name = declaredField.getName();
				String fieldName = StrUtil.humpToUnderLine(name);
				keys.add(fieldName);
				fieldNameMap.put(name, fieldName);
				fieldTypeMap.put(name, declaredField.getType().getName());
			}
			String simpleName = beanClass.getSimpleName();
			String tableName = StrUtil.humpToUnderLine(simpleName);
			/// TODO 暂时，实体对象未于表对应
//			tableCache = new TableCache(tableName, declaredFields, keys, fieldNameMap);
			tableBean = new TableBean("sys_user", declaredFields, keys, fieldTypeMap, fieldNameMap);
			tableBeanMap.put(beanClass, tableBean);
		}
		return tableBean;
	}

	/**
	 * 获取更新值对象
	 * @param t 更新对象
	 * @param <T>
	 * @return
	 */
	static <T> Map<String, Object> getUpdateMap(T t, TableBean tableBean) throws IllegalAccessException {
		Map<String, String> fieldNameMap = tableBean.getFieldNameMap();
		Field[] fields = tableBean.getFields();
		Map<String, Object> map = new LinkedHashMap<>();
		for (Field field : fields) {
			Object object = field.get(t);
			String entityFieldName = field.getName();
			// isDel字段的特殊处理
			if (DATA_IS_DEL.equals(entityFieldName)) {
				continue;
			}
			String tableFieldName = fieldNameMap.get(entityFieldName);
			map.put(tableFieldName, object);
		}
		return map;
	}


	/**
	 * 获取多个新增值对象
	 * @param ts 入库对象列表
	 * @param <T>
	 * @return
	 */
	static <T> List<List<Object>> getInsertValues(List<T> ts, TableBean tableBean) throws IllegalAccessException {
		List<List<Object>> list = new ArrayList<>();
		for (T t : ts) {
			List<Object> insertValues = getInsertValues(t, tableBean);
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
	static <T> List<Object> getInsertValues(T t, TableBean tableBean) throws IllegalAccessException {
		Field[] fields = tableBean.getFields();
		// 数据参数构造
		List<Object> list = new ArrayList<>();
		for (Field field : fields) {
			// isDel字段的特殊处理
			if (DATA_IS_DEL.equals(field.getName())) {
				list.add(false);
			} else {
				Object object = field.get(t);
				list.add(object);
			}
		}
		return list;
	}

}
