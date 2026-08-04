package com.fitness.userservice.repositories;

import com.fitness.userservice.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    Boolean existsByKeycloakId(String keycloakId);

    Boolean existsByEmail(@NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") String email);

    User findByEmail(@NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") String email);
}
