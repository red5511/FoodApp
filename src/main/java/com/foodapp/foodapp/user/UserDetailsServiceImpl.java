package com.foodapp.foodapp.user;

import com.foodapp.foodapp.advice.BusinessException;
import com.foodapp.foodapp.auth.activationToken.ActivationTokenConfirmationService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final ActivationTokenConfirmationService tokenConfirmationService;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(final String username) {
        return userRepository.findByEmail(username).orElseThrow(() -> new BusinessException("Bad credentials"));
    }

    @SneakyThrows
    public String registerUser(final User user) {
        boolean userExists = userRepository.findByEmail(user.getUsername()).isPresent();
        if (userExists) {
            throw new BusinessException("User already exists");
        }
        userRepository.save(user);
        return tokenConfirmationService.initTokenConfirmation(user);
    }

    public void updatePassword(final User user, final String newPassword) {
        user.setPassword(newPassword);
        userRepository.save(user);
    }
}
