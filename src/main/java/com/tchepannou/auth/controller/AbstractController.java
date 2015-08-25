package com.tchepannou.auth.controller;

import com.tchepannou.core.client.v1.ErrorResponse;
import com.tchepannou.core.http.Http;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class AbstractController {
    //-- Exception Handler
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ErrorResponse notFound(final HttpServletRequest request) {
        return createErrorResponse(HttpStatus.NOT_FOUND.value(), "not_found", request);
    }

//    @ResponseStatus(value= HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(AccessTokenException.class)
//    public ErrorResponse authenticationFailed(final AccessTokenException exception, final HttpServletRequest request) {
//        LOG.error("Authentication error", exception);
//        return createErrorResponse(HttpStatus.UNAUTHORIZED.value(),  exception.getMessage(), request);
//    }
//
//    @ResponseStatus(value= HttpStatus.FORBIDDEN)
//    @ExceptionHandler(AuthorizationException.class)
//    public ErrorResponse authorizationFailed(Exception exception, final HttpServletRequest request) {
//        LOG.error("Authorization failed", exception);
//        return createErrorResponse(HttpStatus.FORBIDDEN.value(), exception.getMessage(), request);
//    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse validationFailed(MethodArgumentNotValidException ex, final HttpServletRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        return createErrorResponse(HttpStatus.BAD_REQUEST.value(), fieldErrors.get(0).getDefaultMessage(), request);
    }

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse failure(final Exception exception, final HttpServletRequest request) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), request);
    }

    protected ErrorResponse createErrorResponse(int code, String text, HttpServletRequest request){
        return new ErrorResponse()
                .withCode(code)
                .withText(text)
                .withAccessTokenId(request.getHeader(Http.HEADER_ACCESS_TOKEN))
                .withTransactionId(request.getHeader(Http.HEADER_TRANSACTION_ID));
    }
}
