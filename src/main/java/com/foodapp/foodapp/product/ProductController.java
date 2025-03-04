package com.foodapp.foodapp.product;

import com.foodapp.foodapp.product.request.CreateProductRequest;
import com.foodapp.foodapp.product.request.DeleteProductRequest;
import com.foodapp.foodapp.product.request.GetProductsRequest;
import com.foodapp.foodapp.product.request.ModifyProductRequest;
import com.foodapp.foodapp.product.response.GetPagedProductsResponse;
import com.foodapp.foodapp.product.response.GetProductsByCategoriesResponse;
import com.foodapp.foodapp.productCategory.ProductCategoryService;
import com.foodapp.foodapp.productProperties.ProductPropertiesService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Product")
public class ProductController {
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final ProductPropertiesService productPropertiesService;
    private final ProductMapper productMapper;


    @PostMapping("/save")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING') or hasAuthority('VIEW_MENU_PANEL')")
    public ResponseEntity<Void> saveProduct(final @RequestBody CreateProductRequest request) {
        productService.saveProduct(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING') or hasAuthority('VIEW_MENU_PANEL')")
    public ResponseEntity<Void> deleteProduct(final @RequestBody DeleteProductRequest request) {
        productService.softDeleteProducts(List.of(request.getProductId()), request.getCompanyId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/modify")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING') or hasAuthority('VIEW_MENU_PANEL')")
    public ResponseEntity<Void> modifyProduct(final @RequestBody ModifyProductRequest request) {
        productService.modifyProduct(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pages")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING') or hasAuthority('VIEW_MENU_PANEL')")
    public ResponseEntity<GetPagedProductsResponse> getPagedProducts(final @RequestBody GetProductsRequest request) {
        var pagedResult = productService.getPagedProducts(request);
        var productCategories = productCategoryService.getAllProductCategoriesByCompanyId(request.getCompanyId());
        var productPropertiesList = productPropertiesService.getAllProductPropertiesByCompanyId(request.getCompanyId());
        var response = GetPagedProductsResponse.builder()
                .pagedResult(pagedResult)
                .productCategories(productCategories)
                .productPropertiesList(productPropertiesList)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/menu-ordering/{companyId}")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING') or hasAuthority('VIEW_MENU_PANEL')")
    public ResponseEntity<GetProductsByCategoriesResponse> getProductsByCategories(final @PathVariable Long companyId) {
        var productsByCategories = productService.getProductsByCategories(companyId);
        var result = productMapper.toMenuOrderingTabs(productsByCategories, companyId);
        var response = GetProductsByCategoriesResponse.builder()
                .menuOrderingTabs(result)
                .build();
        return ResponseEntity.ok(response);
    }
}
