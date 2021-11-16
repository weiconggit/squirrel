package org.squirrel.test.web;

import org.springframework.stereotype.Service;
import org.squirrel.framework.database.BaseDao;
import org.squirrel.framework.database.BaseService;
import org.squirrel.sys.user.UserVO;

import javax.annotation.Resource;

@Service
public class TestService implements BaseService<UserVO, String> {

    @Resource
    private TestMapper testMapper;

    @Override
    public TestMapper getDao() {
        return testMapper;
    }
}
