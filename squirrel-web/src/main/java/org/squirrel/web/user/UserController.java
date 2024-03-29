package org.squirrel.web.user;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.squirrel.framework.auth.annotation.Authority;
import org.squirrel.framework.data.AbstractBaseController;

import javax.annotation.Resource;

/**
 * <p>用户信息 Cotroller</p>
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
@Authority("hahah")
@Api(tags={"基础服务-用户信息"})
@RestController
@RequestMapping(value = "user")
public class UserController extends AbstractBaseController<UserVO> {

	@Resource
	private UserService userService;



}
