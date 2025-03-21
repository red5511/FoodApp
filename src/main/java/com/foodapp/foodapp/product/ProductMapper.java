package com.foodapp.foodapp.product;

import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.product.response.ProductsByCategoryTabView;
import com.foodapp.foodapp.productCategory.ProductCategoryDto;
import com.foodapp.foodapp.productCategory.ProductCategoryMapper;
import com.foodapp.foodapp.productCategory.ProductCategoryRepository;
import com.foodapp.foodapp.productProperties.ProductProperties;
import com.foodapp.foodapp.productProperties.ProductPropertiesDto;
import com.foodapp.foodapp.productProperties.ProductPropertiesMapper;
import com.foodapp.foodapp.productProperties.ProductPropertiesRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ProductMapper {
    public final static String NO_CATEGORY = "Bez kategorii";
    private final ProductCategoryRepository productCategoryRepository;
    private final CompanyRepository companyRepository;
    private final ProductPropertiesRepository productPropertiesRepository;

    public static List<ProductDto> mapToProductsDto(final List<Product> products) {
        return products.stream()
                .sorted(
                        Comparator.comparing(Product::getName)
                ).map(ProductMapper::mapToProductDto)
                .collect(Collectors.toList());
    }

    public static ProductDto mapToProductDto(final Product product) {
        var productCategoryDto = product.getProductCategory() != null ? ProductCategoryDto.builder()
                .companyId(product.getCompany().getId())
                .name(product.getProductCategory().getName())
                .id(product.getProductCategory().getId())
                .build() : null;
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .imgUrl(product.getImgUrl())
                .soldOut(product.isSoldOut())
                .price(product.getPrice())
                .deliveryPrice(product.getDeliveryPrice())
                .takeawayPrice(product.getTakeawayPrice())
                .companyId(product.getCompany().getId())
                .productCategory(productCategoryDto)
                .productPropertiesList(ProductPropertiesMapper.toProductPropertiesDto(product.getProductPropertiesList()))
                .build();
    }

    public static Product mapToProduct(final ProductDto product, final Company company) {
        var productCategory =
                product.getProductCategory() != null ?
                        ProductCategoryMapper.toProductCategory(product.getProductCategory(), company) :
                        null;
        var productPropertiesList =
                product.getProductPropertiesList() != null ?
                        ProductPropertiesMapper.toProductProperties(product.getProductPropertiesList(), company) :
                        null;
        return Product.builder()
                .id(product.getId())
                .status(product.getProductStatus())
                .company(company)
                .name(product.getName())
                .price(product.getPrice())
                .deliveryPrice(product.getDeliveryPrice())
                .takeawayPrice(product.getTakeawayPrice())
                .imgUrl(product.getImgUrl())
                .description(product.getDescription())
                .soldOut(product.isSoldOut())
                .productCategory(productCategory)
                .productPropertiesList(productPropertiesList)
                .build();
    }

    public static List<Product> mapToProduct(final List<ProductDto> mapToProductsDto, final Company company) {
        return mapToProductsDto.stream()
                .map(el -> mapToProduct(el, company))
                .collect(Collectors.toList());
    }

    public List<ProductsByCategoryTabView> toMenuOrderingTabs(final Map<String, List<ProductDto>> productsByCategories,
                                                              final Long companyId) {
        List<ProductsByCategoryTabView> productsByCategoryTabViews = new ArrayList<>();
        List<Map<String, List<ProductDto>>> forAllListOfMaps = new ArrayList<>();

        Sort sort = Sort.by(Sort.Order.asc("sortOrder").nullsLast());
        var categories = productCategoryRepository.findAllByCompanyId(companyId, sort);
        for (var category : categories) {
            var products = productsByCategories.get(category.getName());
            if (CollectionUtils.isEmpty(products)) {
                continue;
            }
            var productsByCategoryTabView = ProductsByCategoryTabView.builder()
                    .categoryTabTitle(category.getName())
                    .productsByCategoryList(List.of(Map.of(category.getName(), products)))
                    .build();
            productsByCategoryTabViews.add(productsByCategoryTabView);
            forAllListOfMaps.add(Map.of(category.getName(), products));
        }

        var forAllProductsByCategoryTabView = ProductsByCategoryTabView.builder()
                .categoryTabTitle("Wszystkie")
                .productsByCategoryList(forAllListOfMaps)
                .build();
        productsByCategoryTabViews.add(0, forAllProductsByCategoryTabView);
        if (productsByCategories.containsKey(NO_CATEGORY)) {
            var products = productsByCategories.get(NO_CATEGORY);

            var withoutCategoryProductsByCategoryTabView = ProductsByCategoryTabView.builder()
                    .categoryTabTitle(NO_CATEGORY)
                    .productsByCategoryList(List.of(Map.of(NO_CATEGORY, products)))
                    .build();
            forAllListOfMaps.add(Map.of(NO_CATEGORY, products));
            productsByCategoryTabViews.add(withoutCategoryProductsByCategoryTabView);
        }
        return productsByCategoryTabViews;
    }

    public Product mapToProductDto(final ProductDto productDto, final Long companyId) {
        var company = companyRepository.findById(companyId)
                .orElseThrow(() -> new SecurityException("Company id not valid"));
        var productCategory = productCategoryRepository.findById(productDto.getProductCategory().getId())
                .orElseThrow(() -> new SecurityException("Wrong category id"));

        List<ProductProperties> productPropertiesList = null;
        if (productDto.getProductPropertiesList() != null) {
            var productPropertiesIds = productDto.getProductPropertiesList().stream()
                    .map(ProductPropertiesDto::getId).toList();
            productPropertiesList = productPropertiesRepository.findAllById(productPropertiesIds);
        }
        return Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .deliveryPrice(productDto.getDeliveryPrice())
                .takeawayPrice(productDto.getTakeawayPrice())
                .description(productDto.getDescription())
                .imgUrl(productDto.getImgUrl())
                .company(company)
                .productCategory(productCategory)
                .productPropertiesList(productPropertiesList)
                .build();
    }
}
