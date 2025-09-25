package com.munciple.muncipleWebApp.service;

import com.munciple.muncipleWebApp.dto.LoginRequest;
import com.munciple.muncipleWebApp.dto.LoginResponse;
import com.munciple.muncipleWebApp.repo.AuthenticationRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationRepository authenticationRepository;

    public AuthenticationService(AuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        return authenticationRepository.findByUsername(loginRequest.getUsername())
                .map(user -> {
                    if (user.getPassword().equals(loginRequest.getPassword())) {
                        return new LoginResponse("Login successful", true);
                    } else {
                        return new LoginResponse("Invalid password", false);
                    }
                })
                .orElse(new LoginResponse("User not found", false));
    }
}
