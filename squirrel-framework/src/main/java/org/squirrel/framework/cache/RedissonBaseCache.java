package org.squirrel.framework.cache;

import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @description <p>RMapCache中可以指定某个键值对的过期时间（有专门线程清理，用完要调用destroy），但是可能
 * 造成内存泄露，而桶对象设置过期时间是由redis自身支持的
 * @author weicong
 * @time   2022年2月16日
 * @version 1.0
 */
public class RedissonBaseCache implements BaseCache {

    private static final Logger log = LoggerFactory.getLogger(LocalBaseCache.class);

    private static RedissonClient redissonClient;

    public RedissonBaseCache(RedissonClient redissonClient){
        RedissonBaseCache.redissonClient = redissonClient;
    }

    @Override
    public boolean remove(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        if (bucket != null) {
            return bucket.delete();
        }
        return false;
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> clazz) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        if (bucket != null && bucket.isExists()) {
            Object object = bucket.get();
            if (object != null) {
                try {
                    return Optional.of(clazz.cast(object));
                } catch (Exception e) {
                    log.error("can not cast value {} to {}", object, clazz);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Object> get(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        if (bucket != null && bucket.isExists()) {
            return Optional.ofNullable(bucket.get());
        }
        return Optional.empty();
    }

    @Override
    public void put(String key, Object object) {
        redissonClient.getBucket(key).set(object);
    }

    @Override
    public void put(String key, Object object, int expireTime) {
        redissonClient.getBucket(key).set(object, expireTime, TimeUnit.SECONDS);
    }

    @Override
    public void lock(String lockKey) {
        redissonClient.getLock(lockKey).lock();
    }

    @Override
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        if (lock != null) {
            lock.unlock();
        }
    }

    @Override
    public void lock(String lockKey, int timeout) {
        redissonClient.getLock(lockKey).lock(timeout, TimeUnit.SECONDS);
    }

    @Override
    public boolean tryLock(String lockKey, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("try lock error: ", e);
        }
        return false;
    }
}
