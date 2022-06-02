package org.squirrel.framework.auth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.squirrel.framework.cache.BaseCache;
import org.squirrel.framework.response.Rp;
import org.squirrel.framework.response.RpEnum;
import org.squirrel.framework.util.StrUtil;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * <p>认证服务 Cotroller</p>
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
@Api
@RestController
@RequestMapping
public class DefaultAuthController implements AuthController {

	@Resource
	private BaseCache baseCache;
	@Resource
	private AuthService authService;

	@Override
	@ApiOperation(value = "注册")
	@PostMapping(value = "register")
	public Rp<String> register(@RequestBody Object objectUser) {
		AuthorityUser authorityUser = authService.insertAuthority(objectUser);
		if (authorityUser == null){
			return Rp.failed(RpEnum.FAILED);
		}
		String token = StrUtil.getRandom(32);
		baseCache.put(token, authorityUser, 20*60);
		return Rp.success(token);
	}

	@Override
	@ApiOperation(value = "注销")
	@PostMapping(value = "logoff/{token}")
	public Rp<String> logoff(@PathVariable("token") String token) {
		Optional<AuthorityUser> authorityUser = baseCache.get(token, AuthorityUser.class);
		if (authorityUser.isEmpty()) {
			return Rp.failed(RpEnum.FAILED);
		}
		if (!authService.removeAuthority(authorityUser.get())) {
			return Rp.failed(RpEnum.FAILED);
		}
		baseCache.remove(token);
		return Rp.success(token);
	}

	@ApiOperation(value = "登录")
	@PostMapping(value = "login")
	public Rp<String> login(@RequestBody Object objectUser) {
		AuthorityUser authorityUser = authService.loadAuthority(objectUser);
		if (authorityUser == null){
			return Rp.failed(RpEnum.ERROR_USERNAME_OR_PASSWORD);
		}
		String token = StrUtil.getRandom(32);
		baseCache.put(token, authorityUser, 20*60);
		return Rp.success(token);
	}

	@ApiOperation(value = "退出")
	@GetMapping(value = "logout/{token}")
	public Rp<String> logout(@PathVariable("token") String token) {
		baseCache.remove(token);
		return Rp.success();
	}


}
