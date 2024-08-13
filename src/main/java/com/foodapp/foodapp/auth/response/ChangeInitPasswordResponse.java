package com.foodapp.foodapp.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(staticName = "of")
public class ChangeInitPasswordResponse {
    private boolean isSuccess;
}
