package org.squirrel.framework.database;

import java.util.List;

import org.squirrel.framework.database.bean.DataHandleParam;
import org.squirrel.framework.response.Rp;

/**
 * @description 
 * @author weicong
 * @time   2021年7月24日 上午11:23:38
 * @version 1.0
 */
public interface BaseDao<T> extends MybatisBaseDao<T> {

	default Rp<T> add(DataHandleParam param) {
		return null;
	}

	default Rp<T> edit(DataHandleParam param) {
		return null;
	}

	default Rp<T> remove(DataHandleParam param) {
		return null;
	}

	default Rp<List<T>> list(DataHandleParam param) {
		return Rp.success(select(param));
	}

}
