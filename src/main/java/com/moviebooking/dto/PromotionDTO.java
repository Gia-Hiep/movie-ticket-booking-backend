package com.moviebooking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PromotionDTO {
    private Integer id;
    private String name;
    private String description;
    private String discountType;
    private Double discountValue;
    private String code;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double minPurchase;
    private Double maxDiscount;
    private Integer usageLimit;
    private Integer usageCount;
    private boolean isActive;
}
