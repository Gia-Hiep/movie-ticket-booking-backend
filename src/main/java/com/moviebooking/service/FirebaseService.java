package com.moviebooking.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.moviebooking.entity.User;
import com.moviebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FirebaseService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;

    public void sendPushNotification(Integer userId, String title, String message) throws FirebaseMessagingException {
        String deviceToken = getDeviceTokenForUser(userId);
        if (deviceToken == null || deviceToken.isEmpty()) {
            throw new RuntimeException("No device token found for user with id: " + userId);
        }

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(message)
                .build();

        Message fcmMessage = Message.builder()
                .setToken(deviceToken)
                .setNotification(notification)
                .build();

        firebaseMessaging.send(fcmMessage);
    }

    private String getDeviceTokenForUser(Integer userId) {
        // Lấy deviceToken từ User trong cơ sở dữ liệu
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return user.getDeviceToken(); // Giả sử User có trường deviceToken
    }
}