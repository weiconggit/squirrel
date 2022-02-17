package org.squirrel.framework.cache;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.squirrel.framework.auth.AuthUser;

import java.util.Optional;

/**
 * @description 
 * @author weicong
 * @time   2022年2月16日 下午11:04:53
 * @version 1.0
 */
public class RedissonBaseCache implements BaseCache{

    private static final Logger log = LoggerFactory.getLogger(LocalBaseCache.class);

    private RedissonClient redissonClient;

    public void setRedissonCache(RedissonClient redissonClient) {
        redissonClient = redissonClient;
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> clazz) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        if (bucket.isExists()) {
            Object object = bucket.get();
            if (object != null) {
                try {
                    return Optional.ofNullable((T)object);
                } catch (Exception e) {
                    log.error("can not cast value {} to {}", object, clazz);
                    return Optional.empty();
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Object> get(String key) {
        return Optional.empty();
    }

    @Override
    public void put(String key, Object object) {

    }

    @Override
    public void put(String key, Object object, int expiraTime) {

    }
}
