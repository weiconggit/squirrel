package org.squirrel.test.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.squirrel.framework.data.web.AbstractBaseService;
import org.squirrel.sys.user.UserVO;

@Service
public class TestService extends AbstractBaseService<UserVO> {

    @Resource
    private TestMapper testMapper;

}
