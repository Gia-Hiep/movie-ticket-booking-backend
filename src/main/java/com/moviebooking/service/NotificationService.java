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

        try {
            firebaseService.sendPushNotification(notificationDTO.getUserId(), notificationDTO.getTitle(), notificationDTO.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to send push notification: " + e.getMessage());
        }

        return mapToDTO(notification);
    }

    public void sendBroadcastNotification(NotificationDTO notificationDTO) {
        List<User> targetUsers;

        if ("allUsers".equals(notificationDTO.getTarget())) {
            targetUsers = userRepository.findAll();
        } else if ("membershipLevel".equals(notificationDTO.getTarget())) {
            targetUsers = userRepository.findByMembershipLevelInAndIsActiveTrue(notificationDTO.getTargetValues());
        } else if ("role".equals(notificationDTO.getTarget())) {
            targetUsers = userRepository.findByRoleInAndIsActiveTrue(notificationDTO.getTargetValues());
        } else {
            throw new IllegalArgumentException("Invalid target: " + notificationDTO.getTarget());
        }

        for (User user : targetUsers) {
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setTitle(notificationDTO.getTitle());
            notification.setMessage(notificationDTO.getMessage());
            notification.setType(Notification.Type.valueOf(notificationDTO.getType()));
            notificationRepository.save(notification);

            try {
                firebaseService.sendPushNotification(user.getId(), notificationDTO.getTitle(), notificationDTO.getMessage());
            } catch (Exception e) {
                System.err.println("Failed to send push notification for user " + user.getId() + ": " + e.getMessage());
            }
        }
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