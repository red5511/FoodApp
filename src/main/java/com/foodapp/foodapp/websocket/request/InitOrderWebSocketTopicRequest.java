package com.foodapp.foodapp.websocket.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class InitOrderWebSocketTopicRequest {
    @Schema(description = "company ids to add", required = true)
    private Set<Long> companyIdsToAdd = new HashSet<>();
    @Schema(description = "company ids to remove", required = true)
    private Set<Long> companyIdsToRemove = new HashSet<>();
}
