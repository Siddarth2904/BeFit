package com.fitness.activityservice.services;

import com.fitness.activityservice.config.WebClientConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserValidationService {
    public final WebClient userServiceWebClient;

    public Boolean validateUser(String id) {
        log.info("Calling user validation API for userId:{}",id);
        try{
            return userServiceWebClient.get()
                    .uri("/api/users/{id}/validate",id).retrieve()
                    .bodyToMono(Boolean.class).block();
        }catch(WebClientResponseException e){
            if(e.getStatusCode()== HttpStatus.NOT_FOUND){
                throw new RuntimeException("User not found: "+id);
            }else if(e.getStatusCode()== HttpStatus.BAD_REQUEST){
                throw new RuntimeException("Invalid request: "+id);
            }
        }
        return false;
    }
}
