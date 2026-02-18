package org.intech.vehiclerental.advice;

import org.intech.vehiclerental.dto.errorresponse.ErrorResponse;
import org.intech.vehiclerental.exceptions.PasswordMismatchException;
import org.intech.vehiclerental.exceptions.WrongLoginEmailCredentialException;
import org.intech.vehiclerental.exceptions.WrongLoginPasswordCredentialException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ){

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleEmptyBody(HttpMessageNotReadableException ex) {
//        return ResponseEntity.badRequest().body("Body cannot be empty");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex) {

        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT);
        error.setMessage("Validation failed");

        String message = ex.getMostSpecificCause().getMessage();

        if (message != null) {
            if (message.contains("email")) {
                error.getErrors().put("email", "Email already exists");
            } else if (message.contains("phone_number")) {
                error.getErrors().put("phoneNumber", "Phone number already exists");
            } else if (message.contains("license_number")) {
                error.getErrors().put("licenseNumber", "License number already exists");
            } else {
                error.setMessage("Duplicate value");
            }
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(WrongLoginEmailCredentialException.class)
    public ResponseEntity<?> invalidLoginEmail(WrongLoginEmailCredentialException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(ex, HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(WrongLoginPasswordCredentialException.class)
    public ResponseEntity<?> invalidLoginPassword(WrongLoginPasswordCredentialException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(ex, HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<?> handleWrongPassword(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(ex, HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<?> passwordAndConfirmPasswordMismatch(PasswordMismatchException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(ex, HttpStatus.UNAUTHORIZED));
    }

//    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
//    public ResponseEntity<?> invalidPayloadFormat(HttpMediaTypeNotSupportedException ex){
//        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
//                .body(new ErrorResponse(ex, HttpStatus.UNSUPPORTED_MEDIA_TYPE));
//    }

}
