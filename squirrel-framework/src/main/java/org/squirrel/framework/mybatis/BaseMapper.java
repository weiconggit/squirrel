package org.squirrel.framework.mybatis;

import java.util.Collection;

/**
 * @description 
 * @author weicong
 * @time   2021年3月14日 上午11:53:19
 * @version 1.0
 */
public interface BaseMapper<T, K> {

	/**
	 * 查询详情
	 * @param k
	 * @return
	 */
	T selectById(K k);
	
	/**
	 * 查询列表
	 * @param ids
	 * @return
	 */
	Collection<T> selectByIds(Collection<K> ids);
	
	/**
	 * 分页
	 * @param page
	 * @param limit
	 * @return
	 */
	int selectPage(int page, int limit);
	
	/**
	 * 逻辑删除
	 * @return
	 */
	int deleteByIdLogic(K id);
	
	/**
	 * 批量逻辑删除
	 * @param id
	 * @return
	 */
	int deleteByIdsLogic(Collection<K> ids);
	
	/**
	 * 删除
	 * @return
	 */
	int deleteById(K id);
	
	/**
	 * 批量删除
	 * @return
	 */
	int deleteByIds(Collection<K> ids);
	
	/**
	 * 新增
	 * @param t
	 * @return
	 */
	int insert(T t);
	
	/**
	 * 批量新增
	 * @param list
	 * @return
	 */
	int insertBatch(Collection<T> list);
	
	/**
	 * 更新
	 * @param k
	 * @return
	 */
	int updateById(T t);
	
	/**
	 * 批量更新
	 * @param ids
	 * @return
	 */
	int updateByIds(Collection<T> list);
	
	/**
	 * 更新非空字段
	 * @param t
	 * @return
	 */
	int updateNotEmptyById(T t);
	
	/**
	 * 批量更新非空字段
	 * @param list
	 * @return
	 */
	int updateNotEmptyByIds(Collection<T> list);
}
