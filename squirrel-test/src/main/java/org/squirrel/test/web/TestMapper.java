package org.squirrel.test.web;

import org.springframework.stereotype.Repository;
import org.squirrel.framework.data.BaseDao;
import org.squirrel.test.user.User;
import org.squirrel.test.user.UserVO;

/**
 * <p>用户信息 Mapper</p>
 * @author weicong
 * @time   2021-02-08 
 * @version v1
 */
@Repository
public interface TestMapper extends BaseDao<UserVO> {
//public interface TestMapper  {

	@Override
	default Class<User> getEntityClass() {
		return User.class;
	}

}
