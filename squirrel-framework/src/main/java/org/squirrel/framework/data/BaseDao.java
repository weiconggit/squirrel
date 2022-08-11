package org.squirrel.framework.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;
import org.squirrel.framework.response.Rp;
import org.squirrel.framework.response.RpEnum;
import org.squirrel.framework.util.ColUtil;

/**
 * @description 
 * @author weicong
 * @time   2021年7月24日 上午11:23:38
 * @version 1.0
 */
public interface BaseDao<T> extends SquirrelMybatisDao<T>, DataOperator<T> {
		
	Class<? super T> getBeanClass();
	
	@Override
	default Rp<T> insert(T t) {
		DataOperatorParam dataOperatorParam = DataOperatorParamFactory.createInsert(t);
		int insert = insert(dataOperatorParam);
		if (insert < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<List<T>> insertBatch(List<T> list) {
		DataOperatorParam dataOperatorParam = DataOperatorParamFactory.createInsert(list);
		int insert = insert(dataOperatorParam);
		if (insert < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<T> update(T t) {
		DataOperatorParam dataOperatorParam = DataOperatorParamFactory.createUpdate(t);
		int update = update(dataOperatorParam);
		if (update < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<List<T>> updateBatch(List<T> list) {
		// TODO
//		DataOperatorParam dataOperatorParam = DataOperatorParamFactory.createUpdate(t);
//		int update = update(dataOperatorParam);
//		if (update < 1){
//			return Rp.failed(RpEnum.FAILED);
//		}
		return Rp.success();
	}

	@Override
	default Rp<T> deleteByIds(Set<String> ids){
		Class<? super T> beanClass = getBeanClass();
		DataOperatorParam dataOperatorParam = DataOperatorParamFactory.createDelete(beanClass, ids);
		int delete = deleteByIds(dataOperatorParam);
		if (delete < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}
	
	@Override
	default Rp<T> selectById(String id) {
		Class<? super T> beanClass = getBeanClass();
		// TODO
		return Rp.success();
	}

	@Override
	default Rp<List<T>> selectByIds(Set<String> ids) {
		Class<? super T> beanClass = getBeanClass();
		// TODO
		return Rp.success();
	}

	@Override
	default Rp<List<T>> select(Map<String, Object> query, String sort){
		Class<? super T> beanClass = getBeanClass();
		DataOperatorParam dataOperatorParam = DataOperatorParamFactory.createSelect(beanClass, query);
		List<T> select = this.select(dataOperatorParam);
		return Rp.success(select);
	}

	@Override
	default Rp<BasePage<T>> page(Map<String, Object> query, String sort, Integer current, Integer limit) {
		Class<? super T> beanClass = getBeanClass();
		DataOperatorParam dataOperatorParam = DataOperatorParamFactory.createSelect(beanClass, query);
		BasePage<T> basePage = new BasePage<>(current, limit);
		List<T> pageList = this.page(basePage, dataOperatorParam);
		basePage.setList(pageList);
		return Rp.success(basePage);
	}

}
