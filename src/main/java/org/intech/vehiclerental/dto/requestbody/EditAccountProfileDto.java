package org.intech.vehiclerental.dto.requestbody;

public record EditAccountProfileDto(
        String firstName,
        String lastName,
        String name,
        String email,
        String phoneNumber,
        String address,
        String city,
        String state,
        String country,
        Long zipCode
) {
}
