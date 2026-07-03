package org.gan.assistant.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //1.处理校验失败的异常(@Valid 不通过是触发)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String ,String> errors=new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName=((FieldError) error).getField();
            String errorMessage=error.getDefaultMessage();
            errors.put(fieldName,errorMessage);
        });
        Map<String,Object> response=new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message","参数校验失败");
        response.put("errors",errors);
        return ResponseEntity.badRequest().body(response);
    }
    //2.处理业务运行时异常(用户名名已存在,行程不存在)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String,String>> handleRuntimeException(RuntimeException ex){
        Map<String,String> response=new HashMap<>();
        response.put("message",ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
