package org.squirrel.framework.database;

import org.squirrel.framework.database.bean.DataHandleParam;
import org.squirrel.framework.response.Rp;
import org.squirrel.framework.response.RpEnum;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description 数据处理接口
 * @author weicong
 * @time   2021年8月11日
 * @version 1.0
 */
public interface DataHandler<T> {

    Rp<T> add(List<T> list) ;

    Rp<T> edit(List<T> list);

    Rp<T> remove(Set<String> ids);

    Rp<T> remove(Map<String, String> query);

    Rp<List<T>> list(Map<String, String> query);


    Rp<T> add(DataHandleParam param);

    Rp<T> edit(DataHandleParam param) ;

    Rp<T> remove(DataHandleParam param);

    Rp<List<T>> list(DataHandleParam param);

}
