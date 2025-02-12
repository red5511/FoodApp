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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ProductMapper {
    private final ProductCategoryRepository productCategoryRepository;
    private final CompanyRepository companyRepository;
    private final ProductPropertiesRepository productPropertiesRepository;

    public static List<ProductDto> mapToProductsDto(final List<Product> products) {
        return products.stream()
                .map(ProductMapper::mapToProductDto)
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
                .companyId(product.getCompany().getId())
                .productCategory(productCategoryDto)
                .productPropertiesList(ProductPropertiesMapper.toProductPropertiesDto(product.getProductPropertiesList()))
                .build();
    }

    public static Product mapToProduct(final ProductDto product, final Company company) {
        return Product.builder()
                .id(product.getId())
                .status(product.getProductStatus())
                .company(company)
                .name(product.getName())
                .price(product.getPrice())
                .imgUrl(product.getImgUrl())
                .description(product.getDescription())
                .soldOut(product.isSoldOut())
                .productCategory(ProductCategoryMapper.toProductCategory(product.getProductCategory(), company))
                .productPropertiesList(ProductPropertiesMapper.toProductProperties(product.getProductPropertiesList(), company))
                .build();
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
                .description(productDto.getDescription())
                .imgUrl(productDto.getImgUrl())
                .company(company)
                .productCategory(productCategory)
                .productPropertiesList(productPropertiesList)
                .build();
    }
}
