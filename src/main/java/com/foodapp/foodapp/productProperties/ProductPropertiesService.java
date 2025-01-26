package com.foodapp.foodapp.productProperties;

import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.productProperties.request.CreateProductPropertiesRequest;
import com.foodapp.foodapp.security.ContextProvider;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

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
}
