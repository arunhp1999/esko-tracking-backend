package com.esko.tracking.dto;

import java.util.Map;

public class MetricsSummary {
    private String product;
    private double totalActiveMinutes;
    private double totalIdleMinutes;
    private double idlePercentage;
    private String mostUsedFeature;
    private Map<String, Long> topFeatures;

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public double getTotalActiveMinutes() { return totalActiveMinutes; }
    public void setTotalActiveMinutes(double totalActiveMinutes) { this.totalActiveMinutes = totalActiveMinutes; }

    public double getTotalIdleMinutes() { return totalIdleMinutes; }
    public void setTotalIdleMinutes(double totalIdleMinutes) { this.totalIdleMinutes = totalIdleMinutes; }

    public double getIdlePercentage() { return idlePercentage; }
    public void setIdlePercentage(double idlePercentage) { this.idlePercentage = idlePercentage; }

    public String getMostUsedFeature() { return mostUsedFeature; }
    public void setMostUsedFeature(String mostUsedFeature) { this.mostUsedFeature = mostUsedFeature; }

    public Map<String, Long> getTopFeatures() { return topFeatures; }
    public void setTopFeatures(Map<String, Long> topFeatures) { this.topFeatures = topFeatures; }
}
