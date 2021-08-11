package org.squirrel.framework.database;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.squirrel.framework.database.bean.DataHandleParam;
import org.squirrel.framework.response.Rp;

public interface BaseService<T> extends DataHandler<T> {

	BaseDao<T> getDao();

	@Override
	default Rp<T> add(List<T> list) {
		// TODO
		BaseDao<T> dao = getDao();
		DataHandleParam param = new DataHandleParam();
		return dao.add(param);
	}

	@Override
	default Rp<T> edit(List<T> list) {
		// TODO
		BaseDao<T> dao = getDao();
		DataHandleParam param = new DataHandleParam();
		return dao.edit(param);
	}

	@Override
	default Rp<T> remove(Set<String> ids) {
		return null;
	}

	@Override
	default Rp<T> remove(Map<String, String> query) {
		return null;
	}

	@Override
	default Rp<List<T>> list(Map<String, String> query) {
		// TODO
		BaseDao<T> dao = getDao();
		DataHandleParam param = new DataHandleParam();
		return dao.list(param);
	}
	
}
