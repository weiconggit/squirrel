package org.squirrel.framework.database;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.squirrel.framework.response.Rp;

/**
 * @description 
 * @author weicong
 * @time   2022年1月30日
 * @version 1.0
 */
public interface BaseController<T> {

	BaseService<T> getBaseService();
	
    Rp<T> add(T t);

    Rp<T> add(List<T> list);

    Rp<T> edit(String id, T t);

    Rp<T> remove(Set<String> ids);

    Rp<List<T>> list(Map<String, Object> query);
    
}
