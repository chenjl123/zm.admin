package com.zm.shiro.exception;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 全局异常
 */
@ControllerAdvice
public class CtrlExceptionHandler {
    private Logger log = Logger.getLogger(CtrlExceptionHandler.class);
    //拦截未授权页面
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnauthorizedException.class)
    public String handleException(UnauthorizedException e) {
        log.debug(e.getMessage());
        return "error/403";
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizationException.class)
    public String handleException2(AuthorizationException e) {
        log.debug(e.getMessage());
        return "error/403";
    }
}
