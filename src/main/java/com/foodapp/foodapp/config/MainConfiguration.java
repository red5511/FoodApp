package com.foodapp.foodapp.config;

import com.foodapp.foodapp.auth.AuthenticationService;
import com.foodapp.foodapp.auth.token.ActivationTokenConfirmationRepository;
import com.foodapp.foodapp.auth.token.ActivationTokenConfirmationService;
import com.foodapp.foodapp.security.JwtAuthenticationFilter;
import com.foodapp.foodapp.security.JwtService;
import com.foodapp.foodapp.user.UserDetailsServiceImpl;
import com.foodapp.foodapp.user.UserRepository;
import com.foodapp.foodapp.user.email.EmailSender;
import com.foodapp.foodapp.user.email.EmailService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class MainConfiguration {
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(final JwtService jwtService,
                                                           final UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(jwtService, userDetailsService);
    }

    @Bean
    public UserDetailsService userDetailsService(final UserRepository userRepository,
                                                 final ActivationTokenConfirmationService tokenConfirmationService) {
        return new UserDetailsServiceImpl(userRepository, tokenConfirmationService);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(final UserDetailsService userDetailsService,
                                                         final PasswordEncoder passwordEncoder) {
        var daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationService authenticationService(final UserRepository userRepository,
                                                       final PasswordEncoder passwordEncoder,
                                                       final JwtService jwtService,
                                                       final AuthenticationManager authenticationManager,
                                                       final EmailSender emailSender,
                                                       final UserDetailsServiceImpl userDetailsService) {
        return new AuthenticationService(userRepository,
                passwordEncoder,
                jwtService,
                authenticationManager,
                emailSender,
                userDetailsService);
    }

    @Bean
    public JwtService jwtService(ApplicationContext context) {
        return new JwtService();
    }

    @Bean
    public EmailSender emailService(final JavaMailSender javaMailSender) {
        return new EmailService(javaMailSender);
    }

    @Bean
    public ActivationTokenConfirmationService tokenConfirmationService(final ActivationTokenConfirmationRepository tokenConfirmationRepository){
        return new ActivationTokenConfirmationService(tokenConfirmationRepository);
    }

}
