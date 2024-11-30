package com.foodapp.foodapp.config;

import com.foodapp.foodapp.administration.AdministrationService;
import com.foodapp.foodapp.administration.cache.UsersConnectedToWebCacheWrapper;
import com.foodapp.foodapp.administration.company.CompanyRepository;
import com.foodapp.foodapp.administration.company.CompanyService;
import com.foodapp.foodapp.administration.userAdministration.UserAdministrationService;
import com.foodapp.foodapp.auth.AuthenticationService;
import com.foodapp.foodapp.auth.activationToken.ActivationTokenConfirmationRepository;
import com.foodapp.foodapp.auth.activationToken.ActivationTokenConfirmationService;
import com.foodapp.foodapp.auth.jwtToken.JwtTokenRepository;
import com.foodapp.foodapp.auth.passwordResetToken.PasswordResetTokenRepository;
import com.foodapp.foodapp.auth.passwordResetToken.PasswordResetTokenService;
import com.foodapp.foodapp.dashboard.DashboardService;
import com.foodapp.foodapp.forDevelopment.scheduler.SchedulerForTestingService;
import com.foodapp.foodapp.order.OrderRepository;
import com.foodapp.foodapp.order.OrderService;
import com.foodapp.foodapp.order.OrderValidator;
import com.foodapp.foodapp.product.ProductRepository;
import com.foodapp.foodapp.product.ProductService;
import com.foodapp.foodapp.security.ContextProvider;
import com.foodapp.foodapp.security.JwtAuthenticationFilter;
import com.foodapp.foodapp.security.JwtService;
import com.foodapp.foodapp.statistic.StatisticsService;
import com.foodapp.foodapp.user.UserDetailsServiceImpl;
import com.foodapp.foodapp.user.UserRepository;
import com.foodapp.foodapp.user.email.EmailSender;
import com.foodapp.foodapp.user.email.EmailService;
import com.foodapp.foodapp.websocket.WebSocketService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

@Configuration
public class MainConfiguration {
    @Value("${app.time-to-accept-order}")
    private Long timeToAcceptOrder;

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        config.setAllowedHeaders(Arrays.asList(
                ORIGIN,
                CONTENT_TYPE,
                ACCEPT,
                AUTHORIZATION
        ));
        config.setAllowedMethods(Arrays.asList(
                GET.name(),
                POST.name(),
                DELETE.name(),
                PUT.name(),
                PATCH.name()
        ));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);

    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(final JwtService jwtService,
                                                           final UserDetailsService userDetailsService,
                                                           final HandlerExceptionResolver handlerExceptionResolver) {
        return new JwtAuthenticationFilter(jwtService, userDetailsService, handlerExceptionResolver);
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
                                                       final UserDetailsServiceImpl userDetailsService,
                                                       final PasswordResetTokenService passwordResetTokenService,
                                                       final JwtTokenRepository jwtTokenRepository) {
        return new AuthenticationService(userRepository,
                passwordEncoder,
                jwtService,
                authenticationManager,
                emailSender,
                userDetailsService,
                passwordResetTokenService,
                jwtTokenRepository
        );
    }

    @Bean
    public JwtService jwtService(final JwtTokenRepository jwtTokenRepository) {
        return new JwtService(jwtTokenRepository);
    }

    @Bean
    public EmailSender emailService(final JavaMailSender javaMailSender) {
        return new EmailService(javaMailSender);
    }

    @Bean
    public ActivationTokenConfirmationService tokenConfirmationService(
            final ActivationTokenConfirmationRepository tokenConfirmationRepository) {
        return new ActivationTokenConfirmationService(tokenConfirmationRepository);
    }

    @Bean
    public PasswordResetTokenService passwordResetTokenService(final PasswordResetTokenRepository passwordResetTokenRepository) {
        return new PasswordResetTokenService(passwordResetTokenRepository);
    }

    @Bean
    public CompanyService companyService(final CompanyRepository companyRepository,
                                         final UserDetailsServiceImpl userDetailsService,
                                         final UserRepository userRepository) {
        return new CompanyService(companyRepository, userDetailsService, userRepository);
    }

    @Bean
    public DashboardService dashboardService(final CompanyService companyService,
                                             final ContextProvider contextValidator,
                                             final OrderService orderService) {
        return new DashboardService(companyService, contextValidator, orderService);
    }

    @Bean
    public ProductService productService(final ProductRepository productRepository,
                                         final CompanyRepository companyRepository,
                                         final ContextProvider contextProvider) {
        return new ProductService(productRepository, companyRepository, contextProvider);
    }

    @Bean
    public ContextProvider contextValidator() {
        return new ContextProvider();
    }

    @Bean
    public ApplicationAuditAware applicationAuditAware(final ContextProvider contextProvider) {
        return new ApplicationAuditAware(contextProvider);
    }

    @Bean
    public OrderService orderService(final OrderRepository orderRepository,
                                     final CompanyRepository companyRepository,
                                     final OrderValidator orderValidator,
                                     final ContextProvider contextProvider) {
        return new OrderService(orderRepository,
                companyRepository,
                contextProvider,
                orderValidator
        );
    }

    @Bean
    public OrderValidator orderValidator() {
        return new OrderValidator(timeToAcceptOrder);
    }

    @Bean
    public AdministrationService administrationService(CompanyRepository companyRepository,
                                                       final UserRepository userRepository) {
        return new AdministrationService(companyRepository,
                userRepository
        );
    }

    @Bean
    public UserAdministrationService userAdministrationService(final UserDetailsServiceImpl userDetailsService) {
        return new UserAdministrationService(userDetailsService);
    }

    @Bean
    public StatisticsService statisticsService(final ProductRepository productRepository,
                                               final OrderRepository orderRepository) {
        return new StatisticsService(productRepository, orderRepository);
    }

    @Bean
    public WebSocketService webSocketService(final SimpMessagingTemplate messagingTemplate,
                                             final UsersConnectedToWebCacheWrapper cacheWrapper,
                                             final ContextProvider contextProvider,
                                             final SchedulerForTestingService schedulerForTestingService) {
        return new WebSocketService(messagingTemplate,
                cacheWrapper,
                contextProvider,
                schedulerForTestingService);
    }
}
