package org.squirrel.framework.auth;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.squirrel.framework.cache.BaseCache;
import org.squirrel.framework.response.Rp;
import org.squirrel.framework.response.RpEnum;
import org.squirrel.framework.util.StrUtil;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * <p>认证服务</p>
 * 如果需要框架的认证服务，则在业务中继承奔抽象类，实现方法即可
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
public abstract class AbstractAuthenticationController<T> implements AuthenticationController<T> {

	@Resource
	private BaseCache baseCache;

	@ApiOperation(value = "登录")
	@PostMapping(value = "login")
	public Rp<String> login(@RequestBody Object sysUserObject) {
		AuthorityUser authorityUser = new AuthorityUser();
		Rp<String> tRp = loadAuthority(sysUserObject, authorityUser);
		if (tRp.isSuccess()) {
			String token = StrUtil.getRandom(32);
			baseCache.put(token, authorityUser, 20*60);
			return Rp.success(token);
		}
		return tRp;
	}

	@ApiOperation(value = "退出")
	@GetMapping(value = "logout/{token}")
	public Rp<T> logout(@PathVariable("token") String token) {
		baseCache.remove(token);
		return Rp.success();
	}

	@Override
	@ApiOperation(value = "注册")
	@PostMapping(value = "register")
	public Rp<T> register(@RequestBody Object sysUserObject) {
		return insertSysUser(sysUserObject);
	}

	@Override
	@ApiOperation(value = "注销")
	@DeleteMapping(value = "logoff/{token}")
	public Rp<T> logoff(@PathVariable("token") String token) {
		Optional<AuthorityUser> authorityUser = baseCache.get(token, AuthorityUser.class);
		if (authorityUser.isEmpty()) {
			return Rp.failed(RpEnum.FAILED);
		}
		baseCache.remove(token);
		return deleteSysUser(authorityUser.get().getId());
	}

	/**
	 * 新增系统用户
	 * @param sysUserObject 用户信息
	 * @return 返回对象
	 */
	protected abstract Rp<T> insertSysUser(Object sysUserObject);

	/**
	 * 加载用户权限菜单到权限用户中
	 * @param sysUserObject 用户信息
	 * @return 返回对象
	 */
	protected abstract Rp<String> loadAuthority(Object sysUserObject, AuthorityUser authorityUser);

	/**
	 * 删除系统用户
	 * @param userId 用户id
	 * @return 返回对象
	 */
	protected abstract Rp<T> deleteSysUser(String userId);

}
