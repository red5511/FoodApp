package com.foodapp.foodapp.forDevelopment.uBuly;

import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.common.CommonUtils;
import com.foodapp.foodapp.forDevelopment.TechnicalContextDev;
import com.foodapp.foodapp.forDevelopment.common.DatabaseFakerUtils;
import com.foodapp.foodapp.order.sql.CustomOrderIdGenerator;
import com.foodapp.foodapp.order.sql.OrderRepository;
import com.foodapp.foodapp.product.ProductRepository;
import com.foodapp.foodapp.orderProduct.OrderProductRepository;
import com.foodapp.foodapp.productCategory.ProductCategoryRepository;
import com.foodapp.foodapp.productProperties.ProductPropertiesRepository;
import com.foodapp.foodapp.productProperties.productProperty.ProductPropertyRepository;
import com.foodapp.foodapp.user.User;
import com.foodapp.foodapp.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
public class UBulyDatabaseDataFaker implements UBulyDatabaseDataFakerInterface {
    public static List<String> CATEGORY_STRINGS = List.of("Kebab", "Menu dziecięce", "Inne");
    public static Map<String, BigDecimal> PROPERTY_MAP_OPTIONAL = new HashMap<>() {{
        put("Cebulka prażona", BigDecimal.valueOf(1));
        put("Dodatkowy sos", BigDecimal.valueOf(2));
        put("Dodatkowe mięso", BigDecimal.valueOf(6));
        put("Ser zółty", BigDecimal.valueOf(3));
//        put("Jalapeno", BigDecimal.valueOf(3));
//        put("Dodatkowe frytki", BigDecimal.valueOf(2));
    }};

    public static Map<String, BigDecimal> PROPERTY_MAP_REQUIRED_SAUCE = new HashMap<>() {{
        put("Łagodny (czosnkowy)", BigDecimal.valueOf(0));
        put("Średni", BigDecimal.valueOf(0));
        put("Ostry", BigDecimal.valueOf(0));
        put("Ketchup", BigDecimal.valueOf(0));
        put("Bez sosu", BigDecimal.valueOf(0));
    }};

    public static Map<String, BigDecimal> PROPERTY_MAP_REQUIRED_MEAT = new HashMap<>() {{
        put("Wołowina", BigDecimal.valueOf(0));
        put("Kurczak", BigDecimal.valueOf(0));
        put("Mieszane", BigDecimal.valueOf(0));
    }};

    public static List<Map<String, BigDecimal>> PROPERTY_MAP_LIST =
            List.of(PROPERTY_MAP_OPTIONAL, PROPERTY_MAP_REQUIRED_SAUCE, PROPERTY_MAP_REQUIRED_MEAT);

    public static Map<String, Boolean> PRODUCT_PROPERTIES_NAMES = Map.of("Dodatki", false,
            "Sosy", true,
            "Mięso", true);

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductPropertyRepository productPropertyRepository;
    private final ProductPropertiesRepository productPropertiesRepository;
    private final CustomOrderIdGenerator customOrderIdGenerator;
    Random rand;

    @TechnicalContextDev
    @Transactional
    public void initData() {
        var companyOptional = companyRepository.findByName("Firma Testowa U Buły");
        if (companyOptional.isPresent()) {
            return;
        }
        var companies = createCompanies();
        var users = createFakeUsers();
        setUsersAndCompaniesAndSave(companies, users);

        var productCategories = DatabaseFakerUtils.createCategories(companies, CATEGORY_STRINGS);
        var productPropertyListOfLists = DatabaseFakerUtils.createProductPropertyListOfLists(PROPERTY_MAP_LIST);
        var productPropertiesList = DatabaseFakerUtils.createProductPropertiesList(
                companies.get(0),
                productPropertyListOfLists,
                PRODUCT_PROPERTIES_NAMES
        );

        productPropertyListOfLists = productPropertyListOfLists.stream()
                .map(productPropertyRepository::saveAll)
                .collect(Collectors.toList());

        DatabaseFakerUtils.setProductProperty(productPropertyListOfLists, productPropertiesList);

        var products = DatabaseFakerUtils.createFakeProducts(companies, productCategories);


        log.info("Dane dla buły zostały utworzone");
    }

    private List<User> createFakeUsers() {
        var logins = List.of("macmac", "rejo123", "raajfura", "weronika69", "test");
        List<User> users = new ArrayList<>();
        for (int i = 0; i < logins.size(); i++) {
            users.add(DatabaseFakerUtils.createFakeUser(logins.get(i), passwordEncoder.encode("password123")));
        }

        return userRepository.saveAll(users);
    }

    public List<Company> createCompanies() {
        var names = List.of(" U Buły");
        List<Company> companies = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            companies.add(DatabaseFakerUtils.createFakeCompany(names.get(i), i + 1L));
        }
        companies = companyRepository.saveAll(companies);
        companies.forEach(
                company -> company.setDefaultProductImgUrl(CommonUtils.createDefaultProductImgUrl(company.getId().toString()))
        );
        return companies;
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
}