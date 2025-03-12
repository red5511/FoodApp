package com.foodapp.foodapp.productProperties;

import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.product.Product;
import com.foodapp.foodapp.productProperties.request.CreateProductPropertiesRequest;
import com.foodapp.foodapp.security.ContextProvider;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ProductPropertiesService {
    private final ProductPropertiesRepository productPropertiesRepository;
    private final ContextProvider contextProvider;
    private final CompanyRepository companyRepository;

    public List<ProductPropertiesDto> getAllProductPropertiesByCompanyId(final Long companyId) {
        contextProvider.validateCompanyAccess(List.of(companyId));
        var productProperties = productPropertiesRepository.findAllByCompanyId(companyId);
        return ProductPropertiesMapper.toProductPropertiesDto(productProperties);
    }

    @Transactional
    public ProductPropertiesDto saveProductProperties(final CreateProductPropertiesRequest request) {
        contextProvider.validateCompanyAccess(List.of(request.getProductProperties().getCompanyId()));
        var company = companyRepository.findById(request.getProductProperties().getCompanyId())
                .orElseThrow(() -> new SecurityException("Wrong company Id"));
        var productProperties = ProductPropertiesMapper.toProductProperties(request.getProductProperties(), company);
        if (productProperties.getProductPropertyList() != null) {
            ProductProperties finalProductProperties = productProperties;
            productProperties.getProductPropertyList().forEach(productProperty -> productProperty.setProductProperties(
                    finalProductProperties));
        }
        productProperties = productPropertiesRepository.save(productProperties);
        return ProductPropertiesMapper.toProductPropertiesDto(productProperties);
    }

    @Transactional
    public void deleteProductProperties(final Long companyId, final Long productPropertiesId) {
        contextProvider.validateCompanyAccess(List.of(companyId));
        var productProperties = productPropertiesRepository.findById(productPropertiesId)
                .orElseThrow(() -> new SecurityException("Wrong properties id"));
        if (!productProperties.getCompany().getId().equals(companyId)) {
            throw new SecurityException("Miss match between comapnyId and productProperties");
        }
        var products = productProperties.getProducts();
        if (!CollectionUtils.isEmpty(products)) {
            List<Product> productsCopy = new ArrayList<>(products);
            for (Product product : productsCopy) {
                productProperties.getProducts().remove(product);
                var filteredProductProperties = product.getProductPropertiesList().stream()
                        .filter(el -> !el.getId().equals(productPropertiesId))
                        .toList();
                product.setProductPropertiesList(filteredProductProperties);
            }
        }
        productPropertiesRepository.delete(productProperties);
    }

    public void modifyProductProperties(final ProductPropertiesDto productPropertiesDto) {
        contextProvider.validateCompanyAccess(List.of(productPropertiesDto.getCompanyId()));
        var modifiedProperties = productPropertiesRepository.findById(productPropertiesDto.getId())
                .orElseThrow(() -> new SecurityException("Wrong modified properties id"));
        if (!modifiedProperties.getCompany().getId().equals(productPropertiesDto.getCompanyId())) {
            throw new SecurityException("Miss match between comapnyId and modified properties id");
        }
        System.out.println(123);
        System.out.println();
        ProductPropertiesMapper.modifiedProductProperties(productPropertiesDto, modifiedProperties);
        productPropertiesRepository.save(modifiedProperties);
    }
}
