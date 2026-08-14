package com.icms.customerservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {

    private LocalDateTime timestamp;
    private HttpStatus status;
    private String error;
    private String path;
    private Map<String,String> errors;
}
