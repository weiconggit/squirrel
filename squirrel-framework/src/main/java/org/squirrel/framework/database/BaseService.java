package org.squirrel.framework.database;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.squirrel.framework.database.bean.DataOpEnum;
import org.squirrel.framework.database.bean.DataOpParam;
import org.squirrel.framework.response.Rp;

public interface BaseService<T, I> {

	BaseDao<T, I> getDao();

	default Rp<T> add(List<T> list) {
		beforeAdd(list);
		DataOpParam param = new DataOpParam();
		Rp<T> add = getDao().add(param);
		if (add.isSuccess()){
			afterAdd(list);
			afterChange(DataOpEnum.ADD, list);
		}
		return add;
	}

	default Rp<T> edit(List<T> list) {
		beforeEdit(list);
		DataOpParam param = new DataOpParam();
		Rp<T> edit = getDao().edit(param);
		if (edit.isSuccess()){
			afterEdit(list);
			afterChange(DataOpEnum.EDIT, list);
		}
		return edit;
	}

	default Rp<T> remove(Set<String> ids) {
		DataOpParam param = new DataOpParam();
		Rp<List<T>> rp = beforeRemove(param);
		Rp<T> remove = getDao().remove(param);
		if (remove.isSuccess() && rp.isSuccess()){
			afterRemove(rp.getData());
			afterChange(DataOpEnum.REMOVE, rp.getData());
		}
		return remove;
	}

	default Rp<T> remove(Map<String, String> query) {
		DataOpParam param = new DataOpParam();
		Rp<List<T>> rp = beforeRemove(param);
		Rp<T> remove = getDao().remove(param);
		if (remove.isSuccess() && rp.isSuccess()){
			List<T> list = rp.getData();
			afterRemove(list);
			afterChange(DataOpEnum.REMOVE, list);
		}
		return remove;
	}

	default Rp<List<T>> list(Map<String, String> query) {
		DataOpParam param = new DataOpParam();
		beforeList(param);
		Rp<List<T>> list = getDao().list(param);
		if (list.isSuccess()){
			afterList(list.getData());
			afterChange(DataOpEnum.LIST, list.getData());
		}
		return list;
	}

	default void afterChange(DataOpEnum dataOpEnum, List<T> list){};
	default void beforeAdd(List<T> list){};
	default void afterAdd(List<T> list){};
	default void beforeEdit(List<T> list){};
	default void afterEdit(List<T> list){};
	default Rp<List<T>> beforeRemove(DataOpParam param){ return null; };
	default void afterRemove(List<T> list){};
	default void beforeList(DataOpParam param){};
	default void afterList(List<T> list){};

}
