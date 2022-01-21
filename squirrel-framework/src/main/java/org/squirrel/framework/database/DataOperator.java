package org.squirrel.framework.database;

import org.squirrel.framework.response.Rp;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description 数据操作器
 * @author weicong
 * @time   2022年1月20日
 * @version 1.0
 */
public interface DataOperator<T> {

    Rp<T> add(T t);

    Rp<T> add(List<T> list);

    Rp<T> edit(String id, T t);

    Rp<T> remove(Set<String> ids);

    Rp<List<T>> list(Map<String, Object> query);

//    Rp<PageVO<T>> page(Map<String, Object> query, String current, String size, String sort) ;
//
//    Rp<T> detail(String id);
}
