package org.squirrel.framework.response;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.squirrel.framework.SquirrelComponent;

/**
 * @description 全局异常处理
 * @author weicong
 * @date 2019年6月9日
 * @version 1.0
 */
@SquirrelComponent
@CrossOrigin
@RestControllerAdvice
public final class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(value = { MissingServletRequestParameterException.class
			, MethodArgumentNotValidException.class
			, BindException.class
			, Exception.class
			, BizException.class })
	public Rp<?> processException(Exception e, HttpServletRequest request, HttpServletResponse response) {
		// 参数错误
		if (e instanceof MissingServletRequestParameterException) {
			log.error("MissingServletRequestParameterException: ", e);
			return Rp.failed(RpEnum.ERROR_PARAMETER);
		}
		// 统一参数校验错误转换
		if (e instanceof MethodArgumentNotValidException) {
			log.error("MethodArgumentNotValidException: ", e);
			List<ObjectError> errors = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors();
			return Rp.failed(RpEnum.ERROR_PARAMETER, errors.isEmpty() ? RpEnum.ERROR_PARAMETER.getMsg() : errors.get(0).getDefaultMessage());
		}
		if (e instanceof BindException) {
			log.error("BindException: ", e);
			List<ObjectError> errors = ((BindException) e).getBindingResult().getAllErrors();
			return Rp.failed(RpEnum.ERROR_PARAMETER, errors.isEmpty() ? RpEnum.ERROR_PARAMETER.getMsg() : errors.get(0).getDefaultMessage());
		}
		// 业务异常
		if (e instanceof BizException) {
			BizException exception = (BizException) e;
			log.error("BizException code: {}, message: {}", exception.getCode(), exception.getMessage());
			return Rp.failed(exception.getCode());
		}
		log.error("全局异常: ", e);
		return Rp.failed(RpEnum.ERROR_SYSTEM);
	}

}
