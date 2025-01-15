package com.foodapp.foodapp.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BasePagedRequest {
    @JsonFormat(pattern = "d.MM.yyyy")
    LocalDate dateFrom;
    @JsonFormat(pattern = "d.MM.yyyy")
    LocalDate dateTo;
    private List<Sort> sorts;
    private int page;
    private int size;
    private DateRange dateRange;
}
