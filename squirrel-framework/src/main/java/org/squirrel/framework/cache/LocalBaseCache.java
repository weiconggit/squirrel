package org.squirrel.framework.cache;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description 本地缓存
 * @author weicong
 * @time   2022年2月16日 下午11:02:55
 * @version 1.0
 */
public final class LocalBaseCache {

	private static final Map<String, Object> map = new ConcurrentHashMap<>();
	
	
	public <T> Optional<T> get(String key, Class<T> clazz){
		Object object = map.get(key);
		if (object == null) {
			return Optional.ofNullable(null);
		}
		if (object.getClass().equals(clazz)) {
			 return Optional.ofNullable((T)object);
		}
		return Optional.ofNullable(null);
	}
	
	public Optional<Object> get(String key) {
		return Optional.ofNullable(map.get(key));
	};
	
	public void put(String key, Object object) {
		map.put(key, object);
	}
	
	public void put(String key, Object object, Integer expeirTime) {
		map.put(key, object);
	}
	
}
