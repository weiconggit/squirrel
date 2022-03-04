package org.squirrel.framework.auth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.squirrel.framework.auth.annotation.Auth;
import org.squirrel.framework.cache.BaseCache;
import org.squirrel.framework.response.Rp;
import org.squirrel.framework.response.RpEnum;
import org.squirrel.framework.util.StrUtil;

import javax.annotation.Resource;

/**
 * <p>认证</p>
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
public interface AuthController {

	BaseCache getBaseCache();
	AuthUser loginUser(BaseUser baseUser);
	AuthUser registerUser(BaseUser baseUser);
	Rp<String> removeUser(String userId);

	default Rp<String> register(BaseUser baseUser) {
		AuthUser authUser = loginUser(baseUser);
		if (authUser == null){
			return Rp.failed(RpEnum.ERROR_USERNAME_OR_PASSWORD);
		}
		String token = StrUtil.getRandom(32);
		getBaseCache().put(token, authUser, 20*60);
		return Rp.success(token);
	}

	default Rp<String> login(BaseUser baseUser) {
		AuthUser authUser = registerUser(baseUser);
		if (authUser == null){
			return Rp.failed(RpEnum.ERROR_USERNAME_OR_PASSWORD);
		}
		String token = StrUtil.getRandom(32);
		getBaseCache().put(token, authUser, 20*60);
		return Rp.success(token);
	}

	default Rp<String> logout( String token) {
		getBaseCache().remove(token);
		return Rp.success();
	}

	default Rp<String> logoff(String token) {
		getBaseCache().remove(token);
		return Rp.success(token);
	}


}
