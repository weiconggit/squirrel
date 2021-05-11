package org.squirrel.framework.response;

import org.squirrel.framework.SquirrelProperties;
import org.squirrel.framework.util.StrUtil;

import java.io.Serializable;

/**
 * @description 请求返回体
 * @author weicong
 * @date 2019年6月9日
 * @version 1.0
 */
public final class Rp<E> implements Serializable {

	private static final long serialVersionUID = -939399577904238648L;

	private Integer code; // 响应状态码
	private String msg; // 该状态码对应的提示信息
	private E data; // 响应数据

	private Rp(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private Rp(Integer code, String msg, E data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	// ~ 对外方法
	// ======================================================

	public static <E> Rp<E> success() {
		return build(RpEnum.SUCCESS);
	}

	public static <E> Rp<E> success(E data) {
		return new Rp<>(RpEnum.SUCCESS.getCode(), RpEnum.SUCCESS.getMsg(), data);
	}
	
	public static <E> Rp<E> failed(RpEnum rpEnum) {
		return build(rpEnum);
	}
	
	public static <E> Rp<E> failed(RpEnum rpEnum, String msg) {
		return new Rp<>(rpEnum.getCode(), msg);
	}

	public static <E> Rp<E> failed(Integer propertyKey) {
		if (propertyKey != null) {
			String propertyVal = SquirrelProperties.get(propertyKey);
			if (StrUtil.isNum(propertyVal)) {
				return new Rp<>(propertyKey, propertyVal);
			}
		}
		return build(RpEnum.ERROR_SYSTEM);
	}

	private static <E> Rp<E> build(RpEnum rpEnum) {
		return new Rp<>(rpEnum.getCode(), rpEnum.getMsg());
	}

	// ~ getter & setter
	// ======================================================

	/**
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(Integer code) {
		this.code = code;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the data
	 */
	public E getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(E data) {
		this.data = data;
	}
}
