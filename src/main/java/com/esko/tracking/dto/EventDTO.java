package com.esko.tracking.dto;

public class EventDTO {
    private String userId;
    private String product;
    private String action;
    private long timestamp;
    private long duration;
    
    // Getters and setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }
}
