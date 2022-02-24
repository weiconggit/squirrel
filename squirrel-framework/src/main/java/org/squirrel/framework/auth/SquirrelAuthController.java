package org.squirrel.framework.auth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.squirrel.framework.cache.BaseCache;
import org.squirrel.framework.response.Rp;
import org.squirrel.framework.util.StrUtil;

import javax.annotation.Resource;

/**
 * <p>认证服务 Cotroller</p>
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
@Api(tags={"认证服务"})
@RestController
@RequestMapping()
public class SquirrelAuthController {

	@Resource
	private BaseCache baseCache;
	@Resource
	private AuthUserConverter authUserConverter;

	@ApiOperation(value = "登录")
	@PostMapping(value = "loginIn")
	public Rp<String> loginIn(@RequestBody LoginUser loginUser) {
		AuthUser authUser = authUserConverter.convert(loginUser);
		String token = StrUtil.getRandom(32);
		baseCache.put(token, authUser, 20*60);
		return Rp.success(token);
	}

	@ApiOperation(value = "登出")
	@GetMapping(value = "loginOut/{token}")
	public Rp<String> loginOut(@PathVariable("token") String token) {
		baseCache.remove(token);
		return Rp.success();
	}


}
