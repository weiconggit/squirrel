package org.squirrel.framework.response;

/**
 * @description 通用状态信息
 * @author weicong
 * @date 2019年6月9日 
 * @version 1.0
 */
public enum RpEnum {

	SUCCESS(0, "操作成功"),
	FAILED(-1, "操作失败"),
	ERROR_SYSTEM(-2, "系统繁忙"),
	ERROR_PARAMETER(-3, "参数错误"),
	ERROR_VALIDATE(-4, "校验错误"),

	NO_AUTHEN(-11, "登录已过期，请重新登录"),
	ERROR_USERNAME_REPEAT(-12, "该账号已被注册"),
	ERROR_SMS_CODE(-13, "短信验证码错误"),
	ERROR_IMG_CODE(-14, "图形验证码错误"),
	ERROR_USERNAME_OR_PASSWORD(-15, "账号或密码错误"),
	FAILED_WX_AUTH(-16, "微信授权失败"),
	FAILED_QQ_AUTH(-17, "QQ授权失败"),
	NO_AUTHOR(-18, "对不起，您没有该操作权限"),

	FAILED_ADD(-31, "新增失败"),
	FAILED_EDIT(-32, "编辑失败"),
	FAILED_REMOVE(-33, "删除失败");


	private final Integer code;
	private final String msg;

	RpEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}
