package com.foodapp.foodapp.productCategory;

import com.foodapp.foodapp.productCategory.request.ChangeProductCategoriesSortOrderRequest;
import com.foodapp.foodapp.productCategory.request.CreateProductCategoryRequest;
import com.foodapp.foodapp.productCategory.response.CreateProductCategoryResponse;
import com.foodapp.foodapp.productCategory.response.GetAllCategoriesResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/menu/category/{companyId}")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING') or hasAuthority('VIEW_MENU_PANEL')")
    public ResponseEntity<GetAllCategoriesResponse> getAllCategories(final @PathVariable Long companyId) {
        var categories = productCategoryService.getAllProductCategoriesByCompanyId(companyId);
        var response = GetAllCategoriesResponse.builder()
                .categories(categories)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/menu/category/{companyId}/change-order")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING') or hasAuthority('VIEW_MENU_PANEL')")
    public ResponseEntity<Void> changeProductCategoriesSortOrder(final @RequestBody ChangeProductCategoriesSortOrderRequest request,
                                                                 final @PathVariable Long companyId) {
        productCategoryService.changeProductCategoriesSortOrder(request.getCategories(), companyId);
        return ResponseEntity.ok().build();
    }
}
