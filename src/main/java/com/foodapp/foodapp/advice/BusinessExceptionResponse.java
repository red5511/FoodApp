package com.foodapp.foodapp.advice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BusinessExceptionResponse {
    private String errorCode;
    private Map<String, Object> parameters;
}
