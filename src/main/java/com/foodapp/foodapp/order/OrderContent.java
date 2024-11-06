package com.foodapp.foodapp.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderContent {
    private HashMap<Long, Integer> quantityProductIdMap = new HashMap<>();

}
