package org.squirrel.test.web2;

import org.squirrel.sys.user.UserVO;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface TestInterface<T> {

    Type type = null; // public static final的，就算haha给它赋值了，其他的也会

    Integer haha = null;

    UserVO userVo = null;

    default void haha(){
        Type[] genericInterfaces = getClass().getGenericInterfaces();
        for (Type type: genericInterfaces) {
            if (type instanceof ParameterizedType){
                Type[] params = ((ParameterizedType) type).getActualTypeArguments();
                System.out.println(params[0].getTypeName());
                type = params[0];
                System.out.println(type.getTypeName());
            }
        }
//        haha = 6;
    }
}
