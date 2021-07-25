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
		
	public Rp<T> add(T data);
	
	public Rp<T> edit(String id, T data);
	
	public Rp<T> delete(Set<String> ids);
	
	public Rp<Collection<T>> list(Map<String, Object> query);
	
	public Rp<PageVO<T>> page(Map<String, Object> query, String current, String size, String sort) ;

	public Rp<T> detail(String id);

}
