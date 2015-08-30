package com.tchepannou.auth.controller;

import com.tchepannou.core.client.v1.ErrorResponse;
import com.tchepannou.core.http.Http;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class AbstractController {
    //-- Exception Handler
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse validationFailed(final MethodArgumentNotValidException ex, final HttpServletRequest request) {
        getLogger().error("[] = Validation error", request.getRequestURI(), ex);

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        return createErrorResponse(HttpStatus.BAD_REQUEST.value(), fieldErrors.get(0).getDefaultMessage(), request);
    }

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse failure(final Exception exception, final HttpServletRequest request) {
        getLogger().error("{} - Unexpected error.", request.getRequestURI(), exception);

        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), request);
    }

    protected ErrorResponse createErrorResponse(int code, String text, HttpServletRequest request){
        return new ErrorResponse()
                .withCode(code)
                .withText(text)
                .withAccessTokenId(request.getHeader(Http.HEADER_ACCESS_TOKEN))
                .withTransactionId(request.getHeader(Http.HEADER_TRANSACTION_ID))
        ;
    }

    protected Logger getLogger (){
        return LoggerFactory.getLogger(getClass());
    }
}
