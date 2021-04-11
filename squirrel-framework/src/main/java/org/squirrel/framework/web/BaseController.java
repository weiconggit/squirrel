package org.squirrel.framework.web;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.squirrel.framework.response.Rp;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * controller层抽象接口
 * @author weicong
 * @time   2020年11月25日
 * @version 1.0
 */
public interface BaseController<V> {
		
	public Rp<Collection<V>> list(Map<String, Object> query);
	
	public Rp<Page<V>> page(Map<String, Object> query, String current, String size, String sort) ;

	public Rp<V> detail(String id);

	public Rp<V> add(V data);

	public Rp<V> edit(String id, V data);
	
	public Rp<V> delete(Set<String> ids);
	
}
