package org.squirrel.framework.auth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.squirrel.framework.cache.BaseCache;
import org.squirrel.framework.response.Rp;

import javax.annotation.Resource;

/**
 * <p>默认系统服务 Cotroller</p>
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
@Api(tags={"系统服务"})
@RestController
@RequestMapping()
public class SysController {

	@Resource
	private BaseCache baseCache;

	@ApiOperation(value = "登录")
	@PostMapping(value = "loginIn")
	public Rp<AuthUser> loginIn(@RequestBody AuthUser authUser) {
		// TODO 获取用户权限信息接口
		AuthUser authUser1 = new AuthUser();
		// TODO 随机ID
		String token = "";
		baseCache.put(token, authUser1, 20*60);
		// TODO 返回数据待确定
		return Rp.success();
	}

	@ApiOperation(value = "登出")
	@GetMapping(value = "loginOut/{token}")
	public Rp<String> loginOut(@PathVariable("token") String token) {

//		baseCache.put("", authUser1, 20*60);
		return Rp.success();
	}

}
