package com.moviebooking.service;

import com.moviebooking.dto.UserPreferenceDTO;
import com.moviebooking.entity.UserPreference;
import com.moviebooking.entity.User;
import com.moviebooking.entity.Genre;
import com.moviebooking.repository.UserPreferenceRepository;
import com.moviebooking.repository.UserRepository;
import com.moviebooking.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPreferenceService {
    private final UserPreferenceRepository userPreferenceRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;

    public List<UserPreferenceDTO> getPreferencesByUserId(Integer userId) {
        return userPreferenceRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public UserPreferenceDTO createPreference(UserPreferenceDTO preferenceDTO) {
        User user = userRepository.findById(preferenceDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + preferenceDTO.getUserId()));
        Genre genre = genreRepository.findById(preferenceDTO.getGenreId())
                .orElseThrow(() -> new RuntimeException("Genre not found with id: " + preferenceDTO.getGenreId()));
        UserPreference preference = new UserPreference();
        preference.setUser(user);
        preference.setGenre(genre);
        preference.setPreferenceLevel(preferenceDTO.getPreferenceLevel());
        preference = userPreferenceRepository.save(preference);
        return mapToDTO(preference);
    }

    private UserPreferenceDTO mapToDTO(UserPreference preference) {
        UserPreferenceDTO dto = new UserPreferenceDTO();
        dto.setId(preference.getId());
        dto.setUserId(preference.getUser().getId());
        dto.setGenreId(preference.getGenre().getId());
        dto.setPreferenceLevel(preference.getPreferenceLevel());
        return dto;
    }
}