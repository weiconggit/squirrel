package org.squirrel.framework.database;

import java.util.List;

import org.squirrel.framework.database.bean.DataHandleParam;
import org.squirrel.framework.response.Rp;

public interface BaseService<T> {

	BaseDao<T> getDao();

	default Rp<T> add(List<T> list) {
		return null;
	}

	default Rp<T> edit(List<T> list) {
		return null;
	}

	default Rp<T> remove(DataHandleParam param) {
		return null;
	}

	default Rp<List<T>> list(DataHandleParam param) {
		BaseDao<T> dao = getDao();
		return dao.list(param);
	}
	
}
