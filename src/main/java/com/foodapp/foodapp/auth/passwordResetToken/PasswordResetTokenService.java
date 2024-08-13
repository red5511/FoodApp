package com.foodapp.foodapp.auth.passwordResetToken;

import com.foodapp.foodapp.user.User;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public PasswordResetToken createToken(final User user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .createdOn(LocalDateTime.now())
                .build();

        return passwordResetTokenRepository.save(passwordResetToken);
    }

    public PasswordResetToken processAndGetToken(final String token) {
        var passwordResetTokenOptional = passwordResetTokenRepository.findByToken(token);
        var passwordResetToken = passwordResetTokenOptional.orElseThrow(() -> new SecurityException("Wrong password reset token"));
        verifyToken(passwordResetToken);
        passwordResetToken.setChangedAt(LocalDateTime.now());
        passwordResetTokenRepository.save(passwordResetToken);
        return passwordResetToken;
    }

    private void verifyToken(final PasswordResetToken passwordResetToken) {
        if (passwordResetToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }
        if (passwordResetToken.getChangedAt() != null) {
            throw new IllegalStateException("Token already used!");
        }
    }
}
