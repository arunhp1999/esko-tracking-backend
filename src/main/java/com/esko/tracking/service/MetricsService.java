package com.esko.tracking.service;

import com.esko.tracking.dto.MetricsSummary;
import com.esko.tracking.model.Event;
import com.esko.tracking.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MetricsService {

    private final EventRepository eventRepository;

    public MetricsService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public MetricsSummary computeSummary(String product, Long startMillis, Long endMillis) {
        List<Event> events = eventRepository.findByProductOrderByTimestampAsc(product);

        double totalActiveMinutes = 0.0;
        double totalIdleMinutes = 0.0;
        LocalDateTime lastSessionStart = null;

        for (Event e : events) {
            switch (e.getEventType().toLowerCase()) {
                case "session_start" -> lastSessionStart = e.getTimestamp();
                case "session_end" -> {
                    if (lastSessionStart != null) {
                        Duration duration = Duration.between(lastSessionStart, e.getTimestamp());
                        totalActiveMinutes += duration.toMillis() / 60000.0;
                        lastSessionStart = null;
                    }
                }
                case "idle" -> totalIdleMinutes += e.getDuration() != null ? e.getDuration() : 0.0;
            }
        }

        double totalTime = totalActiveMinutes + totalIdleMinutes;
        double idlePercentage = totalTime == 0 ? 0.0 : (totalIdleMinutes / totalTime) * 100.0;

        MetricsSummary summary = new MetricsSummary();
        summary.setProduct(product);
        summary.setTotalActiveMinutes(totalActiveMinutes);
        summary.setTotalIdleMinutes(totalIdleMinutes);
        summary.setIdlePercentage(Math.round(idlePercentage * 100.0) / 100.0);
        summary.setMostUsedFeature(null);
        summary.setTopFeatures(Collections.emptyMap());
        return summary;
    }
}
