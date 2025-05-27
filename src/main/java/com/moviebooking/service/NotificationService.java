package com.moviebooking.service;

import com.moviebooking.dto.NotificationDTO;
import com.moviebooking.entity.Notification;
import com.moviebooking.entity.User;
import com.moviebooking.repository.NotificationRepository;
import com.moviebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final FirebaseService firebaseService;

    public List<NotificationDTO> getUnreadNotifications(Integer userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void markAsRead(Integer id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        User user = userRepository.findById(notificationDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + notificationDTO.getUserId()));
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(notificationDTO.getTitle());
        notification.setMessage(notificationDTO.getMessage());
        notification.setType(Notification.Type.valueOf(notificationDTO.getType()));
        notification = notificationRepository.save(notification);

        // Gửi thông báo đẩy qua Firebase
        try {
            firebaseService.sendPushNotification(notificationDTO.getUserId(), notificationDTO.getTitle(), notificationDTO.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to send push notification: " + e.getMessage());
        }

        return mapToDTO(notification);
    }

    private NotificationDTO mapToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUser() != null ? notification.getUser().getId() : null);
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType().name());
        dto.setRead(notification.isRead());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }
}