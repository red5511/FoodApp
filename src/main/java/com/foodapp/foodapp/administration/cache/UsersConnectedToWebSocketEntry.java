package com.foodapp.foodapp.administration.cache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class UsersConnectedToWebSocketEntry {
    public Long userId;
    public Long companyId;
}
