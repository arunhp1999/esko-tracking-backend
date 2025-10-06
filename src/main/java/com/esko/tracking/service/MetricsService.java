package com.esko.tracking.service;

import com.esko.tracking.dto.MetricsSummary;
import com.esko.tracking.model.Event;
import com.esko.tracking.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MetricsService {

    private final EventRepository eventRepository;

    public MetricsService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public MetricsSummary computeSummary(String product, Long startMillis, Long endMillis) {
        List<Event> events;

        if (startMillis != null && endMillis != null) {
            LocalDateTime startTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(startMillis), ZoneOffset.UTC);
            LocalDateTime endTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(endMillis), ZoneOffset.UTC);
            events = eventRepository.findByProductAndTimestampBetween(product, startTime, endTime);
        } else {
            events = eventRepository.findByProductOrderByTimestampAsc(product);
        }

        double totalActiveMinutes = 0.0;
        double totalIdleMinutes = 0.0;
        LocalDateTime lastStart = null;

        for (Event e : events) {
            if ("session_start".equalsIgnoreCase(e.getEventType())) {
                lastStart = e.getTimestamp();
            } else if ("session_end".equalsIgnoreCase(e.getEventType()) && lastStart != null) {
                Duration duration = Duration.between(lastStart, e.getTimestamp());
                totalActiveMinutes += duration.toMillis() / 60000.0;
                lastStart = null;
            } else if ("idle".equalsIgnoreCase(e.getEventType()) && e.getDuration() != null) {
                totalIdleMinutes += e.getDuration();
            }
        }

        double totalTime = totalActiveMinutes + totalIdleMinutes;
        double idlePercentage = totalTime == 0 ? 0 : (totalIdleMinutes / totalTime) * 100;

        // Collect feature usage (ignore session/idle events)
        Map<String, Long> featureCounts = events.stream()
                .filter(e -> e.getEventType() != null)
                .filter(e -> {
                    String type = e.getEventType().toLowerCase();
                    return !(type.equals("session_start") || type.equals("session_end") || type.equals("idle"));
                })
                .collect(Collectors.groupingBy(Event::getEventType, Collectors.counting()));

        LinkedHashMap<String, Long> sortedFeatures = featureCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .limit(20)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        String mostUsedFeature = sortedFeatures.keySet().stream().findFirst().orElse(null);

        MetricsSummary summary = new MetricsSummary();
        summary.setProduct(product);
        summary.setTotalActiveMinutes(totalActiveMinutes);
        summary.setTotalIdleMinutes(totalIdleMinutes);
        summary.setIdlePercentage(Math.round(idlePercentage * 100.0) / 100.0);
        summary.setTopFeatures(sortedFeatures);
        summary.setMostUsedFeature(mostUsedFeature);
        return summary;
    }
}
