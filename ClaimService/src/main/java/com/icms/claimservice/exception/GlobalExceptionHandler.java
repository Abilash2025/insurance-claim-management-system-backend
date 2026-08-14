package com.icms.claimservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException exception,
            HttpServletRequest request
    ){

        ErrorResponse errorResponse = createErrorResponse(exception,request);
        errorResponse.setError("Claim Not Found");
        errorResponse.setStatus(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(errorResponse,errorResponse.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationError(
            MethodArgumentNotValidException exception,
            HttpServletRequest request,
            BindingResult bindingResult
    ){
        Map<String,String> errors = new HashMap<>();

        List<FieldError> fieldErrors =  bindingResult.getFieldErrors();

        for(FieldError fieldError : fieldErrors){
            errors.put(
                    fieldError.getField(),
                    fieldError.getDefaultMessage()
            );
        }

        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.UNPROCESSABLE_CONTENT,
                "Validation Error",
                request.getPathInfo(),
                exception.getMessage(),
                errors

        );

        return new ResponseEntity<>(validationErrorResponse,validationErrorResponse.getStatus());
    }

    @ExceptionHandler(DuplicateClaimException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateClaimException(
            DuplicateClaimException exception,
            HttpServletRequest request
    ){

        ErrorResponse errorResponse = createErrorResponse(exception,request);
        errorResponse.setError("Duplicate claim");
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorResponse,errorResponse.getStatus());
    }

    @ExceptionHandler(InvalidClaimException.class)
    public ResponseEntity<ErrorResponse> handleInvalidClaimException(
            InvalidClaimException exception,
            HttpServletRequest request
    ){

        ErrorResponse errorResponse = createErrorResponse(exception,request);
        errorResponse.setError("Invalid claim");
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorResponse,errorResponse.getStatus());
    }

    @ExceptionHandler(PolicyInactiveException.class)
    public ResponseEntity<ErrorResponse> handlePolicyInactiveException(
            PolicyInactiveException exception,
            HttpServletRequest request
    ){

        ErrorResponse errorResponse = createErrorResponse(exception,request);
        errorResponse.setError("Inactive Policy");
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorResponse,errorResponse.getStatus());
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
