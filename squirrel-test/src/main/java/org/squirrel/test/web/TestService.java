package org.squirrel.test.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.squirrel.framework.data.AbstractBaseService;
import org.squirrel.framework.data.BaseDao;
import org.squirrel.framework.data.BaseService;
import org.squirrel.framework.response.Rp;
import org.squirrel.sys.user.UserVO;

import java.util.Set;

@Service
public class TestService implements BaseService<UserVO> {

    @Resource
    private TestMapper testMapper;

    @Override
    public BaseDao<UserVO> getBaseDao() {
        return testMapper;
    }
}
