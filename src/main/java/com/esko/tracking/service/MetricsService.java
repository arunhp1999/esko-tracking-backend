package com.esko.tracking.service;

import com.esko.tracking.dto.MetricsSummary;
import com.esko.tracking.model.Event;
import com.esko.tracking.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MetricsService {

    private final EventRepository eventRepository;

    public MetricsService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Fetch events for product and optional time window, then compute metrics.
     * startMillis/endMillis can be null to fetch all for product.
     */
    public MetricsSummary computeSummary(String product, Long startMillis, Long endMillis) {
        List<Event> events;
        if (startMillis != null && endMillis != null) {
            events = eventRepository.findByProductAndTimestampBetween(product, startMillis, endMillis);
        } else {
            events = eventRepository.findByProduct(product);
        }

        // total active: sum of durations for session_end events
        long totalActiveMs = events.stream()
                .filter(e -> e.getAction() != null && e.getAction().equalsIgnoreCase("session_end"))
                .mapToLong(e -> Optional.ofNullable(e.getDuration()).orElse(0L))
                .sum();

        // total idle: sum of durations for idle events
        long totalIdleMs = events.stream()
                .filter(e -> e.getAction() != null && e.getAction().equalsIgnoreCase("idle"))
                .mapToLong(e -> Optional.ofNullable(e.getDuration()).orElse(0L))
                .sum();

        long totalTimeMs = totalActiveMs + totalIdleMs;
        double idlePercentage = totalTimeMs == 0 ? 0.0 : (totalIdleMs * 100.0) / totalTimeMs;

        // feature usage counting: remove session/idle events, keep clicks/features
        Map<String, Long> featureCounts = events.stream()
            .filter(e -> e.getAction() != null)
            .filter(e -> {
                String a = e.getAction().toLowerCase();
                return !(a.equals("session_start") || a.equals("session_end") || a.equals("idle"));
            })
            .collect(Collectors.groupingBy(Event::getAction, Collectors.counting()));

        // top-N features
        LinkedHashMap<String, Long> sortedFeatures = featureCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .limit(20)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a,b) -> a,
                        LinkedHashMap::new
                ));

        String mostUsedFeature = sortedFeatures.keySet().stream().findFirst().orElse(null);

        MetricsSummary summary = new MetricsSummary();
        summary.setProduct(product);
        summary.setTotalActiveMinutes(totalActiveMs / 60000.0);
        summary.setTotalIdleMinutes(totalIdleMs / 60000.0);
        summary.setIdlePercentage(Math.round(idlePercentage * 100.0) / 100.0); // round 2 decimals
        summary.setTopFeatures(sortedFeatures);
        summary.setMostUsedFeature(mostUsedFeature);
        return summary;
    }
}
