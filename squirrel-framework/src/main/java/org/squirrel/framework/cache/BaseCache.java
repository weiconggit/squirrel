package org.squirrel.framework.cache;

import java.util.Optional;

/**
 * @description 基础缓存操作
 * @author weicong
 * @time   2022年2月16日
 * @version 1.0
 */
public interface BaseCache {

    /**
     * 默认过期时间 20 分钟
     */
    int DEFAULT_EXPIRA_TIME = 20*60;

    <T> Optional<T> get(String key, Class<T> clazz);

    Optional<Object> get(String key);

    void put(String key, Object object);

    void put(String key, Object object, int expiraTime);
}
