package com.esko.tracking.controller;

import com.esko.tracking.model.Event;
import com.esko.tracking.repository.EventRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {

    private final EventRepository repo;

    public EventController(EventRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Event saveEvent(@RequestBody Event event) {
        return repo.save(event);
    }

    @GetMapping
    public List<Event> getEvents() {
        return repo.findAll();
    }
}
