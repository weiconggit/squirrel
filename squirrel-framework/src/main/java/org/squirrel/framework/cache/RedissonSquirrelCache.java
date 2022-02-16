package org.squirrel.framework.cache;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.squirrel.framework.auth.AuthCache;
import org.squirrel.framework.auth.AuthUser;

/**
 * <p>RMapCache中可以指定某个键值对的过期时间（有专门线程清理，用完要调用destroy），但是可能
 * 造成内存泄露，而桶对象设置过期时间是由redis自身支持的
 * 
 * @author weicong
 * @time   2021年1月29日
 * @version 1.0
 */
@Deprecated
public class RedissonSquirrelCache implements AuthCache {

	private static RedissonClient redissonClient;
	
	public static void setRedissonCache(RedissonClient redissonC) {
		redissonClient = redissonC;
	}
	
	@Override
	public AuthUser getAuth(String key) {
		RBucket<Object> bucket = redissonClient.getBucket(key);
		if (bucket.isExists()) {
			Object object = bucket.get();
			if (object instanceof AuthUser) {
				return (AuthUser)object;
			}
		}
		return null;
	}

	@Override
	public void putAuth(String key, AuthUser val) {
		redissonClient.getBucket(key).set(val, OUTTIME, TimeUnit.MINUTES);
	}

	@Override
	public void delAuth(String key) {
		RBucket<Object> bucket = redissonClient.getBucket(key);
		if (bucket.isExists()) {
			bucket.delete();
		}
	}
	
    /**
     * 加锁
     * @param lockKey
     * @return
     */
    public static RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * 释放锁
     * @param lockKey
     */
    public static void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }
    
    /**
     * 释放锁
     * @param lock
     */
    public static void unlock(RLock lock) {
        lock.unlock();
    }

    /**
     * 带超时的锁
     * @param lockKey
     * @param timeout 超时时间   单位：秒
     */
    public static RLock lock(String lockKey, int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, TimeUnit.SECONDS);
        return lock;
    }
    
    /**
     * 带超时的锁
     * @param lockKey
     * @param unit 时间单位
     * @param timeout 超时时间
     */
    public static RLock lock(String lockKey, TimeUnit unit , int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }
    
    /**
     * 尝试获取锁
     * @param lockKey
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public static boolean tryLock(String lockKey, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }
    
    /**
     * 尝试获取锁
     * @param lockKey
     * @param unit 时间单位
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public static boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }
}
