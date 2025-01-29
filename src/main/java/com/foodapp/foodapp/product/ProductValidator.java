package com.foodapp.foodapp.product;

import com.foodapp.foodapp.security.ContextProvider;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ProductValidator {
    private final ContextProvider contextProvider;

    public void validate(final ProductDto productDto, final Long companyId) {
        contextProvider.validateCompanyAccess(List.of(companyId));

        var category = productDto.getProductCategory();
        if (!productDto.getCompanyId().equals(category.getCompanyId())) {
            throw new SecurityException("Wrong company ids");
        }
        var productProperties = productDto.getProductPropertiesList();
        if (productProperties != null) {
            productProperties.forEach(productProperty -> {
                if (!productDto.getCompanyId().equals(productProperty.getCompanyId())) {
                    throw new SecurityException("Wrong company id for properties");
                }
                var propertyList = productProperty.getPropertyList();
                if (propertyList != null) {
                    propertyList.forEach(property -> {
                        if (!productProperty.getId().equals(property.getProductPropertiesId())) {
                            throw new SecurityException("Wrong property/properties ids");
                        }
                    });
                }
            });
        }


    }
}
