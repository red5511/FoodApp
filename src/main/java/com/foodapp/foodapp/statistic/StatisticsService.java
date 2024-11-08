package com.foodapp.foodapp.statistic;

import com.foodapp.foodapp.common.DatePeriod;
import com.foodapp.foodapp.common.DateRange;
import com.foodapp.foodapp.product.ProductMapper;
import com.foodapp.foodapp.product.ProductRepository;
import com.foodapp.foodapp.statistic.response.GetStatisticsConfigResponse;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class StatisticsService {
    private final ProductRepository productRepository;

    public Object getStatisticsOrderCount() {
        return null;
    }

    public GetStatisticsConfigResponse getStatisticsConfig(List<Long> companyIds) {
        var datePeriods = Arrays.asList(DatePeriod.values());
        var dateRanges = Arrays.asList(DateRange.values());
        var products = productRepository.findAllByCompanyIdIn(companyIds);
        return GetStatisticsConfigResponse.builder()
                .datePeriods(datePeriods)
                .dateRanges(dateRanges)
                .products(ProductMapper.mapToProductsDto(products))
                .build();
    }
}
