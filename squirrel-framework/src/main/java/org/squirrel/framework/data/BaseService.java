package org.squirrel.framework.data;

import org.springframework.transaction.annotation.Transactional;
import org.squirrel.framework.response.BizException;
import org.squirrel.framework.response.Rp;
import org.squirrel.framework.response.RpEnum;

import javax.annotation.Nullable;
import java.util.*;

public interface BaseService<T> extends DataOperator<T> {

	BaseDao<T> getBaseDao();

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

	@Override
	default Rp<T> logicDeleteByIds(Set<String> ids) {
		List<T> list = beforeDelete(ids,null);
		beforeOperate(DELETE, null, list);
		Rp<T> remove = getBaseDao().logicDeleteByIds(ids);
		if (remove.isSuccess()) {
			afterOperate(DELETE, null, list);
		}
		return remove;
	}

	@Override
	default Rp<T> logicDelete(List<WhereBean> whereBeans) {
		List<T> list = beforeDelete(null,whereBeans);
		beforeOperate(DELETE, null, list);
		Rp<T> remove = getBaseDao().logicDelete(whereBeans);
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
	default Rp<T> delete(List<WhereBean> whereBeans){
		List<T> list = beforeDelete(null,whereBeans);
		beforeOperate(DELETE, null, list);
		Rp<T> remove = getBaseDao().delete(whereBeans);
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
	default Rp<List<T>> selectByIds(Set<String> ids, List<OrderBean> orderBeans){
		Rp<List<T>> rp = getBaseDao().selectByIds(ids, orderBeans);
		afterSelect(null, rp.getData());
		return rp;
	}

	@Transactional
	@Override
	default Rp<List<T>> select(List<WhereBean> whereBeans, List<OrderBean> orderBeans){
		whereBeans = beforeSelect(whereBeans);
		Rp<List<T>> select = getBaseDao().select(whereBeans, orderBeans);
		afterSelect(null, select.getData());
		return select;
	}

	@Transactional
	@Override
	default Rp<BasePage<T>> page(List<WhereBean> whereBeans, Integer current, Integer limit, List<OrderBean> orderBeans) {
		whereBeans = beforeSelect(whereBeans);
		Rp<BasePage<T>> page = getBaseDao().page(whereBeans, current, limit, orderBeans);
		afterSelect(null, page.getData().getList());
		return page;
	}

	default void beforeOperate(String funcType, @Nullable T t, @Nullable List<T> list){}
	default void afterOperate(String funcType, @Nullable T t, @Nullable List<T> list){}

	default List<T> beforeDelete(@Nullable Set<String> ids, @Nullable List<WhereBean> whereBeans){
		return Collections.emptyList();
	}

	default List<WhereBean> beforeSelect(List<WhereBean> whereBeans){
		if (whereBeans == null) {
			whereBeans = new ArrayList<>();
		}
		// 逻辑删除
		whereBeans.add(new WhereBean(BaseDao.DATA_IS_DEL, false, WhereBean.EQ));
		return whereBeans;
	}

	default void afterSelect(@Nullable T t, @Nullable List<T> list){}


}
