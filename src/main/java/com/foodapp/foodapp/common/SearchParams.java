package com.foodapp.foodapp.common;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class SearchParams {
    List<Filter> filters;
    List<Sort> sorts;
    int page;
    int size;
    private Long companyId;
}
