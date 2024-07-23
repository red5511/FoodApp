package com.foodapp.foodapp.auth;

import com.foodapp.foodapp.auth.passwordResetToken.PasswordResetTokenService;
import com.foodapp.foodapp.security.JwtService;
import com.foodapp.foodapp.user.Role;
import com.foodapp.foodapp.user.User;
import com.foodapp.foodapp.user.UserDetailsServiceImpl;
import com.foodapp.foodapp.user.UserRepository;
import com.foodapp.foodapp.user.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class AuthenticationService {
    private static final String EMAIL_REGEX = "^[\\\\w!#$%&'*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\\\.[a-zA-Z0-9-]+)*\\\\.[a-zA-Z]{2,}$";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailSender emailService;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordResetTokenService passwordResetTokenService;


    public AuthenticationResponse register(final RegisterRequest request) {
        validEmail(request.getEmail());
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .createdOn(LocalDateTime.now())
                .build();
        var activationToken = userDetailsService.registerUser(user);
        emailService.sendUserActivationEmail(request.getEmail(), activationToken);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(final AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user =
                userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new SecurityException("Wrong email or password"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public void initPasswordChange(final String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new SecurityException("Wrong email or password"));
        var passwordResetToken = passwordResetTokenService.createToken(user);
        emailService.sendPasswordResetEmail(email, passwordResetToken.getToken());
    }

    @Transactional
    public void confirmPasswordChange(final String resetToken, final String newPassword) {
        var passwordResetToken = passwordResetTokenService.processAndGetToken(resetToken);
        var user = passwordResetToken.getUser();
        userDetailsService.updatePassword(user, passwordEncoder.encode(newPassword));
    }

    private void validEmail(final String email) {
        var isValid = Pattern.compile(EMAIL_REGEX)
                .matcher(email)
                .matches();
//        if (!isValid) { todo lepszy regex czy cos
//            throw new IllegalStateException("Bad structure of email");
//        }
    }
}
