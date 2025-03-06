package com.foodapp.foodapp.forDevelopment.common;

import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.administration.company.sql.Content;
import com.foodapp.foodapp.administration.company.sql.OpenHours;
import com.foodapp.foodapp.common.Address;
import com.foodapp.foodapp.productCategory.ProductCategory;
import com.foodapp.foodapp.productProperties.ProductProperties;
import com.foodapp.foodapp.productProperties.productProperty.ProductProperty;
import com.foodapp.foodapp.user.Role;
import com.foodapp.foodapp.user.User;
import com.foodapp.foodapp.user.permission.Permission;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseFakerUtils {
    public static Random RANDOM = new Random();

    public static List<ProductCategory> createCategories(final List<Company> companies, final List<String> categoryStrings) {
        return categoryStrings.stream().map(el -> createFakeProductCategory(el, companies.get(0))).toList();
    }

    public static ProductCategory createFakeProductCategory(final String name, final Company company) {
        return ProductCategory.builder()
                .name(name)
                .company(company)
                .build();
    }

    public static User createFakeAdmin(final String email, final String encodedPassword) {
        Set<Permission> permissions = new HashSet<>();
        permissions.add(Permission.SUPER_ADMINISTRATOR);

        return User.builder()
                .email(email)
                .firstName("Admin")
                .lastName("Admin")
                .password(encodedPassword)
                .role(Role.SUPER_ADMIN)
                .enabled(true)
                .permissions(permissions)
                .phoneNumber("123456789")
                .build();
    }

    public static User createFakeUser(final String email, final String encodedPassword) {
        Set<Permission> permissions;
        permissions = new HashSet<>();
        permissions.add(Permission.VIEW_ONLINE_ORDERING);
        permissions.add(Permission.VIEW_ORDERS_HISTORY);
        permissions.add(Permission.VIEW_STATISTICS);
        permissions.add(Permission.VIEW_RESTAURANT_ORDERING);
        permissions.add(Permission.VIEW_MENU_PANEL);

        return User.builder()
                .email(email)
                .firstName("Eustachy")
                .lastName("Motyka")
                .password(encodedPassword)
                .role(Role.USER)
                .enabled(true)
                .permissions(permissions)
                .phoneNumber("987654321")
                .build();
    }

    public static Company createFakeCompany(final String name, final Long i) {
        return Company.builder()
                .name("Firma Testowa" + name)
                .content(createContent())
                .webSocketTopicName(name + i)
                .address(Address.builder()
                        .street(RANDOM.nextInt(2) % 2 == 1 ? "Generała Piłsudskiego" : "Piastów")
                        .city(RANDOM.nextInt(2) % 2 == 1 ? "Warszawa" : "Kraków")
                        .streetNumber("555")
                        .postalCode("34-999")
                        .apartmentNumber("102b")
                        .floor("5")
                        .build())
                .build();
    }


    public static Content createContent() {
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

    public static List<ProductProperty> createProductPropertyList(final Map<String, BigDecimal> propertyMap) {
        return propertyMap.entrySet().stream()
                .map(el -> createFakeProductProperty(el.getKey(), el.getValue()))
                .collect(Collectors.toList());
    }

    public static ProductProperty createFakeProductProperty(final String name, final BigDecimal price) {
        return ProductProperty.builder()
                .name(name)
                .price(price)
                .build();
    }

    public static List<ProductProperties> createProductPropertiesList(final String name,
                                                                      final boolean isRequired,
                                                                      final Company company,
                                                                      final List<ProductProperty> productPropertyList) {
        return List.of(
                DatabaseFakerUtils.createFakeProductProperties(name, isRequired, company, productPropertyList
                ));
    }

    public static ProductProperties createFakeProductProperties(final String name, final boolean required, final Company company,
                                                                final List<ProductProperty> productPropertyList) {
        return ProductProperties.builder()
                .required(required)
                .name(name)
                .company(company)
                .productPropertyList(productPropertyList)
                .build();
    }

    public static List<List<ProductProperty>> createProductPropertyListOfLists(final List<Map<String, BigDecimal>> propertyMapList) {
        return propertyMapList.stream()
                .map(DatabaseFakerUtils::createProductPropertyList)
                .collect(Collectors.toList());
    }

    public static List<ProductProperties> createProductPropertiesList(Company company,
                                                                      List<List<ProductProperty>> productPropertyListOfLists,
                                                                      Map<String, Boolean> productPropertiesNames) {
        var result = new ArrayList<ProductProperties>();

        int i = 0;
        for (var entry : productPropertiesNames.entrySet()) {
            result.add(DatabaseFakerUtils.createFakeProductProperties(entry.getKey(),
                    entry.getValue(),
                    company,
                    productPropertyListOfLists.get(i)));
            i++;
        }
        return result;
    }

    public static void setProductProperty(List<List<ProductProperty>> productPropertyListOfLists,
                                          List<ProductProperties> productPropertiesList) {
        for (int i = 0; i < productPropertiesList.size(); i++) {
            var productPropertyList = productPropertyListOfLists.get(i);
            int finalI = i;
            productPropertyList.forEach(el -> el.setProductProperties(productPropertiesList.get(finalI)));
        }
    }

    public static void createFakeProducts(List<Company> companies, List<ProductCategory> productCategories) {
    }
}
