package com.foodapp.foodapp.forDevelopment.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import org.checkerframework.checker.units.qual.A;

@Builder
@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class ProductNameAndImg {
    private String productName;
    private String imgUrl;
}
