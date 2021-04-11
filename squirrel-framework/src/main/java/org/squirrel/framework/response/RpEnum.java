package org.squirrel.framework.response;

/**
 * @description 通用状态信息
 * @author weicong
 * @date 2019年6月9日 
 * @version 1.0
 */
public enum RpEnum {

	SUCCESS(0, "操作成功"), 
	ERROR_SYSTEM(-1, "系统繁忙,请稍后再试"), 
	ERROR_NOFOUND(-2, "无法找到相应的数据"), 
	ERROR_PARAMETER(-3, "参数错误"),
	NO_AUTHEN(-4, "请先登录"),
	NO_PERMIT(-5, "登录已过期，请重新登录"),
	NO_AUTHOR(-6, "没有权限"),
	DUPLICATE_PHONE(-7, "手机号码已被使用"),
	CUSTOM_MSG(-8, "");

	private Integer code;

	private String msg;

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
