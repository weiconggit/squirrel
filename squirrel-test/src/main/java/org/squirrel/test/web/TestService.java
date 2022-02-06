package org.squirrel.test.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.squirrel.framework.database.DefaultBaseService;
import org.squirrel.sys.user.UserVO;

@Service
public class TestService extends DefaultBaseService<UserVO> {

    @Resource
    private TestMapper testMapper;

}
