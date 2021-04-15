package com.sobey.exception;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义异常
 * @author lgc
 * @date 2020年1月17日 下午2:55:05
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 拦截未知的运行时异常
	 */
	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public Object handleException(HttpServletRequest request, RuntimeException e) {

		String path = request.getRequestURL().toString();
		String code = null;
		String message = null;
		String detailMsg = null;
		Map<String, Object> map = null;
		if (e instanceof AppException) {

			AppException app = (AppException) e;
			code = app.getCode();
			message = app.getMessage();
			detailMsg = app.getDetailMsg();
			/**
			 * 下面这种情况并不会记录堆栈信息，所以这里统一添加
			 * AppException a = new AppException(ExceptionType.PASSWORD); 
			 * 
			 */
			if (StringUtils.isEmpty(detailMsg)) {
				detailMsg = ExceptionKit.toString(app);
			}
			map = warpMsg(path, code, message, detailMsg);
			/**
			 * 这里detailMsg记录的才是目标异常
			 * 
			 */
			log.error("拦截到异常:"+detailMsg);
		} else {

			code = ExceptionType.SYS_RUNTIME.getCode();
			message = ExceptionType.SYS_RUNTIME.getMessage();
			detailMsg = ExceptionKit.toString(e);
			map = warpMsg(path, code, message, detailMsg);
			log.error("拦截到其他异常:"+detailMsg);
		}

		return map;
	}

	private Map<String, Object> warpMsg(String path, String code, String message,
			String detailMsg) {

		Map<String, Object> map = new HashMap<>();
		map.put("path", path);
		map.put("code", code);
		map.put("message", message);
		map.put("detailMsg", detailMsg);

		return map;
	}
	
	/**
	 * 对参数进行校验的异常处理，嵌套的校验也能捕获
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Map<String, Object> resolveMethodArgumentNotValidException(MethodArgumentNotValidException ex){
		String code = ExceptionType.SYS_RUNTIME.getCode();
		List<ObjectError>  list = ex.getBindingResult().getAllErrors();
		StringBuffer sb = new StringBuffer();
        if(CollectionUtils.isNotEmpty(list)) {
        	for (ObjectError error : list) {
				sb.append(error.getDefaultMessage()).append(",");
			}
        }
		Map<String, Object> map = warpMsg(null, code, sb.toString(), null);
        return map;
    }

}
