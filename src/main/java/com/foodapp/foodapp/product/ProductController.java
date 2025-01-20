package com.foodapp.foodapp.product;

import com.foodapp.foodapp.administration.userAdministration.request.GetUsersAdministrationRequest;
import com.foodapp.foodapp.administration.userAdministration.response.GetPagedUsersResponse;
import com.foodapp.foodapp.product.request.DeleteProductRequest;
import com.foodapp.foodapp.product.request.GetProductsRequest;
import com.foodapp.foodapp.product.request.ModifyProductRequest;
import com.foodapp.foodapp.product.request.CreateProductRequest;
import com.foodapp.foodapp.product.response.GetPagedProductsResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Product")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/save")
    public ResponseEntity<String> saveProduct(final @RequestBody CreateProductRequest request) {
        productService.saveProduct(request);
        return ResponseEntity.ok("Saved");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> saveProduct(final @RequestBody DeleteProductRequest request) {
        productService.deleteProduct(request);
        return ResponseEntity.ok("Deleted");
    }

    @PutMapping("/modify")
    public ResponseEntity<String> saveProduct(final @RequestBody ModifyProductRequest request) {
        productService.modifyProduct(request);
        return ResponseEntity.ok("Modified");
    }

    @PostMapping("/pages")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING')")
    public ResponseEntity<GetPagedProductsResponse> getPagedProducts(final @RequestBody GetProductsRequest request) {
        var pagedResult = productService.getPagedProducts(request);
        var response = GetPagedProductsResponse.builder()
                                            .pagedResult(pagedResult)
                                            .build();
        return ResponseEntity.ok(response);
    }
}
