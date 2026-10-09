package com.recsys.model;

/**
 * User preference weight for a specific category (1‑5).
 */
public class UserPreference {
    private Long id;
    private Long userId;
    private Long categoryId;
    private Integer weight;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }
}
