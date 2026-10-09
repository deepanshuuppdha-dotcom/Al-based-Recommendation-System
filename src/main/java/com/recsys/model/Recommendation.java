package com.recsys.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Recommendation entity.
 */
public class Recommendation {
    private Long id;
    private Long userId;
    private Long productId;
    private BigDecimal score;
    private String algorithm; // HYBRID, CONTENT, COLLAB, POPULARITY
    private LocalDateTime generatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public BigDecimal getScore() { return score; }
    public void setScore(BigDecimal score) { this.score = score; }
    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
}
