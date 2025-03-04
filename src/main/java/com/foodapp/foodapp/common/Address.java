package com.foodapp.foodapp.common;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Builder
@Embeddable
@NoArgsConstructor
public class Address implements Serializable {
    private String street;
    private String streetNumber;
    private String city;
    private String postalCode;
    private String country;
    private String floor;
    private String apartmentNumber;
    private String unstructuredAddress;
}
