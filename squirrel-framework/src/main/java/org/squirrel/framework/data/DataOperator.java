package org.squirrel.framework.data;

import org.squirrel.framework.SquirrelProperties;
import org.squirrel.framework.response.Rp;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description 数据操作
 * @author weicong
 * @time   2022年1月20日
 * @version 1.0
 */
public interface DataOperator<T> {

    String DATA_ID = SquirrelProperties.get("dataId");
    String DATA_IS_DEL = SquirrelProperties.get("dataIsDel");
    String DAO_SUFFIX = SquirrelProperties.get("daoSuffix");
    String SERVICE_SUFFIX = SquirrelProperties.get("serviceSuffix");
    String BEAN_SUFFIX = SquirrelProperties.get("beanSuffix");

    // 标识操作方法类型
    String INSERT = "insert";
    String INSERT_BATCH = "insertBatch";
    String UPDATE = "update";
    String DELETE = "delete";

    Rp<T> insert(T t);

    Rp<List<T>> insertBatch(List<T> list);

    Rp<T> update(T t);

    Rp<T> logicDeleteByIds(Set<String> ids);

    Rp<T> logicDelete(List<WhereBean> whereBeans);

    Rp<T> deleteByIds(Set<String> ids);

    Rp<T> delete(List<WhereBean> whereBeans);

    Rp<T> selectById(String id);

    Rp<List<T>> selectByIds(Set<String> ids, List<OrderBean> orderBeans);

    Rp<List<T>> select(List<WhereBean> whereBeans, List<OrderBean> orderBeans);

    Rp<BasePage<T>> page(List<WhereBean> whereBeans, Integer current, Integer limit, List<OrderBean> orderBeans) ;

    
}
