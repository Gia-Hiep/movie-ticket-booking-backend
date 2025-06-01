package com.moviebooking.service;

import com.moviebooking.dto.PromotionDTO;
import com.moviebooking.entity.Promotion;
import com.moviebooking.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final PromotionRepository promotionRepository;

    public List<PromotionDTO> getActivePromotions() {
        return promotionRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    public PromotionDTO updatePromotion(PromotionDTO promotionDTO) {
        Promotion promotion = promotionRepository.findById(promotionDTO.getId())
                .orElseThrow(() -> new RuntimeException("Promotion not found with id: " + promotionDTO.getId()));
        mapToEntity(promotionDTO, promotion);
        promotion = promotionRepository.save(promotion);
        return mapToDTO(promotion);
    }
    public void deletePromotion(Integer id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found with id: " + id));
        promotionRepository.delete(promotion);
    }
    public PromotionDTO applyPromotion(String code) {
        Promotion promotion = promotionRepository.findByCodeAndIsActiveTrue(code)
                .orElseThrow(() -> new RuntimeException("Invalid promotion code"));

        // Kiểm tra usageCount và usageLimit
        Integer usageCount = promotion.getUsageCount() != null ? promotion.getUsageCount() : 0;
        Integer usageLimit = promotion.getUsageLimit() != null ? promotion.getUsageLimit() : Integer.MAX_VALUE;

        if (usageCount >= usageLimit) {
            throw new RuntimeException("Promotion usage limit reached");
        }

        promotion.setUsageCount(usageCount + 1);
        promotion = promotionRepository.save(promotion);
        return mapToDTO(promotion);
    }

    public PromotionDTO createPromotion(PromotionDTO promotionDTO) {
        Promotion promotion = new Promotion();
        mapToEntity(promotionDTO, promotion);
        // Đảm bảo usageCount không null
        promotion.setUsageCount(promotionDTO.getUsageCount() != null ? promotionDTO.getUsageCount() : 0);
        promotion = promotionRepository.save(promotion);
        return mapToDTO(promotion);
    }

    private PromotionDTO mapToDTO(Promotion promotion) {
        PromotionDTO dto = new PromotionDTO();
        dto.setId(promotion.getId());
        dto.setName(promotion.getName());
        dto.setDescription(promotion.getDescription());
        dto.setDiscountType(promotion.getDiscountType().name());
        dto.setDiscountValue(promotion.getDiscountValue());
        dto.setCode(promotion.getCode());
        dto.setStartDate(promotion.getStartDate());
        dto.setEndDate(promotion.getEndDate());
        dto.setMinPurchase(promotion.getMinPurchase());
        dto.setMaxDiscount(promotion.getMaxDiscount());
        dto.setUsageLimit(promotion.getUsageLimit());
        dto.setUsageCount(promotion.getUsageCount());
        dto.setActive(promotion.isActive());
        return dto;
    }

    private void mapToEntity(PromotionDTO dto, Promotion promotion) {
        promotion.setName(dto.getName());
        promotion.setDescription(dto.getDescription());
        promotion.setDiscountType(Promotion.DiscountType.valueOf(dto.getDiscountType()));
        promotion.setDiscountValue(dto.getDiscountValue());
        promotion.setCode(dto.getCode());
        promotion.setStartDate(dto.getStartDate());
        promotion.setEndDate(dto.getEndDate());
        promotion.setMinPurchase(dto.getMinPurchase());
        promotion.setMaxDiscount(dto.getMaxDiscount());
        promotion.setUsageLimit(dto.getUsageLimit());
        promotion.setUsageCount(dto.getUsageCount());
        promotion.setActive(dto.getIsActive());
    }
}
