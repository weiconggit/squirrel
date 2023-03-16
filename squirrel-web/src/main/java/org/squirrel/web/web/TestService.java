package org.squirrel.web.web;

import org.springframework.stereotype.Service;
import org.squirrel.framework.data.BaseDao;
import org.squirrel.framework.data.BaseService;
import org.squirrel.web.user.UserVO;

import javax.annotation.Resource;

@Service
public class TestService implements BaseService<UserVO> {

    @Resource
    private TestMapper testMapper;

    @Override
    public BaseDao<UserVO> getBaseDao() {
        return testMapper;
    }
}
