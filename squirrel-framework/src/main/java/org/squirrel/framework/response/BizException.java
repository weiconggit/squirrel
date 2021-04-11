package org.squirrel.framework.response;

import java.text.MessageFormat;

/**
 * @description 业务异常信息
 * @author weicong
 * @date 2019年6月9日 
 * @version 1.0
 */
public final class BizException extends RuntimeException {
	
	private static final long serialVersionUID = -1971580736283989021L;

	private Integer code;
    private Integer frontCode;
    private String message;

	public BizException(String exceptionMsg) {
    	this(RpEnum.ERROR_SYSTEM, exceptionMsg);
	}
    
    public BizException(RpEnum rpEnum, String exceptionMsg, Object ... args) {
        super(null, null, true, false);
        this.frontCode = rpEnum.getCode();
        this.message = MessageFormat.format(exceptionMsg, args).replaceAll("\\{\\d+\\}", "");
    }
    
    public BizException(RpEnum rpEnum) {
        super(null, null, true, false);
        this.code = rpEnum.getCode();
        this.frontCode = rpEnum.getCode();
        this.message = rpEnum.getMsg();
    }

	/**
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * @return the frontCode
	 */
	public Integer getFrontCode() {
		return frontCode;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

    
}
