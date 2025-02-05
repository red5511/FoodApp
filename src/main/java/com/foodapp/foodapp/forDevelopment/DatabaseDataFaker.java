package com.foodapp.foodapp.forDevelopment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.administration.company.sql.Content;
import com.foodapp.foodapp.administration.company.sql.OpenHours;
import com.foodapp.foodapp.common.Address;
import com.foodapp.foodapp.order.Order;
import com.foodapp.foodapp.order.OrderRepository;
import com.foodapp.foodapp.order.OrderStatus;
import com.foodapp.foodapp.order.OrderType;
import com.foodapp.foodapp.orderProduct.OrderProduct;
import com.foodapp.foodapp.orderProduct.OrderProductRepository;
import com.foodapp.foodapp.product.Product;
import com.foodapp.foodapp.product.ProductRepository;
import com.foodapp.foodapp.productCategory.ProductCategory;
import com.foodapp.foodapp.productCategory.ProductCategoryRepository;
import com.foodapp.foodapp.productProperties.ProductProperties;
import com.foodapp.foodapp.productProperties.ProductPropertiesRepository;
import com.foodapp.foodapp.productProperties.productProperty.ProductProperty;
import com.foodapp.foodapp.productProperties.productProperty.ProductPropertyRepository;
import com.foodapp.foodapp.user.Role;
import com.foodapp.foodapp.user.User;
import com.foodapp.foodapp.user.UserRepository;
import com.foodapp.foodapp.user.permission.Permission;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DatabaseDataFaker {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductPropertyRepository productPropertyRepository;
    private final ProductPropertiesRepository productPropertiesRepository;

    @TechnicalContextDev
    @Transactional
    public void initFakeData() {
        var companyOptional = companyRepository.findById(1L);
        var userOptional = userRepository.findById(1L);
        var productOptional = productRepository.findById(1L);
        var orderOptional = orderRepository.findById(1L);
        if(companyOptional.isPresent() && userOptional.isPresent() && productOptional.isPresent() && orderOptional.isPresent()) {
            return;
        }
        var company = createFakeCompany("", "Topic1", true);
        var company2 = createFakeCompany("#2", "Topic2", true);
        var company3 = createFakeCompany("#3", "Topic3", true);
        var company4 = createFakeCompany("abd_", "Topic4", false);
        var company5 = createFakeCompany("_1234567890", "Topic5", true);
        var company6 = createFakeCompany("bbbbb", "Topic6", false);
        var company7 = createFakeCompany("GIGA_KEBAB_", "Topic7", false);
        var user = createFakeUser();
        var admin = createFakeAdmin();

        var categoryStrings = List.of("Kebab ciasto",
                                      "Kebab bułka",
                                      "Napoje",
                                      "Desery",
                                      "Desery2",
                                      "Desery3",
                                      "Desery4",
                                      "Desery5"
        );
        var productCategoryList = categoryStrings.stream().map(this::createFakeProductCategory).toList();

        var productProperty = createFakeProductProperty("Sos czosnkowy", BigDecimal.ZERO);
        var productProperty2 = createFakeProductProperty("Sos pomidorowy", BigDecimal.ZERO);

        var productProperty3 = createFakeProductProperty("Jalapino", new BigDecimal("1.00"));
        var productProperty4 = createFakeProductProperty("Dodatkowe mieso", new BigDecimal("9.99"));
        var productProperty5 = createFakeProductProperty("Podwójny ser", new BigDecimal("4.99"));
        var productProperty6 = createFakeProductProperty("Buraki", new BigDecimal("19.99"));

        var productProperties = createFakeProductProperties("Sosy", true);
        var productProperties2 = createFakeProductProperties("Dodatki", false);

        var productStrings = List.of("Duży kebab", "Średni kebab", "Mały kebab", "Duży falafel bardzo bardzo dlugaaa nazwa esktra dluga dlyga", "Średni falafel", "Mały falafel", "Duży specjał", "Mały specjał");
        List<Product> productForEveryCategory = new ArrayList<>();
        for(int i = 0; i < productCategoryList.size(); i++) {
            productForEveryCategory.add(createFakeProduct("Fake#" + i));
        }
        var productList = productStrings.stream().map(this::createFakeProduct).toList();

        var productForCompany2 = createFakeProduct("Duża pita");
        var product2ForCompany2 = createFakeProduct("Pizza");
        var productForCompany3 = createFakeProduct2("Mała pita");

        var orderProducts = createFakeOrderProduct(productList);
        var orderProductsForCompany2 = createFakeOrderProduct(List.of(productForCompany2, product2ForCompany2));
        var orderProductsForCompany3 = createFakeOrderProduct(List.of(productForCompany3));
        final var order = createFakeOrder(orderProducts);
        final var orderForCompany2 = createFakeOrder(orderProductsForCompany2);
        final var orderForCompany3 = createFakeOrder(orderProductsForCompany3);
        orderProducts.forEach(el -> el.setOrder(order));
        orderProductsForCompany2.forEach(el -> el.setOrder(orderForCompany2));
        orderProductsForCompany3.forEach(el -> el.setOrder(orderForCompany3));

        company = companyRepository.save(company);
        company2 = companyRepository.save(company2);
        company3 = companyRepository.save(company3);
        company4 = companyRepository.save(company4);
        company5 = companyRepository.save(company5);
        company6 = companyRepository.save(company6);
        company7 = companyRepository.save(company7);
        user.setCompanies(new HashSet<>(Arrays.asList(company, company2, company3)));
        user = userRepository.save(user);
        userRepository.save(admin);
        company.setUsers(new HashSet<>(Arrays.asList(user)));
        company2.setUsers(new HashSet<>(Arrays.asList(user)));
        company3.setUsers(new HashSet<>(Arrays.asList(user)));
        companyRepository.saveAll(List.of(company, company2, company3));

        final Company finalCompany = company;
        productCategoryList.forEach(productCategory ->{
            productCategory.setCompany(finalCompany);
            productCategoryRepository.save(productCategory);
        });
        //
        //        productProperty = productPropertyRepository.save(productProperty);
        //        productProperty2 = productPropertyRepository.save(productProperty2);
        //        productProperty3 = productPropertyRepository.save(productProperty3);
        //        productProperty4 = productPropertyRepository.save(productProperty4);
        //        productProperty5 = productPropertyRepository.save(productProperty5);
        //        productProperty6 = productPropertyRepository.save(productProperty6);

        productProperties.setCompany(company);
        productProperties.setProductPropertyList(List.of(productProperty, productProperty2));
        productProperties.setProducts(productList);
        productProperties2.setProductPropertyList(List.of(productProperty3, productProperty4, productProperty5, productProperty6));
        productProperties2.setCompany(company);
        productProperties2.setProducts(productList);


        var productPropertiesList = productPropertiesRepository.saveAll(List.of(productProperties, productProperties2));
        productList.forEach(product -> product.setProductPropertiesList(productPropertiesList));

        productProperty.setProductProperties(productProperties);
        productProperty2.setProductProperties(productProperties);
        productProperty3.setProductProperties(productProperties2);
        productProperty4.setProductProperties(productProperties2);
        productProperty5.setProductProperties(productProperties2);
        productProperty6.setProductProperties(productProperties2);
        //
        //        productPropertyRepository.save(productProperty);
        //        productPropertyRepository.save(productProperty2);
        //        productPropertyRepository.save(productProperty3);
        //        productPropertyRepository.save(productProperty4);
        //        productPropertyRepository.save(productProperty5);
        //        productPropertyRepository.save(productProperty6);

        final Company finalCompany1 = company;
        productList.forEach(product -> {
            product.setProductCategory(productCategoryList.get(0));
            product.setCompany(finalCompany1);
        });
        for (int i = 0; i < productForEveryCategory.size(); i++) {
            productForEveryCategory.get(i).setProductCategory(productCategoryList.get(i));
            productForEveryCategory.get(i).setCompany(finalCompany1);
        }
        productForCompany2.setCompany(company2);
        product2ForCompany2.setCompany(company2);
        productForCompany3.setCompany(company3);
        productRepository.saveAll(List.of(productForCompany2, product2ForCompany2, productForCompany3));
        productRepository.saveAll(productList);
        productRepository.saveAll(productForEveryCategory);

        order.setCompany(company);
        orderForCompany2.setCompany(company2);
        orderForCompany3.setCompany(company3);

        orderRepository.save(order);
        orderRepository.save(orderForCompany2);
        orderRepository.save(orderForCompany3);

        orderProductRepository.saveAll(orderProducts);
        orderProductRepository.saveAll(orderProductsForCompany2);
        orderProductRepository.saveAll(orderProductsForCompany3);
    }

    private ProductProperties createFakeProductProperties(final String name, final boolean required) {
        return ProductProperties.builder()
                                .required(required)
                                .name(name)
                                .build();
    }

    private ProductProperty createFakeProductProperty(final String name, final BigDecimal price) {
        return ProductProperty.builder()
                              .name(name)
                              .price(price)
                              .build();
    }

    private ProductCategory createFakeProductCategory(final String name) {
        return ProductCategory.builder()
                              .name(name)
                              .build();
    }

    private List<OrderProduct> createFakeOrderProduct(final List<Product> products) {
        List<OrderProduct> orderProducts = new ArrayList<>();
        for(int i = 0; i < products.size(); i++) {
            orderProducts.add(OrderProduct.builder()
                                          .quantity(i + 1)
                                          .price(products.get(i).getPrice().multiply(BigDecimal.valueOf(i + 1)))
                                          .product(products.get(i))
                                          .build());
        }
        return orderProducts;
    }

    private Order createFakeOrder(final List<OrderProduct> orderProducts) {
        var price = orderProducts.stream()
                                 .map(OrderProduct::getPrice)
                                 .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Order.builder()
                    .deliveryCode(UUID.randomUUID().toString())
                    .customerName("Iwona Kowalska")
                    .orderType(OrderType.PYSZNE_PL)
                    .price(price)
                    .deliveryAddress("Piłsudskiego 33 Warszawa 22-322")
                    .description("Poprosze osobno frytki i cole bez lodu. W razie problemow ze znalezeniem numry zostawic na portierni")
                    .status(OrderStatus.EXECUTED)
                    .deliveryTime(LocalDateTime.now())
                    .orderProducts(orderProducts)
                    .build();
    }

    private Product createFakeProduct(final String name) {
        List<String> images = new ArrayList<>();
        images.add("images/kebab1.png");
        images.add("images/kebab2.png");
        images.add("images/kebab3.png");
        images.add("images/kebab3.png");
        images.add(null);
        images.add("images/kebab2.png");
        return Product.builder()
                      .imgUrl(images.get(new Random().nextInt(6)))
                      .description(new Random().nextInt(5) != 1 ? "Bardzo dobre jedzenie" : "Bardzo bardzo dlugiiiii opissssssssssssss extraaaa xddd")
                      .name(name)
                      .price(new BigDecimal("20.20"))
                      .build();
    }

    private Product createFakeProduct2(final String name) {
        return Product.builder()
                      .imgUrl("images/kebab2.png")
                      .description("Pyszny kebab")
                      .name(name)
                      .price(new BigDecimal("10.20"))
                      .build();
    }

    private User createFakeUser() {
        Set<Permission> permissions = new HashSet<>();
        permissions.add(Permission.VIEW_ONLINE_ORDERING);
        permissions.add(Permission.VIEW_ORDERS_HISTORY);
        permissions.add(Permission.VIEW_STATISTICS);
        permissions.add(Permission.VIEW_RESTAURANT_ORDERING);
        permissions.add(Permission.VIEW_MENU_PANEL);

        return User.builder()
                   .email("macmac")
                   .firstName("Eustachy")
                   .lastName("Motyka")
                   .password(passwordEncoder.encode("password123"))
                   .role(Role.USER)
                   .enabled(true)
                   .permissions(permissions)
                   .phoneNumber("987654321")
                   .build();
    }

    private User createFakeAdmin() {
        Set<Permission> permissions = new HashSet<>();
        permissions.add(Permission.SUPER_ADMINISTRATOR);

        return User.builder()
                   .email("admin")
                   .firstName("Admin Eustachy")
                   .lastName("Admin Motyka")
                   .password(passwordEncoder.encode("admin"))
                   .role(Role.SUPER_ADMIN)
                   .enabled(true)
                   .permissions(permissions)
                   .phoneNumber("123456789")
                   .build();
    }

    private Company createFakeCompany(final String postfix, final String topic, final boolean isPostfix) {
        var name = isPostfix ? "Firma Testowa" + postfix : postfix + "Firma Testowa";
        return Company.builder()
                      .name(name)
                      .content(createContent())
                      .webSocketTopicName(UUID.randomUUID().toString())
                      .webSocketTopicName(topic)
                      .address(Address.builder()
                                      .street(new Random().nextInt(2) % 2 == 1 ? "Generała Piłsudskiego" : "Piastów")
                                      .city(new Random().nextInt(2) % 2 == 1 ? "Warszawa" : "Kraków")
                                      .streetNumber("555 / 102b")
                                      .postalCode("34-999")
                                      .build())
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
