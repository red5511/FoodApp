package com.foodapp.foodapp.auth.activationToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivationTokenConfirmationRepository extends JpaRepository<ActivationTokenConfirmation, Long> {
    Optional<ActivationTokenConfirmation> findByActivationToken(String token);
}
