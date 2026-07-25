package com.fitness.userservice.services;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.model.User;
import com.fitness.userservice.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserResponse register( RegisterRequest request) {
        User user=new User();
        user.setFirstName( request.getFirstName() );
        user.setLastName( request.getLastName() );
        user.setEmail( request.getEmail() );
        user.setPassword( request.getPassword() );

        User savedUser=userRepository.save( user );
        UserResponse userResponse=new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setFirstName(savedUser.getFirstName());
        userResponse.setLastName(savedUser.getLastName());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setPassword(savedUser.getPassword());
        userResponse.setCreatedAt( savedUser.getCreatedAt() );
        userResponse.setUpdatedAt( savedUser.getUpdatedAt() );
        return userResponse;
    }

    public UserResponse getUserProfile(String id) {
        User user=userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        UserResponse userResponse=new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPassword(user.getPassword());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());

        return userResponse;

    }

    public Boolean existsById(String id) {
        log.info("Calling user validation API for userId:{}",id);
        return userRepository.existsById(id);
    }
}
