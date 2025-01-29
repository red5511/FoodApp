package com.foodapp.foodapp.product.response;

import java.util.List;
import java.util.Map;

import com.foodapp.foodapp.product.ProductDto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductsByCategoryTabView {
    //robie to tak zeby na froncie bylo latwiej wyswietlic te taby dla zakladki wszystkie
    private String categoryTabTitle;
    private List<Map<String, List<ProductDto>>> productsByCategoryList;
}
