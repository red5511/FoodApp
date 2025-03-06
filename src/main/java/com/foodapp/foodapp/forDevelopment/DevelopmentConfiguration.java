package com.foodapp.foodapp.forDevelopment;

import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.order.sql.CustomOrderIdGenerator;
import com.foodapp.foodapp.order.sql.OrderRepository;
import com.foodapp.foodapp.orderProduct.OrderProductRepository;
import com.foodapp.foodapp.product.ProductRepository;
import com.foodapp.foodapp.productCategory.ProductCategoryRepository;
import com.foodapp.foodapp.productProperties.ProductPropertiesRepository;
import com.foodapp.foodapp.productProperties.productProperty.ProductPropertyRepository;
import com.foodapp.foodapp.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;

@Configuration
@Profile("TEST")
@EnableAspectJAutoProxy
public class DevelopmentConfiguration {

    @Bean
    public DatabaseDataFakerInterface databaseDataFaker(final CompanyRepository companyRepository,
                                                        final UserRepository userRepository,
                                                        final ProductRepository productRepository,
                                                        final OrderRepository orderRepository,
                                                        final OrderProductRepository orderProductRepository,
                                                        final PasswordEncoder passwordEncoder,
                                                        final ProductCategoryRepository productCategoryRepository,
                                                        final ProductPropertyRepository productPropertyRepository,
                                                        final ProductPropertiesRepository productPropertiesRepository,
                                                        final CustomOrderIdGenerator customOrderIdGenerator) {
        return new DatabaseDataFaker(companyRepository,
                userRepository,
                productRepository,
                orderRepository,
                orderProductRepository,
                passwordEncoder,
                productCategoryRepository,
                productPropertyRepository,
                productPropertiesRepository,
                customOrderIdGenerator,
                new Random()
        );
    }
}
