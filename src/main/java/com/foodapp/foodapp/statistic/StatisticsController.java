package com.foodapp.foodapp.statistic;

import com.foodapp.foodapp.statistic.request.GetStatisticsChartRequest;
import com.foodapp.foodapp.statistic.request.GetStatisticsConfigRequest;
import com.foodapp.foodapp.statistic.response.GetStatisticsChartResponse;
import com.foodapp.foodapp.statistic.response.GetStatisticsConfigResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistics")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Statistics")
public class StatisticsController {
    private final StatisticsService statisticsService;

    @PostMapping("/config")
    public ResponseEntity<GetStatisticsConfigResponse> getStatisticsConfig(@RequestBody final GetStatisticsConfigRequest request) {
        var result = statisticsService.getStatisticsConfig(request.getCompanyIds());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/chart")
    public ResponseEntity<GetStatisticsChartResponse> getStatisticsChart(@RequestBody final GetStatisticsChartRequest request) {
        var result = statisticsService.getStatisticsChart(request);
        return ResponseEntity.ok(result);
    }
}