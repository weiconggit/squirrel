package org.squirrel.framework.response;

/**
 * @description 通用状态信息
 * @author weicong
 * @date 2019年6月9日 
 * @version 1.0
 */
public enum RpEnum {

	SUCCESS(0, "操作成功"), 
	ERROR_SYSTEM(1, "系统繁忙"),
	ERROR_PARAMETER(2, "参数错误"),
	ERROR_VALIDATE(3, "校验错误"),
	NO_AUTHEN(11, "请先登录"),
	NO_AUTHOR(12, "没有权限");

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
