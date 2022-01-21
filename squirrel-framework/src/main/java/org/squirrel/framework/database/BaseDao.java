package org.squirrel.framework.database;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.squirrel.framework.database.bean.DataOperatorParam;
import org.squirrel.framework.response.Rp;
import org.squirrel.framework.response.RpEnum;

/**
 * @description 
 * @author weicong
 * @time   2021年7月24日 上午11:23:38
 * @version 1.0
 */
public interface BaseDao<T> extends MybatisBaseDao<T>, DataOperator<T> {

	@Override
	default Rp<T> add(T t) {
		DataOperatorParam dataOperatorParam = DataOperatorParamFactory.create(t);
		int insert = insert(dataOperatorParam);
		if (insert < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<T> add(List<T> list) {
		DataOperatorParam dataOperatorParam = DataOperatorParamFactory.create(list);
		int insert = insert(dataOperatorParam);
		if (insert < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<T> edit(String id, T t) {
		// TODO
		DataOperatorParam param = new DataOperatorParam();
		int update = update(param);
		if (update < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<T> remove(Set<String> ids){
		// TODO
		DataOperatorParam param = new DataOperatorParam();
		int delete = delete(param);
		if (delete < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<List<T>> list(Map<String, Object> query){
		// TODO
		DataOperatorParam param = new DataOperatorParam();
		return Rp.success(this.select(param));
	}
}
