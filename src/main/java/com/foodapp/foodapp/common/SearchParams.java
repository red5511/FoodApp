package com.foodapp.foodapp.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchParams {
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private List<Sort> sorts;
    private int page;
    private int size;
}
