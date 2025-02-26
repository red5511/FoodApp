package com.foodapp.foodapp.delivery;

import com.foodapp.foodapp.administration.company.sql.Company;

import java.util.List;
import java.util.stream.Collectors;

public class DeliveryOptionMapper {
    public static List<DeliveryOptionDto> toDeliveryOptionDto(final List<DeliveryOption> deliveryOptions) {
        return deliveryOptions.stream()
                .map(DeliveryOptionMapper::toDeliveryOptionDto)
                .collect(Collectors.toList());
    }

    public static DeliveryOptionDto toDeliveryOptionDto(final DeliveryOption deliveryOption) {
        return DeliveryOptionDto.builder()
                .id(deliveryOption.getId())
                .deliveryPrice(deliveryOption.getDeliveryPrice())
                .distance(deliveryOption.getDistance())
                .companyId(deliveryOption.getCompany().getId())
                .build();
    }

    public static DeliveryOption toDeliveryOption(final DeliveryOptionDto deliveryOptionDto, final Company company) {
        return DeliveryOption.builder()
                .id(deliveryOptionDto.getId())
                .deliveryPrice(deliveryOptionDto.getDeliveryPrice())
                .distance(deliveryOptionDto.getDistance())
                .company(company)
                .build();
    }
}
