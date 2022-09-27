package org.squirrel.framework.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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

    Rp<T> edit(String id, T t);

    Rp<T> remove(Set<String> ids);

    Rp<T> detail(String id);

    Rp<List<T>> list(Map<String, Object> query);

    Rp<BasePage<T>> page(Map<String, Object> query, Integer current, Integer limit);
}
