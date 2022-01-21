package org.squirrel.sys.user;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.squirrel.framework.database.AbstractBaseController;

import io.swagger.annotations.Api;
import org.squirrel.framework.database.BaseService;

/**
 * <p>用户信息 Cotroller</p>
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
@Api(tags={"基础服务-用户信息"})
@RestController
@RequestMapping(value = "user")
public class UserController extends AbstractBaseController<UserVO>{

	@Resource
	private UserService userService;

	@Override
	public BaseService<UserVO> getService() {
		return userService;
	}


}
