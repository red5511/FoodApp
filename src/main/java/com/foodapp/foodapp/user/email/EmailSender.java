package com.foodapp.foodapp.user.email;

public interface EmailSender {
    void sendUserActivationMail(final String to, final String token);
}
