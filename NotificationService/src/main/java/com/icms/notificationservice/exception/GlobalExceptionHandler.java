package com.icms.notificationservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException exception,
            HttpServletRequest request
    ){

        ErrorResponse errorResponse = createErrorResponse(exception,request);
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        errorResponse.setError("Resource Not Found.");

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }


    public ErrorResponse createErrorResponse(Exception exception,
                                             HttpServletRequest request){

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setPath(request.getPathInfo());

        return errorResponse;
    }
}
