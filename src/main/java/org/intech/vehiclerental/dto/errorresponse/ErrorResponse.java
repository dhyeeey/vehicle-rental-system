package org.intech.vehiclerental.dto.errorresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.intech.vehiclerental.exceptions.WrongLoginEmailCredentialException;
import org.intech.vehiclerental.exceptions.WrongLoginPasswordCredentialException;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {
    Instant timestamp;
    Integer status;
    Map<String, Object> errors;
    String message;

    public ErrorResponse(){
        timestamp = Instant.now();
        status = HttpStatus.UNAUTHORIZED.value();
        errors = new HashMap<>();
        message = null;
    }

    public ErrorResponse(HttpStatus httpStatus){
        timestamp = Instant.now();
        status = httpStatus.value();
        errors = new HashMap<>();
        message = null;
    }

    public ErrorResponse(DataIntegrityViolationException ex, HttpStatus httpStatus){
        this(httpStatus);
        message = ex.getLocalizedMessage();
    }

    public ErrorResponse(MethodArgumentNotValidException ex, HttpStatus httpStatus){
        this(httpStatus);
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        message = "Validation failed";
    }

    public ErrorResponse(HttpMessageNotReadableException ex, HttpStatus httpStatus) {
        this(httpStatus);
        message = "Request body is empty";
    }

    public ErrorResponse(HttpMediaTypeNotSupportedException ex, HttpStatus httpStatus) {
        this(httpStatus);
        message = "Unsupported Media Type";
    }

    public ErrorResponse(PSQLException ex, HttpStatus httpStatus) {
        this(httpStatus);

        message = ex.getMessage();
    }

    public ErrorResponse(WrongLoginEmailCredentialException ex, HttpStatus httpStatus) {
        this(httpStatus);
        message = ex.getMessage();
        errors.put("email","Invalid Email");
    }

    public ErrorResponse(WrongLoginPasswordCredentialException ex, HttpStatus httpStatus) {
        this(httpStatus);
        message = ex.getMessage();
        errors.put("password","Invalid Password");
    }
}
