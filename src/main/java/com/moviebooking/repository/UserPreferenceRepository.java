package com.moviebooking.repository;

import com.moviebooking.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Integer> {
    List<UserPreference> findByUserId(Integer userId);
    Optional<UserPreference> findByUserIdAndGenreId(Integer userId, Integer genreId);
}