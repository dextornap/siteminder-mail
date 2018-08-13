package com.siteminder.mail.validation;

import com.siteminder.mail.model.ApiResponse;
import com.siteminder.mail.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    Messages messages;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String search = "EmailRequest[\"";
        String invalidField = ex.getMessage().substring(ex.getMessage().lastIndexOf(search) + search.length());
        invalidField = invalidField.substring(0, invalidField.lastIndexOf("\"]"));
        String error = invalidField + messages.get("invalid.type");
        ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, messages.get("invalid.request.format"), Optional.of(Arrays.asList(error)));
        return this.handleExceptionInternal(ex, apiResponse, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = Stream.concat(ex.getBindingResult().getFieldErrors().stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage()),
                ex.getBindingResult().getGlobalErrors().stream()
                        .map(error -> error.getObjectName() + ": " + error.getDefaultMessage()))
                .sorted()
                .collect(Collectors.toList());
        ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, messages.get("invalid.request.fields"), Optional.of(errors));
        return handleExceptionInternal(ex, apiResponse, headers, HttpStatus.BAD_REQUEST, request);
    }
}

