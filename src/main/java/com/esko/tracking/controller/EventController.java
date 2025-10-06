package com.esko.tracking.controller;

import com.esko.tracking.model.Event;
import com.esko.tracking.repository.EventRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {

    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        if (event.getTimestamp() == null)
            event.setTimestamp(LocalDateTime.now());
        return eventRepository.save(event);
    }

    @GetMapping
    public List<Event> getAll() {
        return eventRepository.findAll();
    }
}
