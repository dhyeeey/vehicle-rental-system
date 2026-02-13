package org.intech.vehiclerental.dto.requestbody;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginPayloadBody(
        @Email(message = "Email must be valid")
        @NotBlank(message = "Email cannot be empty")
        String email,

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 5, message = "Password must be at least 5 characters long")
        String password
) {
}
