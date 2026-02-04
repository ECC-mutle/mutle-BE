package com.mutle.mutle.exception;

import com.mutle.mutle.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e){
        ErrorCode errorCode=e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode.getCode()+": "+errorCode.getMessage()));


    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String errorName = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        try {
            ErrorCode errorCode = ErrorCode.valueOf(errorName);

            return ResponseEntity
                    .status(errorCode.getStatus())
                    .body(ApiResponse.error(errorCode.getCode() + ": " + errorCode.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(400).body(ApiResponse.error("VALIDATION_ERROR: " + errorName));
        }
}
}
