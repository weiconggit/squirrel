package org.squirrel.test.web;

import org.springframework.stereotype.Service;
import org.squirrel.framework.database.BaseService;
import org.squirrel.sys.user.UserVO;

import javax.annotation.Resource;

@Service
public class TestService implements BaseService<UserVO> {

    @Resource
    private TestMapper testMapper;

    @Override
    public TestMapper getDao() {
        return testMapper;
    }
}