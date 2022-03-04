package org.squirrel.sys.user;

import org.springframework.stereotype.Service;
import org.squirrel.framework.database.AbstractBaseService;

import javax.annotation.Resource;

/**
 * <p>用户信息 Service</p>
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
@Service
public class UserService extends AbstractBaseService<UserVO> {

	@Resource
	private UserMapper userMapper;

}
