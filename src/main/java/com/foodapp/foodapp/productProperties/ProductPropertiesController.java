package com.foodapp.foodapp.productProperties;

import com.foodapp.foodapp.productProperties.request.CreateProductPropertiesRequest;
import com.foodapp.foodapp.productProperties.request.ModifyProductPropertiesRequest;
import com.foodapp.foodapp.productProperties.response.CreateProductPropertiesResponse;
import com.foodapp.foodapp.productProperties.response.GetAllProductPropertiesResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/menu/properties/{companyId}")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING') or hasAuthority('VIEW_MENU_PANEL')")
    public ResponseEntity<GetAllProductPropertiesResponse> getAllProductProperties(final @PathVariable Long companyId) {
        var productPropertiesList = productPropertiesService.getAllProductPropertiesByCompanyId(companyId);
        var response = GetAllProductPropertiesResponse.builder()
                .productPropertiesList(productPropertiesList)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{companyId}/{productPropertiesId}")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING') or hasAuthority('VIEW_MENU_PANEL')")
    public ResponseEntity<Void> deleteProductProperties(
            final @PathVariable Long companyId,
            final @PathVariable Long productPropertiesId) {
        productPropertiesService.deleteProductProperties(companyId, productPropertiesId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/modify")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING') or hasAuthority('VIEW_MENU_PANEL')")
    public ResponseEntity<Void> modifyProductProperties(
            final @RequestBody ModifyProductPropertiesRequest request) {
        productPropertiesService.modifyProductProperties(request.getProductProperties());
        return ResponseEntity.ok().build();
    }

}
