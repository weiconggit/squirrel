package org.squirrel.framework.auth;

/**
 * 将登录用户信息转换为权限用户信息
 * @author weicong
 * @time   2022年2月24日
 * @version 1.0
 */
public interface AuthUserConverter {

    AuthUser convert(LoginUser loginUser);

}
