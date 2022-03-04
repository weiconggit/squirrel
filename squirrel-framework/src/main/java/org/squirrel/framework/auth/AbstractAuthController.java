package org.squirrel.framework.auth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.squirrel.framework.SquirrelInitializer;
import org.squirrel.framework.cache.BaseCache;
import org.squirrel.framework.database.BaseDao;
import org.squirrel.framework.database.data.DataConstant;
import org.squirrel.framework.response.Rp;
import org.squirrel.framework.response.RpEnum;
import org.squirrel.framework.spring.ApplicationContextHelper;
import org.squirrel.framework.util.StrUtil;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <p>认证服务 Cotroller</p>
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
public abstract class AbstractAuthController implements AuthController, SquirrelInitializer {

	private BaseCache baseCache;

	@Override
	public void init() {
//		Type[] actualTypeArguments = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
//		Class<T> classVO = (Class<T>) actualTypeArguments[0];
//		String simpleName = classVO.getSimpleName();
//		String daoSuffix = DataConstant.DAO_SUFFIX;
//		String beanSuffix = DataConstant.BEAN_SUFFIX;
//		String daoName = simpleName.substring(0, simpleName.length() - beanSuffix.length()) + daoSuffix;
//		daoName = StrUtil.lowerFirstLetter(daoName);
//		this.baseDao = (BaseDao<T>) ApplicationContextHelper.getBean(daoName);
	}

//	@ApiOperation(value = "注册")
//	@PostMapping(value = "register")
//	public Rp<String> register(@RequestBody BaseUser baseUser) {
//		AuthUser authUser = authUserConverter.convert(baseUser);
//		if (authUser == null){
//			return Rp.failed(RpEnum.ERROR_USERNAME_OR_PASSWORD);
//		}
//		String token = StrUtil.getRandom(32);
//		baseCache.put(token, authUser, 20*60);
//		return Rp.success(token);
//	}
//
//	@ApiOperation(value = "注销")
//	@PostMapping(value = "logoff/{token}")
//	public Rp<String> logoff(@PathVariable("token") String token) {
//		baseCache.remove(token);
//		return Rp.success(token);
//	}
//
//	@ApiOperation(value = "登录")
//	@PostMapping(value = "login")
//	public Rp<String> login(@RequestBody BaseUser baseUser) {
//		AuthUser authUser = authUserConverter.convert(baseUser);
//		if (authUser == null){
//			return Rp.failed(RpEnum.ERROR_USERNAME_OR_PASSWORD);
//		}
//		String token = StrUtil.getRandom(32);
//		baseCache.put(token, authUser, 20*60);
//		return Rp.success(token);
//	}
//
//	@ApiOperation(value = "退出")
//	@GetMapping(value = "logout/{token}")
//	public Rp<String> logout(@PathVariable("token") String token) {
//		baseCache.remove(token);
//		return Rp.success();
//	}


}
