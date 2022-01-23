package org.squirrel.framework.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.squirrel.framework.database.bean.DataOperatorEnum;
import org.squirrel.framework.response.Rp;

public interface BaseService<T> extends DataOperator<T> {

	BaseDao<T> getDao();

	@Override
	default Rp<T> add(T t){
		List<T> list = new ArrayList<>();
		list.add(t);
		beforeAdd(list);
		Rp<T> add = getDao().add(t);
		if (add.isSuccess()){
			afterAdd(list);
			afterChange(DataOperatorEnum.INSERT, list);
		}
		return add;
	}

	@Override
	default Rp<T> add(List<T> list){
		beforeAdd(list);
		Rp<T> add = getDao().add(list);
		if (add.isSuccess()){
			afterAdd(list);
			afterChange(DataOperatorEnum.INSERT, list);
		}
		return add;
	}

	@Override
	default Rp<T> edit(String id, T t){
		List<T> singletonList = Collections.singletonList(t);
		beforeEdit(singletonList);
		Rp<T> edit = getDao().edit(id, t);
		if (edit.isSuccess()){
			afterEdit(singletonList);
			afterChange(DataOperatorEnum.UPDATE, singletonList);
		}
		return edit;
	}

	@Override
	default Rp<T> remove(Set<String> ids){
		List<T> list = beforeRemove(ids);
		Rp<T> remove = getDao().remove(ids);
		if (remove.isSuccess()){
			afterRemove(list);
			afterChange(DataOperatorEnum.DELETE, list);
		}
		return remove;
	}

	@Override
	default Rp<List<T>> list(Map<String, Object> query){
		beforeList(query);
		Rp<List<T>> list = getDao().list(query);
		if (list.isSuccess()){
			afterList(list.getData());
		}
		return list;
	}

	default void afterChange(DataOperatorEnum dataOpEnum, List<T> list){};
	default void beforeAdd(List<T> list){};
	default void afterAdd(List<T> list){};
	default void beforeEdit(List<T> list){};
	default void afterEdit(List<T> list){};
	default List<T> beforeRemove(Set<String> ids){ return Collections.emptyList(); };
	default void afterRemove(List<T> list){};
	default void beforeList(Map<String, Object> query){};
	default void afterList(List<T> list){};

}
