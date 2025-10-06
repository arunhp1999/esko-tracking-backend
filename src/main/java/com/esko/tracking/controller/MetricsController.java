package com.esko.tracking.controller;

import com.esko.tracking.dto.MetricsSummary;
import com.esko.tracking.service.MetricsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/metrics")
@CrossOrigin(origins = "*")
public class MetricsController {

    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping("/summary")
    public MetricsSummary getSummary(
            @RequestParam String product,
            @RequestParam(required = false) Long start,
            @RequestParam(required = false) Long end
    ) {
        return metricsService.computeSummary(product, start, end);
    }
}
