package com.wl.wlp2ploansystem.infrastructures.common.errors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统错误处理ControllerAdvice
 */
@ControllerAdvice
public class ExceptionTranslator {

    private final Logger log = LoggerFactory.getLogger(ExceptionTranslator.class);

    /**
     * ConcurrencyFailureException
     */
    @ExceptionHandler(ConcurrencyFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorVM processConcurrencyError(ConcurrencyFailureException ex) {

        log.error(ex.getMessage(),ex);

        return new ErrorVM(ErrorConstants.ERR_CONCURRENCY_FAILURE);
    }
    /**
     * 禁止访问
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorVM processAccessDeniedException(AccessDeniedException ex) {

        log.error(ex.getMessage(),ex);

        return new ErrorVM(ErrorConstants.ERR_ACCESS_DENIED, ex.getMessage());
    }

    /**
     * HttpRequestMethodNotSupportedException
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorVM processMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {

        log.error(ex.getMessage(),ex);

        return new ErrorVM(ErrorConstants.ERR_METHOD_NOT_SUPPORTED, ex.getMessage());
    }
    /**
     * 乐观锁并发冲突
     */
    @ExceptionHandler(OptimisticConcurrencyException.class)
    public ResponseEntity<ErrorVM> processOptimisticConcurrencyException(OptimisticConcurrencyException ex) {

        log.error(ex.getMessage(),ex);


    	ErrorVM vm= new ErrorVM(null, ex.getMessage(),null,null);
        Integer statuCode = ex.getStatusCode();
        if(statuCode == null){
            statuCode = HttpStatus.BAD_REQUEST.value();
        }
    	return ResponseEntity.status(statuCode).body(vm);
    }
    /**
     * 字段必输、长度等验证错误
     */
    @ExceptionHandler(value=MethodArgumentNotValidException.class)
    public Object MethodArgumentNotValidHandler(HttpServletRequest request,
                                                MethodArgumentNotValidException ex) throws Exception
    {

        log.error(ex.getMessage(),ex);

        BodyBuilder builder= ResponseEntity.status(8888);
        ErrorVM errorVM;
        //按需重新封装需要返回的错误信息
        List<ArgumentInvalidResult> invalidArguments = new ArrayList<>();
        //解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            ArgumentInvalidResult invalidArgument = new ArgumentInvalidResult();
            invalidArgument.setDefaultMessage(error.getDefaultMessage());
            invalidArgument.setField(error.getField());
            invalidArgument.setRejectedValue(error.getRejectedValue());
            invalidArguments.add(invalidArgument);
        }
        String errorDetails =  StringUtils.join(invalidArguments.stream().map(p->p.toString()).collect(Collectors.toList()),',');

        errorVM = new ErrorVM(ErrorConstants.ERR_VALIDATION, errorDetails);
        return builder.body(errorVM);
    }
    /**
     * 业务异常
     */
    @ExceptionHandler(UserFriendlyException.class)
    public ResponseEntity<ErrorVM> processUserFriendlyException(UserFriendlyException ex) {

        log.error(ex.getMessage(),ex);

        if(ex.getException() !=null){

            log.error("UserFriendlyException inner exception " +ex.getException());
        }

        ErrorVM vm= new ErrorVM(ex.getCode(), ex.getMessage(),ex.getDetails(),ex.getPayload());
        Integer statuCode = ex.getStatusCode();
        if(statuCode == null){
            statuCode = HttpStatus.BAD_REQUEST.value();
        }
        return ResponseEntity.status(statuCode).body(vm);
    }

    /**
     * 通用异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorVM> processException(Exception ex) {

        log.error(ex.getMessage(),ex);

        Throwable cause = ex.getCause();
        while (cause !=null){
            if(cause instanceof  UserFriendlyException){
                return processUserFriendlyException((UserFriendlyException)cause);
            }

            cause = cause.getCause();
        }
        BodyBuilder builder;
        ErrorVM errorVM;
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            builder = ResponseEntity.status(responseStatus.value());
            errorVM = new ErrorVM("error." + responseStatus.value().value(), responseStatus.reason());
        } else {
            builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
            errorVM = new ErrorVM(ErrorConstants.ERR_INTERNAL_SERVER_ERROR, "服务器错误，请重试或联系系统管理员");
        }
        return builder.body(errorVM);
    }


}
