package com.foodapp.foodapp.auth;

import com.foodapp.foodapp.administration.cache.LoginAttemptCacheWrapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginAttemptService {
    private static final int MAX_ATTEMPTS = 10;
    private final LoginAttemptCacheWrapper loginAttemptCacheWrapper;

    public void loginFailed(String email) {
        loginAttemptCacheWrapper.put(email, loginAttemptCacheWrapper.getOrDefault(email, 0) + 1);
    }

    public void loginSucceeded(String email) {
        loginAttemptCacheWrapper.remove(email);
    }

    public boolean isBlocked(String email) {
        return loginAttemptCacheWrapper.getOrDefault(email, 0) >= MAX_ATTEMPTS;
    }
}
