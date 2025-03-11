package com.foodapp.foodapp.forDevelopment.common;

import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.administration.company.sql.Content;
import com.foodapp.foodapp.administration.company.sql.OpenHours;
import com.foodapp.foodapp.common.Address;
import com.foodapp.foodapp.product.Product;
import com.foodapp.foodapp.product.ProductDto;
import com.foodapp.foodapp.product.ProductMapper;
import com.foodapp.foodapp.product.ProductStatus;
import com.foodapp.foodapp.productCategory.ProductCategory;
import com.foodapp.foodapp.productProperties.ProductProperties;
import com.foodapp.foodapp.productProperties.ProductPropertiesDto;
import com.foodapp.foodapp.productProperties.productProperty.ProductProperty;
import com.foodapp.foodapp.user.Role;
import com.foodapp.foodapp.user.User;
import com.foodapp.foodapp.user.permission.Permission;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.foodapp.foodapp.forDevelopment.uBuly.UBulyDatabaseDataFaker.PROPERTIES_PRODUCTS_MAP;

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
        permissions.add(Permission.ADMINISTRATOR);

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
                .webSocketTopicName(name.replaceAll(" ", "_") + i)
                .logoUrl("images/" + "U_BULY/U_BULY_LOGO2.png")
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

    public static ProductProperty createProductPropertyList(final Map.Entry<String, BigDecimal> entry) {
        return createFakeProductProperty(entry.getKey(), entry.getValue());
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
                                                                      final List<ProductProperty> productPropertyList,
                                                                      final Integer maxChosenOptions) {
        return List.of(
                DatabaseFakerUtils.createFakeProductProperties(name, isRequired, company, productPropertyList, maxChosenOptions
                ));
    }

    public static ProductProperties createFakeProductProperties(final String name, final boolean required, final Company company,
                                                                final List<ProductProperty> productPropertyList,
                                                                final Integer maxChosenOptions) {
        return ProductProperties.builder()
                .required(required)
                .name(name)
                .company(company)
                .productPropertyList(productPropertyList)
                .maxChosenOptions(maxChosenOptions)
                .build();
    }

    //        nazwa Propertiesow do korych nalezy lista property
    public static Map<String, List<ProductProperty>> createProductPropertyListOfLists(final Map<String, BigDecimal> propertyMapList,
                                                                                      final Map<String, List<String>> propertiesWithPropertyMap) {

        var propertyMap = propertyMapList.entrySet().stream()
                .map(DatabaseFakerUtils::createProductPropertyList)
                .collect(Collectors.toMap(ProductProperty::getName, Function.identity()));

        return propertiesWithPropertyMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, el -> el.getValue().stream()
                        .map(propertyMap::get)
                        .collect(Collectors.toList())
                ));

    }

    public static Map<String, ProductProperties> createProductPropertiesList(Company company,
                                                                             Map<String, List<ProductProperty>> propertiesByPropertyList,
                                                                             Map<String, ProductPropertiesDto> productPropertiesNames) {

        var result = new ArrayList<ProductProperties>();

        for (var entry : productPropertiesNames.entrySet()) {
            result.add(DatabaseFakerUtils.createFakeProductProperties(entry.getKey(),
                    entry.getValue().isRequired(),
                    company,
                    propertiesByPropertyList.get(entry.getKey()),
                    entry.getValue().getMaxChosenOptions()
            ));
        }
        return result.stream()
                .collect(Collectors.toMap(
                        ProductProperties::getName, Function.identity()
                ));
    }

    public static void setProductProperty(Map<String, List<ProductProperty>> productPropertyLisByPropertiesNametMap,
                                          Map<String, ProductProperties> productPropertiesByNameMap) {
        for (var entry : productPropertyLisByPropertiesNametMap.entrySet()) {
            entry.getValue().forEach(el -> el.setProductProperties(productPropertiesByNameMap.get(entry.getKey())));
        }
    }

    public static List<List<Product>> createFakeProducts(final Company company,
                                                         final Map<String, List<ProductDto>> productCategoriesMap,
                                                         final List<ProductCategory> productCategories) {

        return productCategories.stream()
                .map(el -> {
                    var productsDto = productCategoriesMap.getOrDefault(el.getName(), List.of());
                    return productsDto.stream()
                            .map(product -> {
                                var result = ProductMapper.mapToProduct(product, company);
                                result.setProductCategory(el);
                                return result;

                            })
                            .collect(Collectors.toList());
                })
                .toList();
    }

    public static ProductDto buildProductDto(final String name, final String image, final BigDecimal price,
                                             final BigDecimal deliveryPrice,
                                             final BigDecimal takeawayPrice, final String description) {
        return ProductDto.builder()
                .name(name)
                .price(price)
                .imgUrl(image)
                .deliveryPrice(deliveryPrice)
                .takeawayPrice(takeawayPrice)
                .description(description)
                .productStatus(ProductStatus.ACTIVE)
                .build();
    }

    public static void setProductProperties(final List<List<Product>> productsList,
                                            final Map<String, ProductProperties> productPropertiesMap) {
        var productsByNameMap = productsList.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Product::getName, Function.identity()));

        productsByNameMap.entrySet().stream()
                .forEach(productEntry -> {
                    PROPERTIES_PRODUCTS_MAP.entrySet().forEach(el2 -> {
                        if (el2.getValue().contains(productEntry.getKey())) {
                            var product = productEntry.getValue();
                            if (product.getProductPropertiesList() == null) {
                                product.setProductPropertiesList(new ArrayList<>());
                            }
                            var productProperties = productPropertiesMap.get(el2.getKey());


                            if (productProperties != null) {
                                product.getProductPropertiesList().add(productProperties);
                                if (productProperties.getProducts() == null) {
                                    productProperties.setProducts(new ArrayList<>());
                                }
                                productProperties.getProducts().add(product);
                            }
                        }
                    });
                });
    }
}
