package org.squirrel.framework.database;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.squirrel.framework.database.bean.PageVO;
import org.squirrel.framework.response.Rp;


/**
 * controller层抽象接口
 * @author weicong
 * @time   2020年11月25日
 * @version 1.0
 */
public interface BaseController<T> {
		
	Rp<T> add(T data);
	
	Rp<T> edit(String id, T data);
	
	Rp<T> delete(Set<String> ids);
	
	Rp<Collection<T>> list(Map<String, Object> query);
	
	Rp<PageVO<T>> page(Map<String, Object> query, String current, String size, String sort) ;

	Rp<T> detail(String id);

}
