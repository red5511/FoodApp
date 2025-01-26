package com.foodapp.foodapp.product;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodapp.foodapp.product.request.CreateProductRequest;
import com.foodapp.foodapp.product.request.DeleteProductRequest;
import com.foodapp.foodapp.product.request.GetProductsRequest;
import com.foodapp.foodapp.product.request.ModifyProductRequest;
import com.foodapp.foodapp.product.response.GetPagedProductsResponse;
import com.foodapp.foodapp.productCategory.ProductCategoryService;
import com.foodapp.foodapp.productProperties.ProductPropertiesService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/product")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Product")
public class ProductController {
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final ProductPropertiesService productPropertiesService;

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING') or hasAuthority('VIEW_MENU_PANEL')")
    public ResponseEntity<String> saveProduct(final @RequestBody CreateProductRequest request) {
        productService.saveProduct(request);
        return ResponseEntity.ok("Saved");
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING') or hasAuthority('VIEW_MENU_PANEL')")
    public ResponseEntity<String> saveProduct(final @RequestBody DeleteProductRequest request) {
        productService.deleteProduct(request);
        return ResponseEntity.ok("Deleted");
    }

    @PutMapping("/modify")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING') or hasAuthority('VIEW_MENU_PANEL')")
    public ResponseEntity<String> saveProduct(final @RequestBody ModifyProductRequest request) {
        productService.modifyProduct(request);
        return ResponseEntity.ok("Modified");
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
}
