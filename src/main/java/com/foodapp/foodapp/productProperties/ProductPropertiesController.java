package com.foodapp.foodapp.productProperties;

import com.foodapp.foodapp.productProperties.request.CreateProductPropertiesRequest;
import com.foodapp.foodapp.productProperties.response.CreateProductPropertiesResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product-properties")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "ProductProperties")
public class ProductPropertiesController {
    private final ProductPropertiesService productPropertiesService;

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING') or hasAuthority('VIEW_MENU_PANEL')")
    public ResponseEntity<CreateProductPropertiesResponse> saveProductProperties(
            final @RequestBody CreateProductPropertiesRequest request) {
        var productPropertiesDto = productPropertiesService.saveProductProperties(request);
        var response = CreateProductPropertiesResponse.builder()
                .productProperties(productPropertiesDto)
                .build();
        return ResponseEntity.ok(response);
    }
}
