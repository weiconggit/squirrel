package org.squirrel.framework.cache;

import java.util.Optional;

/**
 * @description 基础缓存操作
 * @author weicong
 * @time   2022年2月16日
 * @version 1.0
 */
public interface BaseCache {

    <T> Optional<T> get(String key, Class<T> clazz);

    Optional<Object> get(String key);

    void put(String key, Object object);

    /**
     * @param key
     * @param object
     * @param expiraTime 过期时间，秒
     */
    void put(String key, Object object, int expiraTime);

    /**
     * 加锁
     * @param lockKey
     * @return
     */
    void lock(String lockKey);

    /**
     * 释放锁
     * @param lockKey
     */
    void unlock(String lockKey);

    /**
     * 带超时的锁
     * @param lockKey
     * @param timeout 超时时间   单位：秒
     */
    void lock(String lockKey, int timeout) ;

    /**
     * 尝试获取锁
     * @param lockKey
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    boolean tryLock(String lockKey, int waitTime, int leaseTime);

}
