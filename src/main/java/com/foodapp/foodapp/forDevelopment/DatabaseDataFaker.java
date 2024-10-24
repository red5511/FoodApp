package com.foodapp.foodapp.forDevelopment;

import com.foodapp.foodapp.administration.company.Company;
import com.foodapp.foodapp.administration.company.CompanyRepository;
import com.foodapp.foodapp.administration.company.Content;
import com.foodapp.foodapp.administration.company.OpenHours;
import com.foodapp.foodapp.order.Order;
import com.foodapp.foodapp.order.OrderRepository;
import com.foodapp.foodapp.order.OrderStatus;
import com.foodapp.foodapp.order.OrderType;
import com.foodapp.foodapp.product.Product;
import com.foodapp.foodapp.product.ProductRepository;
import com.foodapp.foodapp.user.Role;
import com.foodapp.foodapp.user.User;
import com.foodapp.foodapp.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;

@AllArgsConstructor
public class DatabaseDataFaker {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    @TechnicalContextDev
    @Transactional
    public void initFakeData() {
        var companyOptional = companyRepository.findById(1L);
        var userOptional = userRepository.findById(1L);
        var productOptional = productRepository.findById(1L);
        var orderOptional = orderRepository.findById(1L);
        if (companyOptional.isPresent() && userOptional.isPresent() && productOptional.isPresent() && orderOptional.isPresent()) {
            return;
        }
        var company = createFakeCompany();
        var user = createFakeUser();
        var product = createFakeProduct();
        var order = createFakeOrder();

        company = companyRepository.save(company);
        user.setCompanies(new HashSet<>(Arrays.asList(company)));
        user = userRepository.save(user);
        company.setCompanyUsers(new HashSet<>(Arrays.asList(user)));
        companyRepository.save(company);

        product.setCompany(company);
        product = productRepository.save(product);
        productRepository.save(product);

        order.setCompany(company);
        order.setProducts(new HashSet<>(Arrays.asList(product)));
        order = orderRepository.save(order);
        orderRepository.save(order);
    }

    private Order createFakeOrder() {
        return Order.builder()
                .customerName("Iwona Kowalska")
                .orderType(OrderType.PYSZNE_PL)
                .price(new BigDecimal("20.20"))
                .deliveryAddress("Piłsudskiego 44")
                .description("Poprosze osobno frytki i cole bez lodu. W razie problemow ze znalezeniem numry zostawic na portierni")
                .status(OrderStatus.EXECUTED)
                .deliveryTime(LocalDateTime.now())
                .build();
    }

    private Product createFakeProduct() {
        return Product.builder()
                .imgUrl("https://afterfit-catering.pl/wp-content/uploads/2024/01/kebab-glowne.jpg")
                .description("Pyszny kebab")
                .name("Duży kebab")
                .price(new BigDecimal("20.20"))
                .build();
    }

    private User createFakeUser() {
        return User.builder()
                .email("macmac")
                .firstName("Eustachy")
                .lastName("Motyka")
                .password(passwordEncoder.encode("password123"))
                .role(Role.USER)
                .enabled(true)
                .build();
    }

    private Company createFakeCompany() {
        return Company.builder()
                .name("Firma Testowa")
                .address("Powstańców 34a, Warszawa 33-999")
                .content(createContent())
                .build();
    }

    private Content createContent() {
        return Content.builder()
                .openHours(OpenHours.builder()
                        .mondayStart(LocalTime.of(9, 0))
                        .mondayEnd(LocalTime.of(17, 0))
                        .tuesdayStart(LocalTime.of(9, 0))
                        .tuesdayEnd(LocalTime.of(17, 0))
                        .wednesdayStart(LocalTime.of(9, 0))
                        .wednesdayEnd(LocalTime.of(17, 0))
                        .thursdayStart(LocalTime.of(9, 0))
                        .thursdayEnd(LocalTime.of(17, 0))
                        .fridayStart(LocalTime.of(9, 0))
                        .fridayEnd(LocalTime.of(17, 0))
                        .saturdayStart(LocalTime.of(10, 0))
                        .saturdayEnd(LocalTime.of(14, 0))
                        .sundayStart(LocalTime.of(0, 0))  // Zamknięte w niedzielę
                        .sundayEnd(LocalTime.of(0, 0))    // Zamknięte w niedzielę
                        .build())
                .build();
    }
}
