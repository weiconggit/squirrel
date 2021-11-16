package org.squirrel.framework.database;

import java.util.Collection;
import java.util.List;

import org.squirrel.framework.database.bean.DataOpParam;
import org.squirrel.framework.response.Rp;
import org.squirrel.framework.response.RpEnum;

/**
 * @description 
 * @author weicong
 * @time   2021年7月24日 上午11:23:38
 * @version 1.0
 */
public interface BaseDao<T, I> extends MybatisBaseDao<T> {

	default Rp<T> add(DataOpParam param) {
		int insert = insert(param);
		if (insert < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	default Rp<T> edit(DataOpParam param) {
		int update = update(param);
		if (update < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	default Rp<T> remove(Collection<I> ids){
//		int delete = delete(ids);
//		if (delete < 1){
//			return Rp.failed(RpEnum.FAILED);
//		}
		return Rp.success();
	}

	default Rp<T> remove(DataOpParam param) {
		int delete = delete(param);
		if (delete < 1){
			return Rp.failed(RpEnum.FAILED);
		}
		return Rp.success();
	}

	default Rp<List<T>> list(DataOpParam param) {
		return Rp.success(select(param));
	}

}
