package com.moviebooking.repository;

import com.moviebooking.entity.Notification;
import com.moviebooking.entity.User;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserIdAndIsReadFalse(Integer userId);
    List<Notification> findByUserId(Integer userId);

}