package com.foodapp.foodapp.dashboard.request;


import com.foodapp.foodapp.common.Sort;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetActiveOrdersRequest {
    private List<Sort> sorts;
}
