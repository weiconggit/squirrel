package org.squirrel.framework.database;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;
import org.squirrel.framework.database.bean.BasePage;
import org.squirrel.framework.database.data.DataOperatorParam;
import org.squirrel.framework.database.data.DataOperatorParamFactory;
import org.squirrel.framework.response.Rp;
import org.squirrel.framework.response.RpEnum;

/**
 * @description 
 * @author weicong
 * @time   2021年7月24日 上午11:23:38
 * @version 1.0
 */
public interface BaseDao<T> extends MybatisBaseDao<T>, DataOperator<T> {
		
	Class<T> getBeanClass();
	
	@Transactional
	@Override
	default Rp<T> insert(T t) {
		DataOperatorParam dataOperatorParam = DataOperatorParamFactory.createInsert( t);
		int insert = insert(dataOperatorParam);
		if (insert < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Transactional
	@Override
	default Rp<T> insert(List<T> list) {
		DataOperatorParam dataOperatorParam = DataOperatorParamFactory.createInsert(list);
		int insert = insert(dataOperatorParam);
		if (insert < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Transactional
	@Override
	default Rp<T> update(T t) {
		DataOperatorParam dataOperatorParam = DataOperatorParamFactory.createUpdate(t);
		int update = update(dataOperatorParam);
		if (update < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Transactional
	@Override
	default Rp<T> delete(Set<String> ids){
		Class<T> beanClass = getBeanClass();
		DataOperatorParam dataOperatorParam = DataOperatorParamFactory.createDelete(beanClass, ids);
		int delete = deleteByIds(dataOperatorParam);
		if (delete < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<List<T>> select(Map<String, Object> query, String sort){
		Class<T> beanClass = getBeanClass();
		DataOperatorParam dataOperatorParam = DataOperatorParamFactory.createSelect(beanClass, query);
		return Rp.success(this.select(dataOperatorParam));
	}

	@Override
	default Rp<BasePage<T>> page(Map<String, Object> query, Integer current, Integer limit, String sort) {
		Class<T> beanClass = getBeanClass();
		DataOperatorParam dataOperatorParam = DataOperatorParamFactory.createSelect(beanClass, query);
		int total = this.count(dataOperatorParam);
		BasePage<T> basePage = new BasePage<>(current, limit, total);
		List<T> page = this.page(dataOperatorParam, basePage);
		basePage.setList(page);
		return Rp.success(basePage);
	}

	@Override
	default Rp<T> detail(String id) {
		// 
//		this.detail(id);
		return null;
	}
	
}
