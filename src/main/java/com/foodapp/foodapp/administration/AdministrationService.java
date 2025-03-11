package com.foodapp.foodapp.administration;

import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.product.Product;
import com.foodapp.foodapp.product.ProductRepository;
import com.foodapp.foodapp.product.ProductStatus;
import com.foodapp.foodapp.productCategory.ProductCategory;
import com.foodapp.foodapp.productCategory.ProductCategoryRepository;
import com.foodapp.foodapp.productProperties.ProductProperties;
import com.foodapp.foodapp.productProperties.ProductPropertiesRepository;
import com.foodapp.foodapp.productProperties.productProperty.ProductProperty;
import com.foodapp.foodapp.productProperties.productProperty.ProductPropertyRepository;
import com.foodapp.foodapp.user.User;
import com.foodapp.foodapp.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
public class AdministrationService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final ProductCategoryRepository categoryRepository;
    private final ProductPropertyRepository productPropertyRepository;
    private final ProductPropertiesRepository productPropertiesRepository;
    private final ProductRepository productRepository;

    public void addUserToCompany(final Long companyId, final Long userId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new SecurityException("Company not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SecurityException("User not found"));

        company.getUsers().add(user);
        user.getCompanies().add(company);

        companyRepository.save(company);
        userRepository.save(user);
    }

    @Transactional
    public void copyMenu(final Long fromCompanyId, final Long toCompanyId) {
        if (fromCompanyId == null || toCompanyId == null || fromCompanyId.equals(toCompanyId)) {
            throw new IllegalArgumentException("wrong company ids");
        }

        var toCompany = companyRepository.findById(toCompanyId).orElseThrow(() -> new SecurityException("Wrong company id"));

        var newCategories = categoryRepository.findAllByCompanyId(fromCompanyId, null);
        Map<String, ProductProperties> uniqueProductPropertiesMap = new HashMap<>();
        newCategories.forEach(category -> {
            var newCategory = category.toBuilder()
                    .id(null)
                    .company(toCompany)
                    .products(null)
                    .build();

            var newProducts = category.getProducts().stream()
                    .map(product -> {
                        var newProductPropertiesList = product.getProductPropertiesList().stream()
                                .map(productProperties -> {
                                    var fromMap = uniqueProductPropertiesMap.get(
                                            productProperties.getName() + toCompany.getId());
                                    if (fromMap != null) {
                                        return fromMap;
                                    }
                                    var newProductPropertyList = productProperties.getProductPropertyList().stream()
                                            .map(productProperty -> productProperty.toBuilder()
                                                    .id(null)
                                                    .build())
                                            .toList();

                                    var result = productProperties.toBuilder()
                                            .id(null)
                                            .company(toCompany)
                                            .productPropertyList((List<ProductProperty>) newProductPropertyList)
                                            .products(new ArrayList<>())
                                            .build();
                                    ProductProperties finalResult = result;
                                    result.getProductPropertyList().forEach(el -> el.setProductProperties(finalResult));
                                    result = productPropertiesRepository.save(result);
                                    uniqueProductPropertiesMap.put(result.getName() + result.getCompany().getId(), result);
                                    return result;
                                })
                                .toList();

                        return product.toBuilder()
                                .productCategory(newCategory)
                                .company(toCompany)
                                .id(null)
                                .productPropertiesList(newProductPropertiesList)
                                .build();
                    })
                    .collect(Collectors.toList());

            newCategory.toBuilder()
                    .products((List<Product>) newProducts)
                    .build();

            categoryRepository.save(newCategory);
            productRepository.saveAll(newProducts);


        });
        categoryRepository.saveAll(newCategories);
    }

    private void deleteProductsForCompany(final Long toCompanyId) {
        var products = productRepository.findAllByCompanyIdIn(List.of(toCompanyId));
        productRepository.deleteAll(products);
    }

    //    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteCategoriesForCompany(final Long toCompanyId) {
        var categories = categoryRepository.findAllByCompanyId(toCompanyId, null);

        var products = categories.stream()
                .map(ProductCategory::getProducts)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(products)) {
            List<Product> productsCopy = new ArrayList<>(products);
            for (Product product : productsCopy) {
                product.setProductCategory(null);
            }
            categories.forEach(fromCategory -> fromCategory.setProducts(new ArrayList<>()));
        }
        categoryRepository.deleteAll(categories);
    }

    private void deleteAllStuffFromCompany(final Long companyId) {
        categoryRepository.deleteByCompanyId(companyId);
        productPropertiesRepository.deleteByCompanyId(companyId);
        var products = productRepository.findAllByCompanyIdIn(List.of(companyId));
        products.forEach(el -> el.setStatus(ProductStatus.DELETED));
        productRepository.saveAll(products);
    }
}
