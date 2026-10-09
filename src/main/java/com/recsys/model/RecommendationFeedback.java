package com.recsys.model;

/**
 * Recommendation feedback entity (click / conversion).
 */
public class RecommendationFeedback {
    private Long id;
    private Long recommendationId;
    private java.time.LocalDateTime shownAt;
    private Boolean clicked;
    private Boolean converted;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRecommendationId() { return recommendationId; }
    public void setRecommendationId(Long recommendationId) { this.recommendationId = recommendationId; }
    public java.time.LocalDateTime getShownAt() { return shownAt; }
    public void setShownAt(java.time.LocalDateTime shownAt) { this.shownAt = shownAt; }
    public Boolean getClicked() { return clicked; }
    public void setClicked(Boolean clicked) { this.clicked = clicked; }
    public Boolean getConverted() { return converted; }
    public void setConverted(Boolean converted) { this.converted = converted; }
}
