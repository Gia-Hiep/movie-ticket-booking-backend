package com.moviebooking.service;

import com.moviebooking.dto.LoyaltyPointDTO;
import com.moviebooking.entity.LoyaltyPoint;
import com.moviebooking.entity.User;
import com.moviebooking.repository.LoyaltyPointRepository;
import com.moviebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoyaltyPointService {
    private final LoyaltyPointRepository loyaltyPointRepository;
    private final UserRepository userRepository;

    public List<LoyaltyPointDTO> getPointsByUserId(Integer userId) {
        return loyaltyPointRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public LoyaltyPointDTO addPoints(LoyaltyPointDTO pointDTO) {
        User user = userRepository.findById(pointDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + pointDTO.getUserId()));
        LoyaltyPoint point = new LoyaltyPoint();
        point.setUser(user);
        point.setPoints(pointDTO.getPoints());
        point.setTransactionType(LoyaltyPoint.TransactionType.valueOf(pointDTO.getTransactionType()));
        point.setTransactionDetails(pointDTO.getTransactionDetails());
        point = loyaltyPointRepository.save(point);
        return mapToDTO(point);
    }

    private LoyaltyPointDTO mapToDTO(LoyaltyPoint point) {
        LoyaltyPointDTO dto = new LoyaltyPointDTO();
        dto.setId(point.getId());
        dto.setUserId(point.getUser().getId());
        dto.setPoints(point.getPoints());
        dto.setEarnedDate(point.getEarnedDate());
        dto.setUsedDate(point.getUsedDate());
        dto.setTransactionType(point.getTransactionType().name());
        dto.setTransactionDetails(point.getTransactionDetails());
        return dto;
    }
}