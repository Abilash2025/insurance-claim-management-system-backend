package com.icms.policyservice.exception;

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
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        errorResponse.setError("Resource Not Found");

        return new ResponseEntity<>(errorResponse,errorResponse.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationError(
            MethodArgumentNotValidException exception,
            HttpServletRequest request,
            BindingResult bindingResult
    ){
        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse();

        validationErrorResponse.setTimestamp(LocalDateTime.now());
        validationErrorResponse.setStatus(HttpStatus.UNPROCESSABLE_CONTENT);
        validationErrorResponse.setError("Validation Error");
        validationErrorResponse.setPath(request.getPathInfo());
        validationErrorResponse.setMessage(exception.getMessage());

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        Map<String,String> errors = new HashMap<>();

        for(FieldError fieldError : fieldErrors){
            errors.put(
                    fieldError.getField(),
                    fieldError.getDefaultMessage()
            );
        }

        validationErrorResponse.setErrors(errors);

        return new ResponseEntity<>(validationErrorResponse,validationErrorResponse.getStatus());
    }

    @ExceptionHandler(CustomerInactiveException.class)
    public ResponseEntity<ErrorResponse> handleCustomerInactiveException(
            CustomerInactiveException exception,
            HttpServletRequest request
    ){

        ErrorResponse errorResponse = createErrorResponse(exception,request);
        errorResponse.setStatus(HttpStatus.UNPROCESSABLE_CONTENT);
        errorResponse.setError("Customer Inactive");

        return new ResponseEntity<>(errorResponse,errorResponse.getStatus());
    }

    @ExceptionHandler(PolicyActiveException.class)
    public ResponseEntity<ErrorResponse> handlePolicyActiveException(
            PolicyActiveException exception,
            HttpServletRequest request
    ){

        ErrorResponse errorResponse = createErrorResponse(exception,request);
        errorResponse.setStatus(HttpStatus.UNPROCESSABLE_CONTENT);
        errorResponse.setError("Policy is active.");

        return new ResponseEntity<>(errorResponse,errorResponse.getStatus());
    }

    @ExceptionHandler(PolicyInactiveException.class)
    public ResponseEntity<ErrorResponse> handlePolicyInactiveException(
            PolicyInactiveException exception,
            HttpServletRequest request
    ){

        ErrorResponse errorResponse = createErrorResponse(exception,request);
        errorResponse.setStatus(HttpStatus.UNPROCESSABLE_CONTENT);
        errorResponse.setError("Policy is inactive.");

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
