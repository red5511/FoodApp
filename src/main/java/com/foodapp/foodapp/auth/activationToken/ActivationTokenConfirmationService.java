package com.foodapp.foodapp.auth.activationToken;

import com.foodapp.foodapp.user.User;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class ActivationTokenConfirmationService {
    private final ActivationTokenConfirmationRepository tokenConfirmationRepository;

    public void saveConfirmationToken(final ActivationTokenConfirmation token) {
        tokenConfirmationRepository.save(token);
    }

    public String initTokenConfirmation(final User user) {
        String token = UUID.randomUUID().toString();
        ActivationTokenConfirmation tokenConfirmation = ActivationTokenConfirmation.builder()
                .createdOn(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .activationToken(token)
                .user(user)
                .build();
        saveConfirmationToken(tokenConfirmation);
        return token;
    }

    @Transactional
    public void confirmToken(final String token) {
        var tokenConfirmation = tokenConfirmationRepository.findByActivationToken(token)
                .orElseThrow(() -> new IllegalStateException("Wrong token"));
        verifyToken(tokenConfirmation);
        tokenConfirmation.getUser().setEnabled(true);
        tokenConfirmation.setConfirmedAt(LocalDateTime.now());
    }

    private void verifyToken(final ActivationTokenConfirmation tokenConfirmation) {
        if (tokenConfirmation.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }
        if (tokenConfirmation.getConfirmedAt() != null) {
            throw new IllegalStateException("Email already confirmed");
        }

    }

}
