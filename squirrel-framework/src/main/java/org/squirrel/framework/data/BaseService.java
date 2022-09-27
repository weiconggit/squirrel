package org.squirrel.framework.data;

import org.springframework.transaction.annotation.Transactional;
import org.squirrel.framework.response.Rp;

import javax.annotation.Nullable;
import java.util.*;

public interface BaseService<T> extends DataOperator<T> {

	BaseDao<T> getBaseDao();

	// 标识操作方法类型
	String INSERT = "insert";
	String INSERT_BATCH = "insertBatch";
	String UPDATE = "update";
	String UPDATE_BATCH = "updateBatch";
	String DELETE = "delete";

	@Transactional
	@Override
	default Rp<T> insert(T t){
		beforeOperate(INSERT, t, null);
		Rp<T> insert = getBaseDao().insert(t);
		if (insert.isSuccess()){
			afterOperate(INSERT, t, null);
		}
		return insert;
	}

	@Transactional
	@Override
	default Rp<List<T>> insertBatch(List<T> list){
		beforeOperate(INSERT_BATCH,null, list);
		Rp<List<T>> insert = getBaseDao().insertBatch(list);
		if (insert.isSuccess()){
			afterOperate(INSERT, null, list);
		}
		return insert;
	}

	@Transactional
	@Override
	default Rp<T> update(T t){
		beforeOperate(UPDATE, t, null);
		Rp<T> update = getBaseDao().update(t);
		if (update.isSuccess()) {
			afterOperate(UPDATE, t, null);
		}
		return update;
	}

	@Transactional
	@Override
	default Rp<List<T>> updateBatch(List<T> list){
		beforeOperate(UPDATE_BATCH, null, list);
		Rp<List<T>> update = getBaseDao().updateBatch(list);
		if (update.isSuccess()) {
			afterOperate(UPDATE, null, list);
		}
		return update;
	}

	@Transactional
	@Override
	default Rp<T> delete(Map<String, Object> map){
		List<T> list = beforeDelete(null,map);
		beforeOperate(DELETE, null, list);
		Rp<T> remove = getBaseDao().delete(map);
		if (remove.isSuccess()) {
			afterOperate(DELETE, null, list);
		}
		return remove;
	}

	@Transactional
	@Override
	default Rp<T> deleteByIds(Set<String> ids){
		List<T> list = beforeDelete(ids,null);
		beforeOperate(DELETE, null, list);
		Rp<T> remove = getBaseDao().deleteByIds(ids);
		if (remove.isSuccess()) {
			afterOperate(DELETE, null, list);
		}
		return remove;
	}

	@Transactional
	@Override
	default Rp<T> selectById(String id){
		Rp<T> rp = getBaseDao().selectById(id);
		afterSelect(rp.getData(), null);
		return rp;
	}

	@Transactional
	@Override
	default Rp<List<T>> selectByIds(Set<String> ids, LinkedHashMap<String, String> sortMap){
		Rp<List<T>> rp = getBaseDao().selectByIds(ids, sortMap);
		afterSelect(null, rp.getData());
		return rp;
	}

	@Transactional
	@Override
	default Rp<List<T>> select(Map<String, Object> query, LinkedHashMap<String, String> sortMap){
		query = beforeSelect(query);
		Rp<List<T>> select = getBaseDao().select(query, sortMap);
		afterSelect(null, select.getData());
		return select;
	}

	@Transactional
	@Override
	default Rp<BasePage<T>> page(Map<String, Object> query, Integer current, Integer limit, LinkedHashMap<String, String> sortMap) {
		query = beforeSelect(query);
		Rp<BasePage<T>> page = getBaseDao().page(query, current, limit, sortMap);
		afterSelect(null, page.getData().getList());
		return page;
	}

	default void beforeOperate(String funcType, @Nullable T t, @Nullable List<T> list){}
	default void afterOperate(String funcType, @Nullable T t, @Nullable List<T> list){}

	default List<T> beforeDelete(@Nullable Set<String> ids, @Nullable Map<String, Object> map){
		return Collections.emptyList();
	}

	default Map<String, Object> beforeSelect(Map<String, Object> query){
		if (query == null) {
			query = new HashMap<>();
		}
		// 逻辑删除
		query.put(DataOperatorFactory.DATA_IS_DEL, false);
		return Collections.emptyMap();
	}

	default void afterSelect(@Nullable T t, @Nullable List<T> list){}


}
