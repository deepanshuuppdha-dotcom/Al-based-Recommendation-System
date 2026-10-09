package com.recsys.model;

import java.time.LocalDateTime;

/**
 * Interaction entity representing a user action on a product.
 */
public class Interaction {
    private Long id;
    private Long userId;
    private Long productId;
    private String type; // VIEW, LIKE, CLICK, PURCHASE, DISLIKE
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
