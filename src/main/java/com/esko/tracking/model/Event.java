package com.esko.tracking.model;

import jakarta.persistence.*;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String product;
    private String action;
    private Long duration;   // stored in milliseconds
    private Long timestamp;  // epoch millis

    public Event() {}

    // getters & setters
    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public Long getDuration() { return duration; }
    public void setDuration(Long duration) { this.duration = duration; }
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
}
