package com.foodapp.foodapp.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OpenHours implements Serializable {
    private LocalTime mondayStart;
    private LocalTime mondayEnd;

    private LocalTime tuesdayStart;
    private LocalTime tuesdayEnd;

    private LocalTime wednesdayStart;
    private LocalTime wednesdayEnd;

    private LocalTime thursdayStart;
    private LocalTime thursdayEnd;

    private LocalTime fridayStart;
    private LocalTime fridayEnd;

    private LocalTime saturdayStart;
    private LocalTime saturdayEnd;

    private LocalTime sundayStart;
    private LocalTime sundayEnd;
}
