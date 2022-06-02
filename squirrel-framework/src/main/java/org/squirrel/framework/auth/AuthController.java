package org.squirrel.framework.auth;

import org.squirrel.framework.cache.BaseCache;
import org.squirrel.framework.response.Rp;
import org.squirrel.framework.response.RpEnum;
import org.squirrel.framework.util.StrUtil;

import java.util.Optional;

/**
 * <p>认证</p>
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
public interface AuthController {

	/**
	 * 账号密码登录
	 */
	Rp<String> login(Object objectUser);

	/**
	 * 登出
	 */
	Rp<String> logout(String token);

	/**
	 * 注册
	 */
	Rp<String> register(Object objectUser);

	/**
	 * 注销
	 */
	Rp<String> logoff(String token);


}
