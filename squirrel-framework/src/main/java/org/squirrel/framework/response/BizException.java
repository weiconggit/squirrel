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

	private final Integer code;
    private final String message;

	public BizException(String msg) {
    	this(RpEnum.ERROR_SYSTEM, msg);
	}
    
    public BizException(RpEnum rpEnum, String msg, Object ... args) {
        super(null, null, true, false);
        this.code = rpEnum.getCode();
        this.message = MessageFormat.format(msg, args).replaceAll("\\{\\d+\\}", "");
    }
    
    public BizException(RpEnum rpEnum) {
        super(null, null, true, false);
        this.code = rpEnum.getCode();
        this.message = rpEnum.getMsg();
    }

    public static BizException newBizException(String exceptionMsg){
		return new BizException(exceptionMsg);
	}

	public static BizException newBizException(RpEnum rpEnum, String exceptionMsg, Object ... args){
		return new BizException(rpEnum, exceptionMsg, args);
	}

	/**
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
}
