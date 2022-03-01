package org.squirrel.framework.auth;

/**
 * 用户信息
 * @author weicong
 * @time   2022年3月1日
 * @version v1
 */
public interface BaseUser {

    String getUsername();

    String getPassword();

    String getPhone();

    String getWxOpenid();

    String getQqOpenid();
}
