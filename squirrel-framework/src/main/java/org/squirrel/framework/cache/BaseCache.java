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
     * 删除缓存
     * @param key 缓存key
     * @return 是否删除成功
     */
    boolean remove(String key);

    /**
     * 获取一个指定类型的缓存对象
     * @param key 缓存key
     * @param clazz 缓存对象class
     * @param <T> 缓存对象类型
     * @return 指定类型的缓存对象
     */
    <T> Optional<T> get(String key, Class<T> clazz);

    /**
     * 获取一个缓存对象
     * @param key 缓存key
     * @return 缓存对象
     */
    Optional<Object> get(String key);

    /**
     * 放入一个缓存对象
     * @param key 缓存key
     * @param object 缓存对象
     */
    void put(String key, Object object);

    /**
     * 放入一个缓存对象，并设置过期时间
     * @param key 缓存key
     * @param object 缓存对象
     * @param expireTime 过期时间，秒
     */
    void put(String key, Object object, int expireTime);

    /**
     * 加锁
     * @param lockKey 加锁key
     */
    void lock(String lockKey);

    /**
     * 释放锁
     * @param lockKey 解锁key
     */
    void unlock(String lockKey);

    /**
     * 带超时的锁
     * @param lockKey 加锁key
     * @param timeout 超时时间   单位：秒
     */
    void lock(String lockKey, int timeout) ;

    /**
     * 尝试获取锁
     * @param lockKey 加锁key
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return 是否成功
     */
    boolean tryLock(String lockKey, int waitTime, int leaseTime);

}
