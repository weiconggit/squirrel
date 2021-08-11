package org.squirrel.framework.database;

import java.util.List;

import org.squirrel.framework.database.bean.DataHandleParam;
import org.squirrel.framework.response.Rp;
import org.squirrel.framework.response.RpEnum;

/**
 * @description 
 * @author weicong
 * @time   2021年7月24日 上午11:23:38
 * @version 1.0
 */
public interface BaseDao<T> extends MybatisBaseDao<T>, DataHandler<T> {

	@Override
	default Rp<T> add(DataHandleParam param) {
		int insert = insert(param);
		if (insert < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<T> edit(DataHandleParam param) {
		int update = update(param);
		if (update < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<T> remove(DataHandleParam param) {
		int delete = delete(param);
		if (delete < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	@Override
	default Rp<List<T>> list(DataHandleParam param) {
		return Rp.success(select(param));
	}

}
