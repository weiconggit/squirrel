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
	FAILED_ADD(-2, "新增失败"),
	FAILED_EDIT(-3, "编辑失败"),
	FAILED_REMOVE(-4, "删除失败"),
	ERROR_SYSTEM(-11, "系统繁忙"),
	ERROR_PARAMETER(-12, "参数错误"),
	ERROR_VALIDATE(-13, "校验错误"),
	NO_AUTHEN(-21, "请先登录"),
	NO_AUTHOR(-22, "没有权限");


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
