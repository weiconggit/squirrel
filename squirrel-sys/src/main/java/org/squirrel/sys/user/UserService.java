package org.squirrel.sys.user;

import org.springframework.stereotype.Service;
import org.squirrel.framework.database.DefaultBaseService;
import org.squirrel.framework.database.BaseDao;
import org.squirrel.framework.database.BaseService;

import javax.annotation.Resource;

/**
 * <p>用户信息 Service</p>
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
@Service
public class UserService extends DefaultBaseService<UserVO>{

	@Resource
	private UserMapper userMapper;

}
