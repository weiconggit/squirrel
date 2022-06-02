package org.squirrel.framework.auth;

import org.squirrel.framework.database.BaseService;

/**
* @description
* @author weicong
* @time   2022年3月8日
* @version 1.0
*/
public interface AuthService {

    /**
     * 新增权限用户
     * @param objectUser
     * @return
     */
     AuthorityUser insertAuthority(Object objectUser);

    /**
     * 装载权限用户
     * @param objectUser 用户信息
     * @return 权限
     */
    AuthorityUser loadAuthority(Object objectUser);

    /**
     * 删除权限用户
     * @param objectUserId
     * @return
     */
    boolean removeAuthority(AuthorityUser authorityUser);



}
