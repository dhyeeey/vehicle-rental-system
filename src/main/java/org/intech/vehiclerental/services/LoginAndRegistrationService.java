package org.intech.vehiclerental.services;

import org.intech.vehiclerental.dto.requestbody.CreateAccountPayloadBody;
import org.intech.vehiclerental.models.User;

public interface LoginAndRegistrationService {

    User registerUser(CreateAccountPayloadBody payload);
}