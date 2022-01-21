package org.squirrel.test.web;

import org.springframework.stereotype.Repository;
import org.squirrel.framework.database.BaseDao;
import org.squirrel.sys.user.UserVO;

/**
 * <p>用户信息 Mapper</p>
 * @author weicong
 * @time   2021-02-08 
 * @version v1
 */
@Repository
public interface TestMapper extends BaseDao<UserVO> {

}
