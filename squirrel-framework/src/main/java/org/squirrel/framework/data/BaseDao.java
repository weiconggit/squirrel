package org.squirrel.framework.data;

import org.squirrel.framework.response.Rp;
import org.squirrel.framework.response.RpEnum;

import javax.xml.crypto.Data;
import java.util.*;

/**
 * @description 
 * @author weicong
 * @time   2021年7月24日 上午11:23:38
 * @version 1.0
 */
public interface BaseDao<T> extends SquirrelMybatisDao<T>, DataOperator<T> {

	/**
	 * 当前与数据库表对应的实体类型，VO的父类
	 * @return
	 */
	Class<? super T> getBeanClass();
	
	@Override
	default Rp<T> insert(T t) {
		Class<? super T> beanClass = getBeanClass();
		DataOperatorFactory.TableCache tableCache = DataOperatorFactory.getTableCache(beanClass);
		List<Object> beanValues = DataOperatorFactory.getInsertValues(t, tableCache);
		int insert = insert(tableCache.getTableName(), tableCache.getKeys(), beanValues);
		if (insert < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<List<T>> insertBatch(List<T> list) {
		Class<? super T> beanClass = getBeanClass();
		DataOperatorFactory.TableCache tableCache = DataOperatorFactory.getTableCache(beanClass);
		List<List<Object>> beanValues = DataOperatorFactory.getInsertValues(list, tableCache);
		int insert = insertBatch(tableCache.getTableName(), tableCache.getKeys(), beanValues);
		if (insert < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<T> update(T t) {
		Class<? super T> beanClass = getBeanClass();
		DataOperatorFactory.TableCache tableCache = DataOperatorFactory.getTableCache(beanClass);
		Map<String, Object> updateValues = DataOperatorFactory.getUpdateValues(t, tableCache);
		// 确保数据ID存在
		Object dataId = updateValues.remove(DataConstant.DATA_ID);
		if (dataId == null){
			return Rp.failed(RpEnum.ERROR_PARAMETER);
		}
		Map<String, Object> whereKeyValues = new Hashtable<>();
		whereKeyValues.put(DataConstant.DATA_ID, dataId);
		int update = update(tableCache.getTableName(), updateValues, whereKeyValues);
		if (update < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<List<T>> updateBatch(List<T> list) {
		// TODO
		return Rp.success();
	}

	@Override
	default Rp<T> delete(Map<String, Object> map){
		Class<? super T> beanClass = getBeanClass();
		DataOperatorFactory.TableCache tableCache = DataOperatorFactory.getTableCache(beanClass);
		int i = delete(tableCache.getTableName(), map);
		if (i < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<T> deleteByIds(Set<String> ids){
		Class<? super T> beanClass = getBeanClass();
		DataOperatorFactory.TableCache tableCache = DataOperatorFactory.getTableCache(beanClass);
		int i = deleteByIds(tableCache.getTableName(), ids);
		if (i < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<T> selectById(String id) {
		Class<? super T> beanClass = getBeanClass();
		DataOperatorFactory.TableCache paramCache = DataOperatorFactory.getTableCache(beanClass);
		List<T> ts = selectByIds(paramCache.getTableName(), paramCache.getKeys(), Collections.singletonList(id));
		if (ts != null && !ts.isEmpty()){
			return Rp.success(ts.get(0));
		}
		return Rp.success();
	}

	@Override
	default Rp<List<T>> selectByIds(Set<String> ids) {
		Class<? super T> beanClass = getBeanClass();
		DataOperatorFactory.TableCache paramCache = DataOperatorFactory.getTableCache(beanClass);
		List<T> ts = selectByIds(paramCache.getTableName(), paramCache.getKeys(), ids);
		return Rp.success(ts);
	}

	@Override
	default Rp<List<T>> select(Map<String, Object> map, String sort){
		// TODO sort
		Class<? super T> beanClass = getBeanClass();
		DataOperatorFactory.TableCache paramCache = DataOperatorFactory.getTableCache(beanClass);
		List<T> select = select(paramCache.getTableName(), paramCache.getKeys(), map);
		return Rp.success(select);
	}

	@Override
	default Rp<BasePage<T>> page(Map<String, Object> map, String sort, Integer current, Integer limit) {
		Class<? super T> beanClass = getBeanClass();
		DataOperatorFactory.TableCache paramCache = DataOperatorFactory.getTableCache(beanClass);
		BasePage<T> basePage = new BasePage<>(current, limit);
		List<T> pageList = this.page(basePage, paramCache.getTableName(), paramCache.getKeys(), map);
		basePage.setList(pageList);
		return Rp.success(basePage);
	}

}
