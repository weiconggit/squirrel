package org.squirrel.framework.cache;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.util.concurrent.Striped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description 本地缓存
 * @author weicong
 * @time   2022年2月16日 下午11:02:55
 * @version 1.0
 */
public class LocalBaseCache implements BaseCache {

	private static final Logger log = LoggerFactory.getLogger(LocalBaseCache.class);

	private static final Map<String, CacheObejct> map = new ConcurrentHashMap<>();

//	private ReferenceQueue<ReentrantLock> queue = new ReferenceQueue<>();
//	private static final Map<String, CacheObejct> map = new ConcurrentHashMap<>();

	@Override
	public <T> Optional<T> get(String key, Class<T> clazz){
		CacheObejct cacheObejct = map.get(key);
		if (cacheObejct == null) {
			return Optional.empty();
		}
		Object value = cacheObejct.getValue();
		if (value == null) {
			return Optional.empty();
		}
		try {
			return Optional.ofNullable((T)value);
		} catch (Exception e) {
			log.error("can not cast value {} to {}", value, clazz);
			return Optional.empty();
		}
	}

	@Override
	public Optional<Object> get(String key) {
		CacheObejct cacheObejct = map.get(key);
		if (cacheObejct == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(cacheObejct.getValue());
	};

	@Override
	public void put(String key, Object object) {
		map.put(key, new CacheObejct(key, object));
	}

	@Override
	public void put(String key, Object object, int expiraTime) {
		map.put(key, new CacheObejct(key, object, expiraTime));
	}

	@Override
	public void lock(String lockKey) {
//		lockStriped.get(lockKey);
	}

	@Override
	public void unlock(String lockKey) {
//		lockMap.computeIfAbsent(lockKey, k -> new ReentrantLock()).lock();
	}

	@Override
	public void lock(String lockKey, int timeout) {
		// TODO
	}

	@Override
	public boolean tryLock(String lockKey, int waitTime, int leaseTime) {
		// TODO
		return false;
	}

	/**
	 * 缓存对象
	 */
	private class CacheObejct{
		private String key;	// 数据键
		private Object value; // 数据值
		private int expiraTime; // 过期时间，秒

		public CacheObejct(String key, Object value) {
			this.key = key;
			this.value = value;
		}

		public CacheObejct(String key, Object value, int expiraTime) {
			this.key = key;
			this.value = value;
			this.expiraTime = expiraTime;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public int getExpiraTime() {
			return expiraTime;
		}

		public void setExpiraTime(int expiraTime) {
			this.expiraTime = expiraTime;
		}
	}

}
