package org.squirrel.framework.cache;

import java.time.Duration;

import org.squirrel.framework.auth.AuthCache;
import org.squirrel.framework.auth.AuthUser;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

/**
 * @author weicong
 * @time   2021年1月29日
 * @version 1.0
 */
public class CaffeineSquirrelCache implements AuthCache {

	private final LoadingCache<String, AuthUser> cache;
	
	public CaffeineSquirrelCache() {
		this.cache = Caffeine.newBuilder()
		.expireAfterAccess(Duration.ofMinutes(OUTTIME))
		.build(k -> null);
	}

	@Override
	public AuthUser getAuth(String key) {
		return cache.get(key);
	}

	@Override
	public void putAuth(String key, AuthUser val) {
		cache.put(key, val);
	}

	@Override
	public void delAuth(String k) {
		cache.invalidate(k);
	}
}
