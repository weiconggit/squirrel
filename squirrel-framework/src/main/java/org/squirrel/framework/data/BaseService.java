package org.squirrel.framework.data;

import org.springframework.transaction.annotation.Transactional;
import org.squirrel.framework.response.Rp;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BaseService<T> extends DataOperator<T> {

	BaseDao<T> getBaseDao();

	@Transactional
	@Override
	default Rp<T> insert(T t){
		beforeOperate(DataOperatorEnum.INSERT, t, null);
		Rp<T> insert = getBaseDao().insert(t);
		if (insert.isSuccess()){
			afterOperate(DataOperatorEnum.INSERT, t, null);
		}
		return insert;
	}

	@Transactional
	@Override
	default Rp<List<T>> insertBatch(List<T> list){
		beforeOperate(DataOperatorEnum.INSERT_BATCH,null, list);
		Rp<List<T>> insert = getBaseDao().insertBatch(list);
		if (insert.isSuccess()){
			afterOperate(DataOperatorEnum.INSERT, null, list);
		}
		return insert;
	}

	@Transactional
	@Override
	default Rp<T> update(T t){
		beforeOperate(DataOperatorEnum.UPDATE, t, null);
		Rp<T> update = getBaseDao().update(t);
		if (update.isSuccess()) {
			afterOperate(DataOperatorEnum.UPDATE, t, null);
		}
		return update;
	}

	@Transactional
	@Override
	default Rp<List<T>> updateBatch(List<T> list){
		beforeOperate(DataOperatorEnum.UPDATE_BATCH, null, list);
		Rp<List<T>> update = getBaseDao().updateBatch(list);
		if (update.isSuccess()) {
			afterOperate(DataOperatorEnum.UPDATE, null, list);
		}
		return update;
	}

	@Transactional
	@Override
	default Rp<T> delete(Map<String, Object> map){
		List<T> list = beforeDelete(null,map);
		beforeOperate(DataOperatorEnum.DELETE, null, list);
		Rp<T> remove = getBaseDao().delete(map);
		if (remove.isSuccess()) {
			afterOperate(DataOperatorEnum.DELETE, null, list);
		}
		return remove;
	}

	@Transactional
	@Override
	default Rp<T> deleteByIds(Set<String> ids){
		List<T> list = beforeDelete(ids,null);
		beforeOperate(DataOperatorEnum.DELETE, null, list);
		Rp<T> remove = getBaseDao().deleteByIds(ids);
		if (remove.isSuccess()) {
			afterOperate(DataOperatorEnum.DELETE, null, list);
		}
		return remove;
	}

	@Transactional
	@Override
	default Rp<T> selectById(String id){
		Rp<T> rp = getBaseDao().selectById(id);
		afterSelect(DataOperatorEnum.SELECT, rp.getData(), null);
		return rp;
	}

	@Transactional
	@Override
	default Rp<List<T>> selectByIds(Set<String> ids){
		Rp<List<T>> rp = getBaseDao().selectByIds(ids);
		afterSelect(DataOperatorEnum.SELECT, null, rp.getData());
		return rp;
	}

	@Transactional
	@Override
	default Rp<List<T>> select(Map<String, Object> query, String sort){
		beforeSelect(query);
		Rp<List<T>> select = getBaseDao().select(query, sort);
		afterSelect(DataOperatorEnum.SELECT, null, select.getData());
		return select;
	}

	@Transactional
	@Override
	default Rp<BasePage<T>> page(Map<String, Object> query, String sort, Integer current, Integer limit) {
		beforeSelect(query);
		Rp<BasePage<T>> page = getBaseDao().page(query, sort, current, limit);
		afterSelect(DataOperatorEnum.SELECT, null, page.getData().getList());
		return page;
	}

	default void beforeOperate(DataOperatorEnum dataOpEnum, @Nullable T t, @Nullable List<T> list){};
	default void afterOperate(DataOperatorEnum dataOpEnum, @Nullable T t, @Nullable List<T> list){};

	default List<T> beforeDelete(@Nullable Set<String> ids, @Nullable Map<String, Object> map){return Collections.emptyList();};

	default void beforeSelect(Map<String, Object> query){};
	default void afterSelect(DataOperatorEnum dataOpEnum, @Nullable T t, @Nullable List<T> list){};


}
