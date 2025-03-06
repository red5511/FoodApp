package com.foodapp.foodapp.forDevelopment.uBuly;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.common.CommonUtils;
import com.foodapp.foodapp.forDevelopment.TechnicalContextDev;
import com.foodapp.foodapp.forDevelopment.common.DatabaseFakerUtils;
import com.foodapp.foodapp.order.sql.CustomOrderIdGenerator;
import com.foodapp.foodapp.order.sql.OrderRepository;
import com.foodapp.foodapp.orderProduct.OrderProductRepository;
import com.foodapp.foodapp.product.ProductDto;
import com.foodapp.foodapp.product.ProductRepository;
import com.foodapp.foodapp.productCategory.ProductCategoryRepository;
import com.foodapp.foodapp.productProperties.ProductPropertiesRepository;
import com.foodapp.foodapp.productProperties.productProperty.ProductPropertyRepository;
import com.foodapp.foodapp.user.User;
import com.foodapp.foodapp.user.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class UBulyDatabaseDataFaker implements UBulyDatabaseDataFakerInterface {
    public static String CATEGORY = "Duży głód";
    public static String CATEGORY2 = "Mały głód";
    public static String CATEGORY3 = "Zapieksy";
    public static String CATEGORY4 = "Napoje";
    public static List<String> CATEGORY_STRINGS = List.of(CATEGORY, CATEGORY2, CATEGORY3, CATEGORY4);


    public static String KEBAB_W_CIESCIE_NAME = "Kebab w cieście";
    public static String KEBAB_W_CIESCIE_Z_FRYTKAMI_NAME = "Kebab w cieście z frytkami";
    public static String KEBAB_W_CIESCIE_SAMO_MIESO_NAME = "Kebab w cieście samo mięso";
    public static String KEBAB_W_BULCE_NAME = "Kebab w bułce";
    public static String KEBAB_W_BULCE_Z_FRYTKAMI_FRYTKAMI_NAME = "Kebab w bułce z frytkami";
    public static String ROG_NAME = "Róg";
    public static String KEBAB_ZESTAW_NAME = "Kebab zestaw";
    public static String BOX_SALATKOWY_NAME = "Box sałatkowy";
    public static String KEBAB_VEGE_NAME = "Kebab wegetariański";
    public static String FRYTKI_NAME = "Frytki duże";
    public static List<String> PRODUCTS_NAMES =
        List.of(KEBAB_W_CIESCIE_NAME, KEBAB_W_CIESCIE_Z_FRYTKAMI_NAME, KEBAB_W_CIESCIE_SAMO_MIESO_NAME, KEBAB_W_BULCE_NAME, KEBAB_W_BULCE_Z_FRYTKAMI_FRYTKAMI_NAME,
                ROG_NAME, KEBAB_ZESTAW_NAME, BOX_SALATKOWY_NAME, KEBAB_VEGE_NAME, FRYTKI_NAME
        );

    public static ProductDto KEBAB_W_CIESCIE =
        DatabaseFakerUtils.buildProductDto(KEBAB_W_CIESCIE_NAME, getImgUrl(KEBAB_W_CIESCIE_NAME), BigDecimal.valueOf(27), BigDecimal.valueOf(2),
                                           BigDecimal.valueOf(1), ""
        );
    public static ProductDto KEBAB_W_CIESCIE_Z_FRYTKAMI =
        DatabaseFakerUtils.buildProductDto(KEBAB_W_CIESCIE_Z_FRYTKAMI_NAME,  getImgUrl(KEBAB_W_CIESCIE_Z_FRYTKAMI_NAME), BigDecimal.valueOf(29),
                                           BigDecimal.valueOf(2), BigDecimal.valueOf(1), ""
        );
    public static ProductDto KEBAB_W_CIESCIE_SAMO_MIESO =
        DatabaseFakerUtils.buildProductDto(KEBAB_W_CIESCIE_SAMO_MIESO_NAME,  getImgUrl(KEBAB_W_CIESCIE_SAMO_MIESO_NAME), BigDecimal.valueOf(29),
                                           BigDecimal.valueOf(2), BigDecimal.valueOf(1), ""
        );
    public static ProductDto KEBAB_W_BULCE =
        DatabaseFakerUtils.buildProductDto(KEBAB_W_BULCE_NAME, getImgUrl(KEBAB_W_BULCE_NAME), BigDecimal.valueOf(27), BigDecimal.valueOf(2), BigDecimal.valueOf(1),
                                           ""
        );
    public static ProductDto KEBAB_W_BULCE_Z_FRYTKAMI =
        DatabaseFakerUtils.buildProductDto(KEBAB_W_BULCE_Z_FRYTKAMI_FRYTKAMI_NAME,  getImgUrl(KEBAB_W_BULCE_Z_FRYTKAMI_FRYTKAMI_NAME), BigDecimal.valueOf(29),
                                           BigDecimal.valueOf(2), BigDecimal.valueOf(1), ""
        );
    public static ProductDto ROG =
        DatabaseFakerUtils.buildProductDto(ROG_NAME,  getImgUrl(ROG_NAME), BigDecimal.valueOf(29),
                                           BigDecimal.valueOf(2), BigDecimal.valueOf(1), ""
        );
    public static ProductDto KEBAB_ZESTAW =
        DatabaseFakerUtils.buildProductDto(KEBAB_ZESTAW_NAME, getImgUrl(KEBAB_ZESTAW_NAME), BigDecimal.valueOf(30), BigDecimal.valueOf(2), BigDecimal.valueOf(1),
                                           ""
        );
    public static ProductDto BOX_SALATKOWY =
        DatabaseFakerUtils.buildProductDto(BOX_SALATKOWY_NAME, getImgUrl(BOX_SALATKOWY_NAME), BigDecimal.valueOf(25), BigDecimal.valueOf(2), BigDecimal.valueOf(1),
                                           ""
        );

    public static ProductDto KEBAB_VEGE =
        DatabaseFakerUtils.buildProductDto(KEBAB_VEGE_NAME,  getImgUrl(KEBAB_VEGE_NAME), BigDecimal.valueOf(22), BigDecimal.valueOf(2),
                                           BigDecimal.valueOf(1), ""
        );

    public static ProductDto FRYTKI =
        DatabaseFakerUtils.buildProductDto(FRYTKI_NAME,  getImgUrl(FRYTKI_NAME), BigDecimal.valueOf(15), BigDecimal.valueOf(2),
                                           BigDecimal.valueOf(1), ""
        );

    public static List<ProductDto> PRODUCTS =
        List.of(KEBAB_W_CIESCIE, KEBAB_W_CIESCIE_Z_FRYTKAMI, KEBAB_W_CIESCIE_SAMO_MIESO, KEBAB_W_BULCE, KEBAB_W_BULCE_Z_FRYTKAMI,
                ROG, KEBAB_ZESTAW, BOX_SALATKOWY, KEBAB_VEGE, FRYTKI);

    public static String PRODUKT_NAME2_1 = "Kebab małe ciasto";
    public static String PRODUKT_NAME2_2 = "Kebab małe ciasto z frytkami";
    public static String PRODUKT_NAME2_3 = "Kebab małe ciasto samo mięso";
    public static String PRODUKT_NAME2_4 = "Kebab mała bułka";
    public static String PRODUKT_NAME2_5 = "Kebab mała bułka z frytkami";
    public static String PRODUKT_NAME2_6 = "Mały róg";
    public static String PRODUKT_NAME2_7 = "Mały zestaw";
    public static String PRODUKT_NAME2_8 = "Frytki";
    public static List<String> PRODUCTS_NAMES2 = List.of(PRODUKT_NAME2_1, PRODUKT_NAME2_2, PRODUKT_NAME2_3, PRODUKT_NAME2_4, PRODUKT_NAME2_5,
                                                         PRODUKT_NAME2_6, PRODUKT_NAME2_7, PRODUKT_NAME2_8);


    public static ProductDto PRODUKT2_1 =
        DatabaseFakerUtils.buildProductDto(PRODUKT_NAME2_1, getImgUrl(PRODUKT_NAME2_1), BigDecimal.valueOf(22), BigDecimal.valueOf(2), BigDecimal.valueOf(1), "");
    public static ProductDto PRODUKT2_2 =
        DatabaseFakerUtils.buildProductDto(PRODUKT_NAME2_2, getImgUrl(PRODUKT_NAME2_2), BigDecimal.valueOf(24), BigDecimal.valueOf(2), BigDecimal.valueOf(1), "");
    public static ProductDto PRODUKT2_3 =
        DatabaseFakerUtils.buildProductDto(PRODUKT_NAME2_3, getImgUrl(PRODUKT_NAME2_3), BigDecimal.valueOf(24), BigDecimal.valueOf(2), BigDecimal.valueOf(1), "");
    public static ProductDto PRODUKT2_4 =
        DatabaseFakerUtils.buildProductDto(PRODUKT_NAME2_4, getImgUrl(PRODUKT_NAME2_4), BigDecimal.valueOf(22), BigDecimal.valueOf(2), BigDecimal.valueOf(1), "");
    public static ProductDto PRODUKT2_5 =
        DatabaseFakerUtils.buildProductDto(PRODUKT_NAME2_5, getImgUrl(PRODUKT_NAME2_5), BigDecimal.valueOf(24), BigDecimal.valueOf(2), BigDecimal.valueOf(1), "");
    public static ProductDto PRODUKT2_6 =
        DatabaseFakerUtils.buildProductDto(PRODUKT_NAME2_6, getImgUrl(PRODUKT_NAME2_6), BigDecimal.valueOf(25), BigDecimal.valueOf(2), BigDecimal.valueOf(1), "");
    public static ProductDto PRODUKT2_7 =
        DatabaseFakerUtils.buildProductDto(PRODUKT_NAME2_7, getImgUrl(PRODUKT_NAME2_7), BigDecimal.valueOf(26), BigDecimal.valueOf(2), BigDecimal.valueOf(1), "");
    public static ProductDto PRODUKT2_8 =
        DatabaseFakerUtils.buildProductDto(PRODUKT_NAME2_8, getImgUrl(PRODUKT_NAME2_8), BigDecimal.valueOf(10), BigDecimal.valueOf(2), BigDecimal.valueOf(1), "");


    public static List<ProductDto> PRODUCTS2 = List.of(PRODUKT2_1, PRODUKT2_2, PRODUKT2_3, PRODUKT2_4,
                                                       PRODUKT2_5, PRODUKT2_6, PRODUKT2_7, PRODUKT2_8);


    public static String PRODUKT_NAME3_1 = "Zapiekanka";
    public static String PRODUKT_NAME3_2 = "Zapiekanka wół";
    public static String PRODUKT_NAME3_3 = "Zapiekanka kura";

    public static ProductDto PRODUKT3_1 =
        DatabaseFakerUtils.buildProductDto(PRODUKT_NAME3_1, getImgUrl(PRODUKT_NAME3_1), BigDecimal.valueOf(15), BigDecimal.valueOf(2), BigDecimal.valueOf(1), "");
    public static ProductDto PRODUKT3_2 =
        DatabaseFakerUtils.buildProductDto(PRODUKT_NAME3_2, getImgUrl(PRODUKT_NAME3_2), BigDecimal.valueOf(24), BigDecimal.valueOf(2), BigDecimal.valueOf(1), "");
    public static ProductDto PRODUKT3_3 =
        DatabaseFakerUtils.buildProductDto(PRODUKT_NAME3_3, getImgUrl(PRODUKT_NAME3_3), BigDecimal.valueOf(24), BigDecimal.valueOf(2), BigDecimal.valueOf(1), "");

    public static List<ProductDto> PRODUCTS3 = List.of(PRODUKT3_1, PRODUKT3_2, PRODUKT3_3);

    public static String PRODUKT_NAME4_1 = "Puszka";
    public static String PRODUKT_NAME4_2 = "Butelka";
    public static String PRODUKT_NAME4_3 = "Woda";

    public static ProductDto PRODUKT4_1 =
        DatabaseFakerUtils.buildProductDto(PRODUKT_NAME4_1, getImgUrl(PRODUKT_NAME4_1), BigDecimal.valueOf(6), BigDecimal.valueOf(2), BigDecimal.valueOf(1), "");
    public static ProductDto PRODUKT4_2 =
        DatabaseFakerUtils.buildProductDto(PRODUKT_NAME4_2, getImgUrl(PRODUKT_NAME4_2), BigDecimal.valueOf(8), BigDecimal.valueOf(2), BigDecimal.valueOf(1), "");
    public static ProductDto PRODUKT4_3 =
        DatabaseFakerUtils.buildProductDto(PRODUKT_NAME4_3, getImgUrl(PRODUKT_NAME4_3), BigDecimal.valueOf(4), BigDecimal.valueOf(2), BigDecimal.valueOf(1), "");

    public static List<ProductDto> PRODUCTS4 = List.of(PRODUKT4_1, PRODUKT4_2, PRODUKT4_3);




    public static Map<String, List<ProductDto>> PRODUCTS_CATEGORY_MAP = Map.of(
        CATEGORY, PRODUCTS,
        CATEGORY2, PRODUCTS2,
        CATEGORY3, PRODUCTS3,
        CATEGORY4, PRODUCTS4
    );

    public static String DODATKOWE_MIESO = "Podwójne mięso";
    public static String JALAPENO = "Jalapeno";
    public static String CEBULKA_PRAZONA = "Cebulka prażona";
    public static String DODATKOWY_SOS = "Dodatkowy sos";
    public static String SER_ZOLTY = "Ser zółty";
    public static String DODATKOWY_SOS_EXTRA = "Dodatkowy sos na wynos";
    public static List<String> PROPERTY_LIST = List.of(DODATKOWE_MIESO, JALAPENO, CEBULKA_PRAZONA, DODATKOWY_SOS, SER_ZOLTY, DODATKOWY_SOS_EXTRA);

    public static String SOS1 = "Czosnkowy";
    public static String SOS2 = "Koperkowy";
    public static String SOS3 = "Ostry";
    public static String SOS4 = "Brak sosu";
    public static String SOS5 = "Mieszany";
    public static List<String> PROPERTY_LIST2 = List.of(SOS1, SOS2, SOS3, SOS4, SOS5);

    public static String WOLOWINA = "Wołowina";
    public static String KURCZAK = "Kurczak";
    public static String MIESZANE = "Mieszane";
    public static List<String> PROPERTY_LIST3 = List.of(WOLOWINA, KURCZAK, MIESZANE);

    public static Map<String, BigDecimal> PROPERTY_MAP_ALL = new HashMap<>() {{
        put(DODATKOWE_MIESO, BigDecimal.valueOf(9));
        put(JALAPENO, BigDecimal.valueOf(3));
        put(SER_ZOLTY, BigDecimal.valueOf(3));
        put(CEBULKA_PRAZONA, BigDecimal.valueOf(3));
        put(DODATKOWY_SOS, BigDecimal.valueOf(2));
        put(DODATKOWY_SOS_EXTRA, BigDecimal.valueOf(2));

        put(SOS1, BigDecimal.valueOf(0));
        put(SOS2, BigDecimal.valueOf(0));
        put(SOS3, BigDecimal.valueOf(0));
        put(SOS4, BigDecimal.valueOf(0));
        put(SOS5, BigDecimal.valueOf(0));

        put(WOLOWINA, BigDecimal.valueOf(0));
        put(KURCZAK, BigDecimal.valueOf(0));
        put(MIESZANE, BigDecimal.valueOf(0));
    }};

    public static String PROPERTIES_DODATKI = "Dodatki";
    public static String PROPERTIES_SOSY = "Sosy";
    public static String PROPERTIES_MIESO = "Mięso";

    public static Map<String, List<String>> PROPERTIES_WITH_PROPERTY_MAP = Map.of(
        PROPERTIES_DODATKI, PROPERTY_LIST,
        PROPERTIES_SOSY, PROPERTY_LIST2,
        PROPERTIES_MIESO, PROPERTY_LIST3
    );

    public static Map<String, Boolean> PRODUCT_PROPERTIES_NAMES = Map.of(PROPERTIES_DODATKI, false,
                                                                         PROPERTIES_SOSY, true,
                                                                         PROPERTIES_MIESO, true
    );

    public static Map<String, List<String>> PROPERTIES_PRODUCTS_MAP = Map.of(
        PROPERTIES_DODATKI, Stream.concat(PRODUCTS_NAMES.stream(), PRODUCTS_NAMES2.stream())
                                  .collect(Collectors.toList()),
        PROPERTIES_SOSY, Stream.concat(PRODUCTS_NAMES.stream(), PRODUCTS_NAMES2.stream())
                               .collect(Collectors.toList()),
        PROPERTIES_MIESO, Stream.concat(PRODUCTS_NAMES.stream(), PRODUCTS_NAMES2.stream())
                                .collect(Collectors.toList())
    );

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
        if(companyOptional.isPresent()) {
            return;
        }
        var companies = createCompanies();
        var users = createFakeUsers();
        setUsersAndCompaniesAndSave(companies, users);

        var productCategories = DatabaseFakerUtils.createCategories(companies, CATEGORY_STRINGS);
        var productPropertyListMap = DatabaseFakerUtils.createProductPropertyListOfLists(PROPERTY_MAP_ALL, PROPERTIES_WITH_PROPERTY_MAP);
        var productPropertiesListMap = DatabaseFakerUtils.createProductPropertiesList(
            companies.get(0),
            productPropertyListMap,
            PRODUCT_PROPERTIES_NAMES
        );

        productPropertiesListMap = productPropertiesListMap.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, el -> productPropertiesRepository.save(el.getValue())));

        DatabaseFakerUtils.setProductProperty(productPropertyListMap, productPropertiesListMap);

        var productsList = DatabaseFakerUtils.createFakeProducts(companies.get(0), PRODUCTS_CATEGORY_MAP, productCategories);
        DatabaseFakerUtils.setProductProperties(productsList, productPropertiesListMap);

        var products = productsList.stream()
                                   .flatMap(Collection::stream)
                                   .collect(Collectors.toList());

        productPropertiesRepository.saveAll(productPropertiesListMap.values().stream().toList());
        companyRepository.saveAll(companies);
        productCategoryRepository.saveAll(productCategories);
        productRepository.saveAll(products);

        log.info("Dane dla buły zostały utworzone");
    }

    public static String getImgUrl(final String name){
        return "U_BULY/" + name.replaceAll(" ", "_").toUpperCase() + ".jpg";
    }

    private List<User> createFakeUsers() {
        var logins = List.of("macmac", "rejo123", "raajfura", "sweetWerooo", "test");
        List<User> users = new ArrayList<>();
        for(int i = 0; i < logins.size(); i++) {
            users.add(DatabaseFakerUtils.createFakeUser(logins.get(i), passwordEncoder.encode("password123")));
        }

        return userRepository.saveAll(users);
    }

    public List<Company> createCompanies() {
        var names = List.of(" U Buły");
        List<Company> companies = new ArrayList<>();
        for(int i = 0; i < names.size(); i++) {
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
