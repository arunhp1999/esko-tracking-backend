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

    /**
     * GET /api/metrics/summary?product=MobileApp&start=1633024800000&end=1635616800000
     * start/end are epoch millis (optional)
     */
    @GetMapping("/summary")
    public MetricsSummary getSummary(
            @RequestParam String product,
            @RequestParam(required = false) Long start,
            @RequestParam(required = false) Long end) {
        return metricsService.computeSummary(product, start, end);
    }
}
