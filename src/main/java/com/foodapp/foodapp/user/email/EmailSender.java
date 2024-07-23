package com.foodapp.foodapp.user.email;

public interface EmailSender {
    void sendUserActivationEmail(final String to, final String token);

    void sendPasswordResetEmail(final String to, final String token);
}
