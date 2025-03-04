package com.foodapp.foodapp.forDevelopment;

import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.administration.company.sql.Content;
import com.foodapp.foodapp.administration.company.sql.OpenHours;
import com.foodapp.foodapp.common.Address;
import com.foodapp.foodapp.common.CommonUtils;
import com.foodapp.foodapp.order.sql.CustomOrderIdGenerator;
import com.foodapp.foodapp.order.sql.Order;
import com.foodapp.foodapp.order.sql.OrderRepository;
import com.foodapp.foodapp.order.OrderStatus;
import com.foodapp.foodapp.order.OrderType;
import com.foodapp.foodapp.orderProduct.OrderProduct;
import com.foodapp.foodapp.orderProduct.OrderProductContent;
import com.foodapp.foodapp.orderProduct.OrderProductRepository;
import com.foodapp.foodapp.product.Product;
import com.foodapp.foodapp.product.ProductRepository;
import com.foodapp.foodapp.productCategory.ProductCategory;
import com.foodapp.foodapp.productCategory.ProductCategoryRepository;
import com.foodapp.foodapp.productProperties.ProductProperties;
import com.foodapp.foodapp.productProperties.ProductPropertiesDto;
import com.foodapp.foodapp.productProperties.ProductPropertiesMapper;
import com.foodapp.foodapp.productProperties.ProductPropertiesRepository;
import com.foodapp.foodapp.productProperties.productProperty.ProductProperty;
import com.foodapp.foodapp.productProperties.productProperty.ProductPropertyRepository;
import com.foodapp.foodapp.user.Role;
import com.foodapp.foodapp.user.User;
import com.foodapp.foodapp.user.UserRepository;
import com.foodapp.foodapp.user.permission.Permission;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

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
    Random rand;
    private final CustomOrderIdGenerator customOrderIdGenerator;

    public List<Company> createCompanies() {
        var names = List.of("", "#2", "#3", "abd_", "_1234567890", "bbbbb", "GIGA_KEBAB");
        List<Company> companies = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            companies.add(createFakeCompany(names.get(i), i + 1L));
        }
        return companies;
    }

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
        var companies = createCompanies();
        var users = createFakeUsers();
        var admin = createFakeAdmin();
        setUsersAndCompaniesAndSave(companies, users);
        var productCategories = createCategories(companies);
        var productPropertyList = createProductPropertyList();
        var productPropertiesList = createProductPropertiesList(companies, productPropertyList);
        productPropertyList = productPropertyRepository.saveAll(productPropertyList);
        setProductProperty(productPropertyList, productPropertiesList);
        var products = createFakeProducts(companies, productCategories);
        setProductProperties(products, productPropertiesList);
        productPropertiesList = productPropertiesRepository.saveAll(productPropertiesList);

        var orders = createFakeOrders(companies, products, productPropertiesList);

        companyRepository.saveAll(companies);
        userRepository.save(admin);
        productCategoryRepository.saveAll(productCategories);
        productRepository.saveAll(products);
        orderRepository.saveAll(orders);
    }

    private void setProductProperty(List<ProductProperty> productPropertyList, List<ProductProperties> productPropertiesList) {
        for (int i = 0; i < 2; i++) {
            productPropertyList.get(i).setProductProperties(productPropertiesList.get(0));
        }
        for (int i = 2; i < productPropertyList.size(); i++) {
            productPropertyList.get(i).setProductProperties(productPropertiesList.get(1));
        }
    }

    private void setProductProperties(List<Product> products, List<ProductProperties> productPropertiesList) {
        products.forEach(product -> product.setProductPropertiesList(productPropertiesList));
        productPropertiesList.forEach(productProperties -> productProperties.setProducts(products));
    }

    private void setUsersAndCompaniesAndSave(List<Company> companies, List<User> users) {
        users = userRepository.saveAll(users);

        List<User> finalUsers = users;
        companies.forEach(company -> {
            // Using the helper method ensures both sides are updated
            company.setUsers(new HashSet<>(finalUsers));
        });
        users.forEach(user -> {
            // Using the helper method ensures both sides are updated
            user.setCompanies(new HashSet<>(companies));
        });
        companyRepository.saveAll(companies);

    }

    private List<Order> createFakeOrders(List<Company> companies, List<Product> products, List<ProductProperties> propertiesList) {
        var orders = new ArrayList<Order>();

        for (int i = 0; i < companies.size(); i++) {
            int finalI = i;
            var companyProducts =
                    products.stream().filter(el -> Objects.equals(el.getCompany().getId(), companies.get(finalI).getId())).toList();
            var productListProducts =
                    propertiesList.stream().filter(el -> Objects.equals(el.getCompany().getId(), companies.get(finalI).getId()))
                            .toList();
            var productProperties =
                    productListProducts.size() == 0 ? null : productListProducts.get(rand.nextInt(productListProducts.size()));
            if (productProperties != null) {
                if (productProperties.isRequired()) {
                    productProperties.getProductPropertyList().get(0);
                } else {
                    if (productProperties.getProductPropertyList().size() > 1) {
                        var subList = productProperties.getProductPropertyList()
                                .subList(0, productProperties.getProductPropertyList().size() - 1);
                        productProperties.setProductPropertyList(subList);
                    }
                }
            }
            var orderProducts =
                    createFakeOrderProduct(companyProducts, productProperties);
            var order = createFakeOrder(orderProducts);
//            Long nextDisplayableId = customOrderIdGenerator.generate(order); company musi miec id
//            order.setDisplayableId(nextDisplayableId);
            order.setCompany(companies.get(i));
            if (!CollectionUtils.isEmpty(order.getOrderProducts())) {
                orders.add(order);
            }
        }

        return orders;
    }

    private List<Product> createFakeProducts(List<Company> companies, List<ProductCategory> productCategories) {
        List<Product> productForEveryCategory = new ArrayList<>();
        for (int i = 0; i < productCategories.size(); i++) {
            var prod = createFakeProduct("Fake#" + i);
            prod.setProductCategory(productCategories.get(i));
            prod.setCompany(companies.get(0));
            productForEveryCategory.add(prod);
        }

        var prodList2 =
                List.of("Duży kebab", "Średni kebab", "Mały kebab", "Duży falafel bardzo bardzo dlugaaa nazwa esktra dluga dlyga",
                                "Średni falafel", "Mały falafel", "Duży specjał", "Mały specjał")
                        .stream().map(name -> {
                            var product = createFakeProduct(name);
                            product.setCompany(companies.get(0));
                            product.setProductCategory(productCategories.get(0)); // Assigning first category
                            return product;
                        }).toList();
        productForEveryCategory.addAll(prodList2);
        return productForEveryCategory;
    }

    private List<ProductProperties> createProductPropertiesList(
            List<Company> companies,
            List<ProductProperty> productPropertyList) {
        return List.of(
                createFakeProductProperties("Sosy", true, companies.get(0), productPropertyList.subList(0, 2)),
                createFakeProductProperties("Dodatki", false, companies.get(0),
                        productPropertyList.subList(2, productPropertyList.size())
                ));
    }

    private List<ProductProperty> createProductPropertyList() {
        return List.of(
                createFakeProductProperty("Sos czosnkowy", BigDecimal.ZERO),
                createFakeProductProperty("Sos pomidorowy", BigDecimal.ZERO),
                createFakeProductProperty("Jalapino", new BigDecimal("1.00")),
                createFakeProductProperty("Dodatkowe mieso", new BigDecimal("9.99")),
                createFakeProductProperty("Podwójny ser", new BigDecimal("4.99")),
                createFakeProductProperty("Buraki", new BigDecimal("19.99"))
        );
    }

    private List<ProductCategory> createCategories(final List<Company> companies) {
        var categoryStrings = List.of("Kebab ciasto",
                "Kebab bułka",
                "Napoje",
                "Desery",
                "Desery2",
                "Desery3",
                "Desery4",
                "Desery5"
        );
        return categoryStrings.stream().map(el -> createFakeProductCategory(el, companies.get(0))).toList();
    }

    private ProductProperties createFakeProductProperties(final String name, final boolean required, final Company company,
                                                          final List<ProductProperty> productPropertyList) {
        return ProductProperties.builder()
                .required(required)
                .name(name)
                .company(company)
                .productPropertyList(productPropertyList)
                .build();
    }

    private ProductProperty createFakeProductProperty(final String name, final BigDecimal price) {
        return ProductProperty.builder()
                .name(name)
                .price(price)
                .build();
    }

    private ProductCategory createFakeProductCategory(final String name, final Company company) {
        return ProductCategory.builder()
                .name(name)
                .company(company)
                .build();
    }

    private List<OrderProduct> createFakeOrderProduct(final List<Product> products, final ProductProperties productProperties) {
        List<ProductPropertiesDto> productPropertiesList =
                productProperties == null ? List.of() : List.of(ProductPropertiesMapper.toProductPropertiesDto(productProperties));
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            orderProducts.add(OrderProduct.builder()
                    .quantity(i + 1)
                    .price(products.get(i).getPrice().multiply(BigDecimal.valueOf(i + 1)))
                    .product(products.get(i))
                    .content(OrderProductContent.builder()
                            .productPropertiesList(productPropertiesList)
                            .build())
                    .build());
        }
        return orderProducts;
    }

    private Order createFakeOrder(final List<OrderProduct> orderProducts) {
        var foodPrice = orderProducts.stream()
                .map(OrderProduct::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        var deliveryPrice = BigDecimal.valueOf(5);
        Order order = Order.builder()
                .deliveryCode(UUID.randomUUID().toString())
                .customerName("Iwona Kowalska")
                .orderType(OrderType.PYSZNE_PL)
                .foodPrice(foodPrice)
                .deliveryPrice(deliveryPrice)
                .totalPrice(foodPrice.add(deliveryPrice))
                .delivery(false)
                .deliveryAddress(Address.builder()
                        .country("Polska")
                        .postalCode("43-999")
                        .city("Warszawa")
                        .street("Odry")
                        .streetNumber("22")
                        .build())
                .description("Poproszę osobno frytki i colę bez lodu.")
                .status(OrderStatus.EXECUTED)
                .executionTime(LocalDateTime.now())
                .orderProducts(orderProducts)
                .orderType(OrderType.OWN)
                .build();

        // Ensure each OrderProduct references the parent order
        orderProducts.forEach(orderProduct -> orderProduct.setOrder(order));

        return order;
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
                .description(new Random().nextInt(5) != 1 ? "Bardzo dobre jedzenie" :
                        "Bardzo bardzo dlugiiiii opissssssssssssss extraaaa xddd")
                .name(name)
                .price(new BigDecimal("10"))
                .deliveryPrice(new BigDecimal("2"))
                .takeawayPrice(new BigDecimal("1"))
                .build();
    }

    private Product createFakeProduct2(final String name) {
        return Product.builder()
                .imgUrl("images/kebab2.png")
                .description("Pyszny kebab")
                .name(name)
                .price(new BigDecimal("9.99"))
                .build();
    }

    private List<User> createFakeUsers() {
        var user = createFakeUser();
        return List.of(user);
    }

    private User createFakeUser() {
        Set<Permission> permissions;
        permissions = new HashSet<>();
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

    private Company createFakeCompany(final String name, final Long i) {
        return Company.builder()
                .id(i)
                .name("Firma Testowa" + name)
                .content(createContent())
                .webSocketTopicName(UUID.randomUUID().toString())
                .webSocketTopicName("Topic" + i)
                .address(Address.builder()
                        .street(new Random().nextInt(2) % 2 == 1 ? "Generała Piłsudskiego" : "Piastów")
                        .city(new Random().nextInt(2) % 2 == 1 ? "Warszawa" : "Kraków")
                        .streetNumber("555 / 102b")
                        .postalCode("34-999")
                        .build())
                .defaultProductImgUrl(CommonUtils.createDefaultProductImgUrl(i.toString()))
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
