package org.squirrel.sys.user;

import org.springframework.stereotype.Repository;
import org.squirrel.framework.data.BaseDao;

/**
 * <p>用户信息 Mapper</p>
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
@Repository
public interface UserMapper extends BaseDao<UserVO> {

	@Override
	default Class<User> getBeanClass() {
		return User.class;
	}

}
