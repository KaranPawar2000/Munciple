package com.munciple.muncipleWebApp.service;

import com.munciple.muncipleWebApp.dto.Response;
import com.munciple.muncipleWebApp.entity.User;
import com.munciple.muncipleWebApp.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

    public Response registerUser(User user) {
        Response response = new Response();

        // Check if user exists by whatsappId
        Optional<User> existingUserByWhatsapp = userRepository.findByWhatsappId(user.getWhatsappId());
        if (existingUserByWhatsapp.isPresent()) {
            response.setStatus("error");
            response.setMessage("User with WhatsApp ID " + user.getWhatsappId() + " already exists.");
            return response;
        }

        try {
            // Set creation timestamp
            user.setCreatedAt(LocalDateTime.now());

            // Save the user
            User savedUser = userRepository.save(user);

            response.setStatus("success");
            response.setMessage("User registered successfully");
            response.setUserId(savedUser.getUserId());
            response.setUserName(savedUser.getName());
            response.setWardNumber(savedUser.getWardNumber());
            return response;
        } catch (Exception e) {
            log.error("Error while registering user: ", e);
            response.setStatus("error");
            response.setMessage("Error while registering user: " + e.getMessage());
            return response;
        }
    }

    public Response getUserByWhatsappId(String whatsappId) {
        Optional<User> user = userRepository.findByWhatsappId(whatsappId);
        Response response = new Response();
        if (user.isPresent()) {
            response.setStatus("success");
            response.setMessage("User found.");
            response.setUserName(user.get().getName());
            response.setUserId(user.get().getUserId());
            return response;
        } else {
            response.setStatus("error");
            response.setMessage("User with WhatsApp ID " + whatsappId + " not found.");
            return response;
        }
    }
}
