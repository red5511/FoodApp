package com.foodapp.foodapp.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Filter {
    private String fieldName;
    private List<String> values;
    private String mode;
}
