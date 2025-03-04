package com.foodapp.foodapp.config;

import com.foodapp.foodapp.administration.AdministrationService;
import com.foodapp.foodapp.administration.cache.CacheService;
import com.foodapp.foodapp.administration.cache.CompanyWithActiveReceivingUsersCacheWrapper;
import com.foodapp.foodapp.administration.cache.UsersConnectedToWebSocketCacheWrapper;
import com.foodapp.foodapp.administration.company.CompanyAdministrationService;
import com.foodapp.foodapp.administration.company.CompanyService;
import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.administration.userAdministration.UserAdministrationService;
import com.foodapp.foodapp.auth.AuthenticationService;
import com.foodapp.foodapp.auth.activationToken.ActivationTokenConfirmationRepository;
import com.foodapp.foodapp.auth.activationToken.ActivationTokenConfirmationService;
import com.foodapp.foodapp.auth.jwtToken.JwtTokenRepository;
import com.foodapp.foodapp.auth.passwordResetToken.PasswordResetTokenRepository;
import com.foodapp.foodapp.auth.passwordResetToken.PasswordResetTokenService;
import com.foodapp.foodapp.dashboard.DashboardService;
import com.foodapp.foodapp.delivery.DeliveryOptionRepository;
import com.foodapp.foodapp.delivery.DeliveryOptionService;
import com.foodapp.foodapp.forDevelopment.scheduler.SchedulerForTestingService;
import com.foodapp.foodapp.order.OrderMapper;
import com.foodapp.foodapp.order.OrderService;
import com.foodapp.foodapp.order.OrderValidator;
import com.foodapp.foodapp.order.sql.CustomOrderIdGenerator;
import com.foodapp.foodapp.order.sql.OrderRepository;
import com.foodapp.foodapp.orderProduct.OrderProductRepository;
import com.foodapp.foodapp.product.ProductMapper;
import com.foodapp.foodapp.product.ProductRepository;
import com.foodapp.foodapp.product.ProductService;
import com.foodapp.foodapp.product.ProductValidator;
import com.foodapp.foodapp.productCategory.ProductCategoryRepository;
import com.foodapp.foodapp.productCategory.ProductCategoryService;
import com.foodapp.foodapp.productProperties.ProductPropertiesController;
import com.foodapp.foodapp.productProperties.ProductPropertiesRepository;
import com.foodapp.foodapp.productProperties.ProductPropertiesService;
import com.foodapp.foodapp.rabbitMQ.RabbitMQSender;
import com.foodapp.foodapp.security.ContextProvider;
import com.foodapp.foodapp.security.JwtAuthenticationFilter;
import com.foodapp.foodapp.security.JwtService;
import com.foodapp.foodapp.statistic.StatisticsService;
import com.foodapp.foodapp.user.UserDetailsServiceImpl;
import com.foodapp.foodapp.user.UserRepository;
import com.foodapp.foodapp.user.email.EmailSender;
import com.foodapp.foodapp.user.email.EmailService;
import com.foodapp.foodapp.websocket.WebSocketEventHandler;
import com.foodapp.foodapp.websocket.WebSocketEventSender;
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
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost",
                "http://localhost:4200",   // Angular Web
                "capacitor://localhost",   // Capacitor WebView
                "http://10.0.2.2",          // Android Emulator accessing host machine
                "192.168.1.5",
                "192.168.56.1",
                "192.168.56.2",
                "192.168.1.21",
                "192.168.56.1"
        ));
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
                                                       final JwtTokenRepository jwtTokenRepository,
                                                       final ActivationTokenConfirmationService activationTokenConfirmationService,
                                                       final ContextProvider contextProvider) {
        return new AuthenticationService(userRepository,
                passwordEncoder,
                jwtService,
                authenticationManager,
                emailSender,
                userDetailsService,
                passwordResetTokenService,
                jwtTokenRepository,
                activationTokenConfirmationService,
                contextProvider
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
                                         final ContextProvider contextProvider,
                                         final ProductValidator productValidator,
                                         final ProductMapper productMapper) {
        return new ProductService(productRepository,
                contextProvider,
                productValidator,
                productMapper);
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
                                     final ContextProvider contextProvider,
                                     final WebSocketEventSender webSocketEventSender,
                                     final OrderMapper orderMapper,
                                     final OrderProductRepository orderProductRepository) {
        return new OrderService(orderRepository,
                companyRepository,
                contextProvider,
                orderValidator,
                webSocketEventSender,
                orderMapper,
                orderProductRepository
        );
    }

    @Bean
    public OrderValidator orderValidator(final ContextProvider contextProvider,
                                         final OrderRepository orderRepository) {
        return new OrderValidator(contextProvider, orderRepository);
    }

    @Bean
    public AdministrationService administrationService(CompanyRepository companyRepository,
                                                       final UserRepository userRepository) {
        return new AdministrationService(companyRepository,
                userRepository
        );
    }

    @Bean
    public UserAdministrationService userAdministrationService(final UserDetailsServiceImpl userDetailsService,
                                                               final ContextProvider contextProvider,
                                                               final UserRepository userRepository,
                                                               final CompanyRepository companyRepository) {
        return new UserAdministrationService(userDetailsService, contextProvider, userRepository, companyRepository);
    }

    @Bean
    public StatisticsService statisticsService(final ProductRepository productRepository,
                                               final OrderRepository orderRepository,
                                               final ContextProvider contextProvider) {
        return new StatisticsService(productRepository, orderRepository, contextProvider);
    }

    @Bean
    public WebSocketService webSocketService(final ContextProvider contextProvider,
                                             final SchedulerForTestingService schedulerForTestingService,
                                             final CacheService cacheService,
                                             final WebSocketEventSender webSocketEventSender,
                                             final RabbitMQSender rabbitMQSender) {
        return new WebSocketService(
                contextProvider,
                schedulerForTestingService,
                cacheService,
                webSocketEventSender,
                rabbitMQSender
        );
    }

    @Bean
    public WebSocketEventHandler webSocketEventHandler(final CacheService cacheService,
                                                       final WebSocketEventSender webSocketEventSender,
                                                       final CompanyRepository companyRepository) {
        return new WebSocketEventHandler(cacheService, webSocketEventSender, companyRepository);
    }

    @Bean
    public CacheService cacheService(final UsersConnectedToWebSocketCacheWrapper usersConnectedToWebSocketCacheWrapper,
                                     final CompanyWithActiveReceivingUsersCacheWrapper companyWithActiveReceivingCacheWrapper) {
        return new CacheService(companyWithActiveReceivingCacheWrapper,
                usersConnectedToWebSocketCacheWrapper
        );
    }

    @Bean
    public WebSocketEventSender webSocketEventSender(final SimpMessagingTemplate messagingTemplate) {
        return new WebSocketEventSender(messagingTemplate);
    }


    @Bean
    public CompanyAdministrationService companyAdministrationService(final ContextProvider contextProvider,
                                                                     final CompanyRepository companyRepository,
                                                                     final UserRepository userRepository) {
        return new CompanyAdministrationService(contextProvider,
                companyRepository,
                userRepository
        );
    }

    @Bean
    public ProductCategoryService productCategoryService(final ProductCategoryRepository productCategoryRepository,
                                                         final ContextProvider contextProvider,
                                                         final CompanyRepository companyRepository,
                                                         final ProductService productService) {
        return new ProductCategoryService(productCategoryRepository, contextProvider, companyRepository, productService);
    }

    @Bean
    public ProductPropertiesService productPropertiesService(final ProductPropertiesRepository productPropertiesRepository,
                                                             final ContextProvider contextProvider,
                                                             final CompanyRepository companyRepository) {
        return new ProductPropertiesService(productPropertiesRepository, contextProvider, companyRepository);
    }

    @Bean
    public ProductPropertiesController productPropertiesController(final ProductPropertiesService productPropertiesService) {
        return new ProductPropertiesController(productPropertiesService);
    }

    @Bean
    public ProductMapper productMapper(final ProductCategoryRepository productCategoryRepository,
                                       final CompanyRepository companyRepository,
                                       final ProductPropertiesRepository productPropertiesRepository) {
        return new ProductMapper(productCategoryRepository,
                companyRepository,
                productPropertiesRepository);
    }

    @Bean
    public ProductValidator productValidator(final ContextProvider contextProvider) {
        return new ProductValidator(contextProvider);
    }

    @Bean
    public DeliveryOptionService deliveryOptionService(final DeliveryOptionRepository deliveryOptionRepository,
                                                       final ContextProvider contextProvider,
                                                       final CompanyRepository companyRepository) {
        return new DeliveryOptionService(deliveryOptionRepository,
                contextProvider,
                companyRepository);
    }

//    @Bean
//    public CustomOrderIdGenerator customOrderIdGenerator() {
//        return new CustomOrderIdGenerator();
//    }

    @Bean
    public OrderMapper orderMapper(final CustomOrderIdGenerator customOrderIdGenerator) {
        return new OrderMapper(customOrderIdGenerator);
    }
}
