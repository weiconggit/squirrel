package org.squirrel.framework.auth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.squirrel.framework.database.DefaultBaseController;
import org.squirrel.framework.response.Rp;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>用户信息 Cotroller</p>
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
@Api(tags={"系统服务"})
@RestController
@RequestMapping()
public class SysController {

	@Auth(AuthMenuLoader.ADD)
	@ApiOperation(value = "登录")
	@PostMapping(value = "login")
	public Rp<AuthUser> add(@RequestBody AuthUser authUser) {

		return null;
	}



}
