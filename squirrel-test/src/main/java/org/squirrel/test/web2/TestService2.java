package org.squirrel.test.web2;

import org.springframework.stereotype.Service;
import org.squirrel.test.web.TestService;

import javax.annotation.Resource;

@Service
public class TestService2 {

    @Resource
    private TestService testService;

    public void haha(){
        testService.select(null, "");
    }
}
