package com.foodapp.foodapp.dashboard.request;


import com.foodapp.foodapp.common.Sort;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetActiveOrdersRequest {
    private List<Sort> sorts;
    @Schema(required = true)
    private Boolean isWaitingToAcceptanceSection;
    @Schema(required = true)
    private Set<Long> companyIds;
}
