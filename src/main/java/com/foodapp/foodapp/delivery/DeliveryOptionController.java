package com.foodapp.foodapp.delivery;

import com.foodapp.foodapp.delivery.request.CreateDeliveryOptionRequest;
import com.foodapp.foodapp.delivery.request.ModifyDeliveryOptionRequest;
import com.foodapp.foodapp.delivery.response.CreateDeliveryOptionResponse;
import com.foodapp.foodapp.delivery.response.GetAllDeliveryOptionsResponse;
import com.foodapp.foodapp.delivery.response.ModifyDeliveryOptionResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/delivery-option")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "DeliveryOption")
public class DeliveryOptionController {
    private final DeliveryOptionService deliveryOptionService;

    @PostMapping("/save/{companyId}")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING') or hasAuthority('VIEW_MENU_PANEL')")
    public ResponseEntity<CreateDeliveryOptionResponse> saveDeliveryOption(final @PathVariable Long companyId,
                                                                           final @RequestBody CreateDeliveryOptionRequest request) {
        var deliveryOption = deliveryOptionService.saveDeliveryOption(request.getDeliveryOption(), companyId);
        var response = CreateDeliveryOptionResponse.builder()
                .deliveryOption(deliveryOption)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all/{companyId}")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING') or hasAuthority('VIEW_MENU_PANEL')")
    public ResponseEntity<GetAllDeliveryOptionsResponse> getAllDeliveryOptions(final @PathVariable Long companyId) {
        var deliveryOptions = deliveryOptionService.getAllDeliveryOptionByCompanyId(companyId);
        var response = GetAllDeliveryOptionsResponse.builder()
                .deliveryOptions(deliveryOptions)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/modify/{companyId}")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING') or hasAuthority('VIEW_MENU_PANEL')")
    public ResponseEntity<ModifyDeliveryOptionResponse> modifyDeliveryOption(final @RequestBody ModifyDeliveryOptionRequest request,
                                                                             final @PathVariable Long companyId) {
        var deliveryOption = deliveryOptionService.modifyDeliveryOption(request.getDeliveryOption(), companyId);
        var response = ModifyDeliveryOptionResponse.builder()
                .deliveryOption(deliveryOption)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{companyId}/{deliveryOptionId}")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING') or hasAuthority('VIEW_MENU_PANEL')")
    public ResponseEntity<Void> deleteDeliveryOption(final @PathVariable Long companyId,
                                                     final @PathVariable Long deliveryOptionId) {
        deliveryOptionService.deleteDeliveryOption(deliveryOptionId, companyId);
        return ResponseEntity.ok().build();
    }
}
