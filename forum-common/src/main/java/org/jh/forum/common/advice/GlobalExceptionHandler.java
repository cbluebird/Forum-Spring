package org.jh.forum.common.advice;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.jh.forum.common.api.ErrorCode;
import org.jh.forum.common.api.Result;
import org.jh.forum.common.exception.BizException;
import org.jh.forum.common.exception.SystemException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@ResponseBody
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getAllErrors();
        String msg = errors.get(0).getDefaultMessage();
        log.info("code: {}, msg: {}", ErrorCode.COMMON_PARAM_ERROR.getCode(), msg);
        return Result.fail(ErrorCode.COMMON_PARAM_ERROR, msg);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> constraintViolationExceptionHandler(ConstraintViolationException e) {
        log.info("code: {}, msg: {}", ErrorCode.COMMON_PARAM_ERROR.getCode(), e.getMessage());
        return Result.fail(ErrorCode.COMMON_PARAM_ERROR, e.getMessage());
    }

    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> bizExceptionHandler(BizException e) {
        log.info("code: {}, msg: {}", e.getErrorCode().getCode(), e.getMessage());
        return Result.fail(e.getErrorCode());
    }

    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> systemExceptionHandler(SystemException e) {
        log.error("code: {}, msg: {}, stackTrace: {}", e.getErrorCode().getCode(), e.getMessage(), e.getStackTrace());
        return Result.fail(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> exceptionHandler(Exception e) {
        System.out.println(e.getClass());
        log.error("code: {}, msg: {}, stackTrace: {}", ErrorCode.SYSTEM_ERROR.getCode(), e.getMessage(), e.getStackTrace());
        return Result.fail(ErrorCode.SYSTEM_ERROR);
    }
}
