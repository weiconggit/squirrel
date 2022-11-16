package org.squirrel.framework.cache;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
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

	private static final Integer MAX_CACHE_COUNT = 100_0000;
	private static final Integer MAX_SLEEP_TIME = 5000;

	// 普通缓存
	private static final Map<String, CacheObejct> map = new ConcurrentHashMap<>();
	// 锁缓存队列，当内存不足、mapLok的锁失去引用时，lock对象会被放入queue中在清理线程中被清理
	private static final ReferenceQueue<Lock> queue = new ReferenceQueue<>();
	// 软引用锁缓存
	private static final Map<String, SoftReference<Lock>> mapLock = new ConcurrentHashMap<>();

	static {
		// 初始化过期清除线程
		Thread thread = new Thread(() -> {
			while (true) {
				try {
					// 是否需要线程间隔运行
					boolean needSleep = true;
					// 普通缓存清理过期数据
					if (!map.isEmpty()) {
						// 避免内存泄露
						if (map.size() > MAX_CACHE_COUNT) {
							log.error("LocalBaseCacheExpireThread map size is more than {}, map has been clear!", MAX_CACHE_COUNT);
							map.clear();
						} else {
							long currentTimeMillis = System.currentTimeMillis();
							map.values().removeIf(v -> v.getRemoveTime() != null && currentTimeMillis > v.getRemoveTime());
						}
					}
					// 锁缓存清理
					if (!mapLock.isEmpty() && mapLock.size() > MAX_CACHE_COUNT) {
						mapLock.clear();
					}
					// 清理失去引用的锁
					Reference<? extends Lock> poll = queue.poll();
					if (poll != null) {
						Lock lock = poll.get();
						if (lock != null) {
							// 如果是GC清除的软引用，锁可能还没有被释放，需要在此释放
							lock.unlock();
						}
						needSleep = false;
					}

					if (needSleep) {
						try {
							Thread.sleep(MAX_SLEEP_TIME);
						} catch (InterruptedException interruptedException) {
							log.error("LocalBaseCacheExpireThread sleep error", interruptedException);
							Thread.currentThread().interrupt();
						}
					}
				} catch (Exception e) {
					log.error("unknown error, while will be break", e);
					break;
				}
			}
		});
		thread.setName("LocalBaseCacheExpireThread");
		thread.start();
	}


	@Override
	public boolean remove(String key) {
		map.remove(key);
		return true;
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
			return Optional.of(clazz.cast(value));
		} catch (Exception e) {
			log.error("can not cast value {} to {}", value, clazz);
		}
		return Optional.empty();
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
	public void put(String key, Object object, int expireTime) {
		map.put(key, new CacheObejct(key, object, expireTime));
	}

	@Override
	public void lock(String lockKey) {
		Lock lock = mapLock.computeIfAbsent(lockKey, kg -> new SoftReference<>(new ReentrantLock(), queue)).get();
		// 一定不为null，不过idea会提示，为了消除提示加上不为null判断
		if (lock != null) {
			lock.lock();
		}
	}

	@Override
	public void unlock(String lockKey) {
		// 删除缓存、解锁
		SoftReference<Lock> remove = mapLock.remove(lockKey);
		if (remove != null) {
			Lock lock = remove.get();
			if (lock != null) {
				lock.unlock();
			}
		}
	}

	@Override
	public void lock(String lockKey, int expireTime) {
		Lock lock = mapLock.computeIfAbsent(lockKey, kg -> new SoftReference<>(new ReentrantLock(), queue)).get();
		try {
			if (lock != null) {
				lock.tryLock(expireTime, TimeUnit.SECONDS);
			}
		} catch (InterruptedException e) {
			log.error("try lock error: ", e);
		}
	}

	@Override
	public boolean tryLock(String lockKey, int expireTime, int waitTime) {
		Lock lock = mapLock.computeIfAbsent(lockKey, kg -> new SoftReference<>(new ReentrantLock(), queue)).get();
		try {
			if (lock != null) {
				return lock.tryLock(expireTime, TimeUnit.SECONDS);
			}
		} catch (InterruptedException e) {
			log.error("try lock error: ", e);
		}
		return false;
	}

	/**
	 * 缓存对象
	 */
	private static class CacheObejct{

		private String key;	// 数据键
		private Object value; // 数据值
		private Integer expireTime; // 过期时间，秒
		private Long removeTime; // 删除时间

		public CacheObejct(String key, Object value) {
			this.key = key;
			this.value = value;
		}

		public CacheObejct(String key, Object value, int expireTime) {
			this.key = key;
			this.value = value;
			this.expireTime = expireTime;
			this.removeTime = System.currentTimeMillis() + expireTime*1000;
		}

		public String getKey() {
			return key;
		}

		public Object getValue() {
			return value;
		}

		public Integer getExpireTime() {
			return expireTime;
		}

		public Long getRemoveTime() {
			return removeTime;
		}

	}

}
