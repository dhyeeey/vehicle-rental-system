package org.intech.vehiclerental.dto.requestbody;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateAccountPayloadBody(

        @NotBlank(message = "FirstName cannot be empty")
        @Size(min = 2, max = 50, message = "FirstName must be between 2 and 50 characters")
        String firstName,

        @NotBlank(message = "LastName cannot be empty")
        @Size(min = 2, max = 50, message = "LastName must be between 2 and 50 characters")
        String lastName,

        @Email(message = "Email must be valid")
        @NotBlank(message = "Email cannot be empty")
        String email,

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 5, message = "Password must be at least 5 characters long")
        String password,

        @NotBlank(message = "Phone number cannot be empty")
        @Pattern(regexp = "\\+?[0-9]{10,15}", message = "Phone number must be valid")
        String phoneNumber,

        @NotBlank(message = "License number cannot be empty")
        @Pattern(regexp = "[A-Z0-9]{6,15}", message = "License number must be alphanumeric and 6-15 chars")
        String licenseNumber,

        @NotBlank(message = "Confirm password cannot be empty")
        String confirmPassword
) {}

