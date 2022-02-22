package org.squirrel.framework.cache;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description 本地缓存
 * @author weicong
 * @time   2022年2月16日 下午11:02:55
 * @version 1.0
 */
public class LocalBaseCache implements BaseCache {

	private static final Logger log = LoggerFactory.getLogger(LocalBaseCache.class);

	private static final Map<String, CacheObejct> map = new ConcurrentHashMap<>();

	private static final ReferenceQueue<Lock> queue = new ReferenceQueue<>();
	private static final Map<String, WeakReference<Lock>> mapLock = new ConcurrentHashMap<>();

	static {
		Thread thread = new Thread(() -> {
			while (true) {
				try {
					boolean needSleep = true;
					Reference<? extends Lock> poll = queue.poll();
					if (poll != null) {
						poll.clear();
						needSleep = false;
					}
					if (!map.isEmpty()) {
						long currentTimeMillis = System.currentTimeMillis();
						map.values().removeIf(v -> v.getRemoveTime() != null && currentTimeMillis > v.getRemoveTime());
						needSleep = false;
					}
					// 避免内存泄露
					if (mapLock.size() > 100_0000) {
						mapLock.clear();
					}
					if (needSleep) {
						Thread.sleep(5000);
					}
				} catch (Exception e) {
					log.error("thread error: ", e);
					break;
				}
			}
		});
		thread.start();
	}
	
	
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
		Lock lock = mapLock.computeIfAbsent(lockKey, kg -> new WeakReference<>(new ReentrantLock(), queue)).get();
		lock.lock();
	}

	@Override
	public void unlock(String lockKey) {
		mapLock.computeIfPresent(lockKey, (k, v) -> {
			v.get().unlock();
			return v;
		});
	}

	@Override
	public void lock(String lockKey, int expiraTime) {
		Lock lock = mapLock.computeIfAbsent(lockKey, kg -> new WeakReference<>(new ReentrantLock(), queue)).get();
		try {
			lock.tryLock(expiraTime, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.error("try lock error: ", e);
		}
	}

	@Override
	public boolean tryLock(String lockKey, int expiraTime, int waitTime) {
		Lock lock = mapLock.computeIfAbsent(lockKey, kg -> new WeakReference<>(new ReentrantLock(), queue)).get();
		try {
			return lock.tryLock(expiraTime, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.error("try lock error: ", e);
			return false;
		}
	}
	
	/**
	 * 缓存对象
	 */
	private class CacheObejct{
		private String key;	// 数据键
		private Object value; // 数据值
		private Integer expiraTime; // 过期时间，秒
		private Long removeTime; // 删除时间

		public CacheObejct(String key, Object value) {
			this.key = key;
			this.value = value;
		}

		public CacheObejct(String key, Object value, int expiraTime) {
			this.key = key;
			this.value = value;
			this.expiraTime = expiraTime;
			this.removeTime = System.currentTimeMillis() + expiraTime*1000;
		}

		public String getKey() {
			return key;
		}
		
		public Object getValue() {
			return value;
		}

		public Integer getExpiraTime() {
			return expiraTime;
		}
		public Long getRemoveTime() {
			return removeTime;
		}

	}

}
