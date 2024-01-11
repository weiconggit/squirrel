package org.squirrel.web.web;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.squirrel.framework.auth.AbstractAuthenticationController;
import org.squirrel.framework.auth.AuthenticationController;
import org.squirrel.framework.auth.AuthorityUser;
import org.squirrel.framework.response.Rp;
import org.squirrel.framework.response.RpEnum;
import org.squirrel.web.user.UserVO;

import java.util.LinkedHashMap;

/**
 * <p>认证服务</p>
 * @author weicong
 * @time   2023-10-24
 * @version v1
 */
@Api(tags = "认证服务")
@RequestMapping("auth")
@RestController
public class SquirrelAuthenticationController extends AbstractAuthenticationController<UserVO> {

    @Override
    protected Rp<UserVO> insertSysUser(Object sysUserObject) {
        return null;
    }

    @Override
    protected Rp<String> loadAuthority(Object sysUserObject, AuthorityUser authorityUser) {
        // TODO 测试数据
        LinkedHashMap cast = (LinkedHashMap) sysUserObject;
        if ("123".equals(cast.get("username")) && "123".equals(cast.get("password"))) {
            return Rp.success();
        }
        return Rp.failed(RpEnum.ERROR_USERNAME_OR_PASSWORD);
    }

    @Override
    protected Rp<UserVO> deleteSysUser(String userId) {
        return null;
    }
}
