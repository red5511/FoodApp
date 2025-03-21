package com.foodapp.foodapp.forDevelopment.uBuly;

import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.order.sql.CustomOrderIdGenerator;
import com.foodapp.foodapp.order.sql.OrderRepository;
import com.foodapp.foodapp.orderProduct.OrderProductRepository;
import com.foodapp.foodapp.product.ProductRepository;
import com.foodapp.foodapp.productCategory.ProductCategoryRepository;
import com.foodapp.foodapp.productProperties.ProductPropertiesRepository;
import com.foodapp.foodapp.productProperties.productProperty.ProductPropertyRepository;
import com.foodapp.foodapp.user.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;

@ConditionalOnProperty(name = "app.feature-toggle.create-fake-u-buly-stuff", havingValue = "true", matchIfMissing = false)
@Configuration
@EnableAspectJAutoProxy
public class UBulyDevelopmentConfiguration {

    @Bean
    public UBulyDatabaseDataFakerInterface uBulyDatabaseDataFaker(final CompanyRepository companyRepository,
                                                                  final UserRepository userRepository,
                                                                  final ProductRepository productRepository,
                                                                  final OrderRepository orderRepository,
                                                                  final OrderProductRepository orderProductRepository,
                                                                  final PasswordEncoder passwordEncoder,
                                                                  final ProductCategoryRepository productCategoryRepository,
                                                                  final ProductPropertyRepository productPropertyRepository,
                                                                  final ProductPropertiesRepository productPropertiesRepository,
                                                                  final CustomOrderIdGenerator customOrderIdGenerator) {
        return new UBulyDatabaseDataFaker(companyRepository,
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
