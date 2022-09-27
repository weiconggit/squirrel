package org.squirrel.framework.data;

import org.squirrel.framework.response.Rp;

import java.util.LinkedHashMap;
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

    Rp<List<T>> insertBatch(List<T> list);

    Rp<T> update(T t);

    Rp<List<T>> updateBatch(List<T> list);

    Rp<T> delete(Map<String, Object> map);

    Rp<T> deleteByIds(Set<String> ids);

    Rp<T> selectById(String id);

    Rp<List<T>> selectByIds(Set<String> ids, LinkedHashMap<String, String> sortMap);

    Rp<List<T>> select(Map<String, Object> map, LinkedHashMap<String, String> sortMap);

    Rp<BasePage<T>> page(Map<String, Object> map, Integer current, Integer limit, LinkedHashMap<String, String> sortMap) ;

    
}
