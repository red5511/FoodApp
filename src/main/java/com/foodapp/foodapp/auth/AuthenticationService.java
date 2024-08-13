package com.foodapp.foodapp.auth;

import com.foodapp.foodapp.advice.BusinessException;
import com.foodapp.foodapp.auth.jwtToken.JwtToken;
import com.foodapp.foodapp.auth.jwtToken.JwtTokenRepository;
import com.foodapp.foodapp.auth.jwtToken.TokenType;
import com.foodapp.foodapp.auth.passwordResetToken.PasswordResetTokenService;
import com.foodapp.foodapp.auth.request.AuthenticationRequest;
import com.foodapp.foodapp.auth.request.RegisterRequest;
import com.foodapp.foodapp.auth.response.AuthenticationResponse;
import com.foodapp.foodapp.security.JwtService;
import com.foodapp.foodapp.user.Role;
import com.foodapp.foodapp.user.User;
import com.foodapp.foodapp.user.UserDetailsServiceImpl;
import com.foodapp.foodapp.user.UserRepository;
import com.foodapp.foodapp.user.email.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class AuthenticationService {
    private static final String EMAIL_REGEX = "^(.+)@(\\S+)$";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailSender emailService;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final JwtTokenRepository jwtTokenRepository;

    @Transactional
    public AuthenticationResponse register(final RegisterRequest request) throws BusinessException {
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
        return AuthenticationResponse.builder()
                .token("i dont generate it")
                .build();
    }

    @Transactional
    @SneakyThrows
    public AuthenticationResponse authenticate(final AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user =
                userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new SecurityException("Wrong email or password"));
        if (!user.getEnabled()) {
            throw new BusinessException("Account isn`t activated, pleas check your email");
        }
        var jwtToken = jwtService.generateToken(user);
        saveJwtToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @SneakyThrows
    public void initPasswordChange(final String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException("Bad credentials"));
        var passwordResetToken = passwordResetTokenService.createToken(user);
        emailService.sendPasswordResetEmail(email, passwordResetToken.getToken());
    }

    @Transactional
    public void confirmPasswordChange(final String resetToken, final String newPassword) {
        var passwordResetToken = passwordResetTokenService.processAndGetToken(resetToken);
        var user = passwordResetToken.getUser();
        userDetailsService.updatePassword(user, passwordEncoder.encode(newPassword));
        revokeAllUserTokens(user);
    }

    private void revokeAllUserTokens(final User user) {
        var validUserTokens = jwtTokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        jwtTokenRepository.saveAll(validUserTokens);
    }

    private void saveJwtToken(final User user, final String jwtToken) {
        var token = JwtToken.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        jwtTokenRepository.save(token);
    }

    private void validEmail(final String email) throws BusinessException {
        var isValid = Pattern.compile(EMAIL_REGEX)
                .matcher(email)
                .matches();
        if (!isValid) {
            throw new BusinessException("Bad structure of email");
        }
    }
}
