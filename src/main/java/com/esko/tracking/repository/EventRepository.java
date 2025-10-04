package com.esko.tracking.repository;

import com.esko.tracking.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByProduct(String product);
    List<Event> findByProductAndTimestampBetween(String product, Long startMillis, Long endMillis);
}
