package com.esko.tracking.repository;

import com.esko.tracking.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByProduct(String product);

    List<Event> findByProductAndTimestampBetween(String product, LocalDateTime start, LocalDateTime end);

    List<Event> findByProductOrderByTimestampAsc(String product);
}
