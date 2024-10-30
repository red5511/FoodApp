package com.foodapp.foodapp.common;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class DateParam {
    private LocalDate date;
    private String mode;
}
