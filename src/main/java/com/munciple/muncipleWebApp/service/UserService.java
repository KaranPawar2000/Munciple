package com.munciple.muncipleWebApp.service;

import com.munciple.muncipleWebApp.dto.Response;
import com.munciple.muncipleWebApp.entity.User;
import com.munciple.muncipleWebApp.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

    public Response registerUser(User user) {
        Optional<User> existingUser = userRepository.findByWhatsappId(user.getWhatsappId());

        Response response = new Response(); // Creating a separate Response object

        if (existingUser.isPresent()) {
            response.setStatus("error");
            response.setMessage("User with WhatsApp ID " + user.getWhatsappId() + " already exists.");
            return response;
        }

        userRepository.save(user);
        response.setStatus("success");
        response.setMessage("User registered successfully.");
        return response;
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
