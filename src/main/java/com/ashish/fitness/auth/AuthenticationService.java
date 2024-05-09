package com.ashish.fitness.auth;

import jakarta.mail.MessagingException;

public interface AuthenticationService {
    void register(RegistrationRequest request) throws MessagingException;
}
