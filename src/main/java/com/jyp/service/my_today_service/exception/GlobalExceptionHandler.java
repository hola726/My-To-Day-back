package com.jyp.service.my_today_service.exception;

import com.jyp.service.my_today_service.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice()
public class GlobalExceptionHandler {

    ///  validation exception 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        ApiResponse<Map<String, String>> response = new ApiResponse<>(false, "Validation failed", "", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /// 부적절한 인자 값 런타임 exception 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponse<String> response = new ApiResponse<>(false, ex.getMessage(), "", null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    ///  기본 exception 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception ex) {
        ApiResponse<String> response = new ApiResponse<>(false, "Internal server error", "", null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
