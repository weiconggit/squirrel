package org.squirrel.framework.database;

import java.util.*;

import org.squirrel.framework.database.bean.DataOpEnum;
import org.squirrel.framework.database.bean.DataOpParam;
import org.squirrel.framework.response.Rp;

public interface BaseService<T, I> {

	BaseDao<T, I> getDao();

	default Rp<T> insert(T t){
		return this.insert(Arrays.asList(t));
	}

	default Rp<T> insert(List<T> list) {
		beforeAdd(list);
		DataOpParam param = new DataOpParam();
		Rp<T> add = getDao().add(param);
		if (add.isSuccess()){
			afterAdd(list);
			afterChange(DataOpEnum.INSERT, list);
		}
		return add;
	}

	default Rp<T> update(T t){
		return this.update(Arrays.asList(t));
	}

	default Rp<T> update(List<T> list) {
		beforeEdit(list);
		DataOpParam param = new DataOpParam();
		Rp<T> edit = getDao().edit(param);
		if (edit.isSuccess()){
			afterEdit(list);
			afterChange(DataOpEnum.UPDATE, list);
		}
		return edit;
	}

	default Rp<T> delete(Set<String> ids) {
		DataOpParam param = new DataOpParam();
		List<T> list = beforeRemove(param);
		Rp<T> remove = getDao().remove(param);
		if (remove.isSuccess()){
			afterRemove(list);
			afterChange(DataOpEnum.DELETE, list);
		}
		return remove;
	}

	default Rp<T> delete(Map<String, String> query) {
		DataOpParam param = new DataOpParam();
		List<T> list = beforeRemove(param);
		Rp<T> remove = getDao().remove(param);
		if (remove.isSuccess()){
			afterRemove(list);
			afterChange(DataOpEnum.DELETE, list);
		}
		return remove;
	}

	default Rp<List<T>> list(Map<String, String> query) {
		DataOpParam param = new DataOpParam();
		beforeList(param);
		Rp<List<T>> list = getDao().list(param);
		if (list.isSuccess()){
			afterList(list.getData());
		}
		return list;
	}

	default void afterChange(DataOpEnum dataOpEnum, List<T> list){};
	default void beforeAdd(List<T> list){};
	default void afterAdd(List<T> list){};
	default void beforeEdit(List<T> list){};
	default void afterEdit(List<T> list){};
	default List<T> beforeRemove(DataOpParam param){ return Collections.emptyList(); };
	default void afterRemove(List<T> list){};
	default void beforeList(DataOpParam param){};
	default void afterList(List<T> list){};

}
