package com.foodapp.foodapp.productCategory;

import com.foodapp.foodapp.productCategory.request.CreateProductCategoryRequest;
import com.foodapp.foodapp.productCategory.response.CreateProductCategoryResponse;
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
@RequestMapping("/api/v1/product-category")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "ProductCategory")
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING') or hasAuthority('VIEW_MENU_PANEL')")
    public ResponseEntity<CreateProductCategoryResponse> saveProductCategory(
            final @RequestBody CreateProductCategoryRequest request) {
        var productCategory = productCategoryService.saveProductCategory(request);
        var response = CreateProductCategoryResponse.builder()
                .productCategory(productCategory)
                .build();
        return ResponseEntity.ok(response);
    }
}
