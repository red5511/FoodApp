package com.foodapp.foodapp.auth;

import com.foodapp.foodapp.advice.BusinessException;
import com.foodapp.foodapp.auth.activationToken.ActivationTokenConfirmationService;
import com.foodapp.foodapp.auth.jwtToken.JwtToken;
import com.foodapp.foodapp.auth.jwtToken.JwtTokenRepository;
import com.foodapp.foodapp.auth.jwtToken.TokenType;
import com.foodapp.foodapp.auth.passwordResetToken.PasswordResetTokenService;
import com.foodapp.foodapp.auth.request.AuthenticationRequest;
import com.foodapp.foodapp.auth.request.RegisterRequest;
import com.foodapp.foodapp.auth.response.AuthenticationResponse;
import com.foodapp.foodapp.security.ContextProvider;
import com.foodapp.foodapp.security.JwtService;
import com.foodapp.foodapp.user.Role;
import com.foodapp.foodapp.user.User;
import com.foodapp.foodapp.user.UserDetailsServiceImpl;
import com.foodapp.foodapp.user.UserRepository;
import com.foodapp.foodapp.user.email.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public class AuthenticationService {
    private static final String EMAIL_REGEX = "^[^@]+@[^@]+\\.[^@]+$";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailSender emailService;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final JwtTokenRepository jwtTokenRepository;
    private final ActivationTokenConfirmationService activationTokenConfirmationService;
    private final ContextProvider contextProvider;

    @Transactional
    public AuthenticationResponse register(final RegisterRequest request) throws BusinessException {
        validEmail(request.getEmail());
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .phoneNumber(request.getPhoneNumber())
                .build();
        var activationToken = userDetailsService.registerUser(user);
        emailService.sendUserActivationEmail(request.getEmail(), request.getFirstName(), activationToken);
        return AuthenticationResponse.builder()
                .token("OK")
                .build();
    }

    @Transactional
    @SneakyThrows
    public AuthenticationResponse authenticate(final AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user =
                userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new SecurityException("Wrong email or password"));
        if (!user.getEnabled()) {
            throw new BusinessException("Konto nie zostało aktywowane, sprawdź maila");
        }
        if (user.getLocked()) {
            throw new BusinessException("Konto zostało zablokowane");
        }
        var jwtToken = jwtService.generateToken(user);
        saveJwtToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @SneakyThrows
    public void initPasswordChange(final String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException("Nieprawidłowe dane logowania"));
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
        if (validUserTokens.isEmpty()) {
            return;
        }
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
            throw new BusinessException("Błędny email");
        }
    }

    public void resendActivationEmail(final String email) throws BusinessException {
        validEmail(email);
        var userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()){
            var user = userOptional.get();
            if (BooleanUtils.isFalse(user.getEnabled())){
                var activationToken = activationTokenConfirmationService.initTokenConfirmation(user);
                emailService.sendUserActivationEmail(email, user.getEmail(), activationToken);
            }
        }
    }

    @Transactional
    public void blockUser(final Long userId) {
        contextProvider.validateSuperAdminRights();
        userRepository.blockUserById(userId);
        jwtTokenRepository.updateRevokedByUserId(true, userId);

    }

    @Transactional
    public void unblockUser(final Long userId) {
        contextProvider.validateSuperAdminRights();
        userRepository.unblockUserById(userId);
    }
}
