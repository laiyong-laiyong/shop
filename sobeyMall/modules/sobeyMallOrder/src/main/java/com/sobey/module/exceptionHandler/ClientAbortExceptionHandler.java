package com.sobey.module.exceptionHandler;

import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * @author WCY
 * @createTime 2020/5/29 16:23
 * 处理当时在联调时由于华为云环境配置不当导致一直出现ClientAbortException异常，现在已不会出现
 */
@ControllerAdvice
@Deprecated
public class ClientAbortExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ClientAbortExceptionHandler.class);

    @ExceptionHandler(ClientAbortException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object handleException(HttpServletRequest request, ClientAbortException e){
        String path = request.getRequestURL().toString();
        log.error("RequestUri:{},ClientAbortException:{}",path,e.getMessage());
        return e.getMessage();
    }

}
