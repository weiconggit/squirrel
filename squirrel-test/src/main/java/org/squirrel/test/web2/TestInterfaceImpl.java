package org.squirrel.test.web2;

import org.squirrel.sys.user.User;
import org.squirrel.sys.user.UserVO;

public class TestInterfaceImpl implements TestInterface<UserVO>{



    public static void main(String[] args) {
        TestInterface<UserVO> test = new TestInterfaceImpl();
        test.haha();
    }
}
