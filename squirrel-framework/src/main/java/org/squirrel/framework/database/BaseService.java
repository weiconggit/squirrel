package org.squirrel.framework.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.squirrel.framework.database.bean.BasePage;
import org.squirrel.framework.database.data.DataOperatorEnum;
import org.squirrel.framework.response.Rp;

public interface BaseService<T> extends DataOperator<T> {

	BaseDao<T> getBaseDao();

	@Override
	default Rp<T> insert(T t){
		List<T> list = new ArrayList<>();
		list.add(t);
		beforeInsert(list);
		Rp<T> insert = getBaseDao().insert(t);
		if (insert.isSuccess()){
			afterInsert(list);
			afterChange(DataOperatorEnum.INSERT, list);
		}
		return insert;
	}

	@Override
	default Rp<T> insert(List<T> list){
		beforeInsert(list);
		Rp<T> insert = getBaseDao().insert(list);
		if (insert.isSuccess()){
			afterInsert(list);
			afterChange(DataOperatorEnum.INSERT, list);
		}
		return insert;
	}

	@Override
	default Rp<T> update(T t){
		List<T> singletonList = Collections.singletonList(t);
		beforeUpdate(singletonList);
		Rp<T> update = getBaseDao().update(t);
		if (update.isSuccess()){
			afterUpdate(singletonList);
			afterChange(DataOperatorEnum.UPDATE, singletonList);
		}
		return update;
	}

	@Override
	default Rp<T> delete(Set<String> ids){
		List<T> list = beforeDelete(ids);
		Rp<T> remove = getBaseDao().delete(ids);
		if (remove.isSuccess()){
			afterDelete(list);
			afterChange(DataOperatorEnum.DELETE, list);
		}
		return remove;
	}

	@Override
	default Rp<List<T>> select(Map<String, Object> query, String sort){
		beforeSelect(query);
		Rp<List<T>> list = getBaseDao().select(query, sort);
		if (list.isSuccess()){
			afterSelect(list.getData());
		}
		return list;
	}
	
	@Override
	default Rp<BasePage<T>> page(Map<String, Object> query, Integer current, Integer limit, String sort) {
		return null;
	}

	@Override
	default Rp<T> detail(String id) {
		return null;
	}

	default void afterChange(DataOperatorEnum dataOpEnum, List<T> list){};
	default void beforeInsert(List<T> list){};
	default void afterInsert(List<T> list){};
	default void beforeUpdate(List<T> list){};
	default void afterUpdate(List<T> list){};
	default List<T> beforeDelete(Set<String> ids){ return Collections.emptyList(); };
	default void afterDelete(List<T> list){};
	default void beforeSelect(Map<String, Object> query){};
	default void afterSelect(List<T> list){};

}
