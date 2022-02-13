package org.squirrel.framework.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;
import org.squirrel.framework.database.bean.BasePage;
import org.squirrel.framework.database.data.DataConstant;
import org.squirrel.framework.database.data.DataOperatorParam;
import org.squirrel.framework.database.data.DataOperatorParamFactory;
import org.squirrel.framework.response.Rp;
import org.squirrel.framework.response.RpEnum;
import org.squirrel.framework.util.ColUtil;

/**
 * @description 
 * @author weicong
 * @time   2021年7月24日 上午11:23:38
 * @version 1.0
 */
public interface BaseDao<T> extends MybatisBaseDao<T>, DataOperator<T> {
		
	Class<? super T> getBeanClass();
	
	@Transactional
	@Override
	default Rp<T> insert(T t) {
		DataOperatorParam dataOperatorParam = DataOperatorParamFactory.createInsert(t);
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
		Class<? super T> beanClass = getBeanClass();
		DataOperatorParam dataOperatorParam = DataOperatorParamFactory.createDelete(beanClass, ids);
		int delete = deleteByIds(dataOperatorParam);
		if (delete < 1){
			return Rp.failed(RpEnum.FAILED);
		}
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
	default Rp<T> detail(String id) {
		Class<? super T> beanClass = getBeanClass();
		Map<String, Object> query = new HashMap<>();
		query.put(DataConstant.DATA_ID, id);
		DataOperatorParam dataOperatorParam = DataOperatorParamFactory.createSelect(beanClass, query);
		List<T> select = this.select(dataOperatorParam);
		if (ColUtil.isNotEmpty(select)) {
			return Rp.success(select.get(0));
		}
		return Rp.success();
	}

	@Override
	default Rp<BasePage<T>> page(Map<String, Object> query, Integer current, Integer limit, String sort) {
		Class<? super T> beanClass = getBeanClass();
		DataOperatorParam dataOperatorParam = DataOperatorParamFactory.createSelect(beanClass, query);
//		int total = this.count(dataOperatorParam);
//		BasePage<T> basePage = new BasePage<>(current, limit);
//		List<T> page = this.page(dataOperatorParam, basePage);
//		basePage.setList(page);
		
		BasePage<T> basePage = new BasePage<>(current, limit);
		List<T> pageList = this.page(basePage, dataOperatorParam);
		basePage.setList(pageList);
		return Rp.success(basePage);
	}

}
