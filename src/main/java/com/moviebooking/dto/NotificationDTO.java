package com.moviebooking.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NotificationDTO {
    private Integer id;
    private Integer userId; // Dùng cho thông báo cá nhân
    private String title;
    private String message;
    private String type;
    private boolean isRead;
    private LocalDateTime createdAt;
    private String target; // allUsers, membershipLevel, role
    private List<String> targetValues; // Giá trị cụ thể (ví dụ: ["STANDARD", "VIP"] hoặc ["USER"])
}