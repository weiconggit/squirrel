package org.squirrel.framework.auth;

/**
 * @description 
 * @author weicong
 * @time   2021年1月30日 下午10:16:33
 * @version 1.0
 */
public interface AuthCache {

	String CACHE_KEY = "auth-";
	int OUTTIME = 20; // minutes
	
	AuthUser getAuth(String key);
	
	void putAuth(String key, AuthUser val);
	
	void delAuth(String key);
}
