package org.squirrel.framework.data;

import org.squirrel.framework.data.web.BasePage;
import org.squirrel.framework.response.Rp;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description 数据操作
 * @author weicong
 * @time   2022年1月20日
 * @version 1.0
 */
public interface DataOperator<T> {

    Rp<T> insert(T t);

    Rp<T> insert(List<T> list);

    Rp<T> update(T t);

    Rp<T> delete(Set<String> ids);
    
    Rp<T> detail(String id);

    Rp<List<T>> select(Map<String, Object> query, String sort);

    Rp<BasePage<T>> page(Map<String, Object> query, Integer current, Integer limit, String sort) ;

    
}
