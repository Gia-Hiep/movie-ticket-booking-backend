package com.moviebooking.service;

import com.moviebooking.dto.LoyaltyPointDTO;
import com.moviebooking.dto.UserDTO;
import com.moviebooking.entity.User;
import com.moviebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final LoyaltyPointService loyaltyPointService;

    public UserDTO register(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername()) || userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Username or email already exists");
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setFullName(userDTO.getFullName());
        user.setRole(User.Role.valueOf(userDTO.getRole()));
        user.setMembershipLevel(User.MembershipLevel.valueOf(userDTO.getMembershipLevel()));
        user = userRepository.save(user);
        return mapToDTO(user);
    }

    public AuthResponse login(AuthRequest request) {
        if (request.getFirebaseToken() != null) {
            try {
                // Xác minh Firebase token
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(request.getFirebaseToken());
                String email = decodedToken.getEmail();
                User user = userRepository.findByEmail(email).orElse(null);

                if (user == null) {
                    // Tạo tài khoản mới nếu người dùng không tồn tại
                    user = new User();
                    user.setEmail(email);
                    user.setUsername(decodedToken.getName() != null ? decodedToken.getName() : email.split("@")[0]);
                    user.setPassword(passwordEncoder.encode("firebaseUser" + System.currentTimeMillis())); // Mật khẩu ngẫu nhiên
                    user.setRole(User.Role.USER);
                    user.setMembershipLevel(User.MembershipLevel.STANDARD);
                    user.setActive(true);
                    user = userRepository.save(user);
                }

                String token = jwtService.generateToken(user);
                return new AuthResponse(token, mapToDTO(user));
            } catch (Exception e) {
                throw new RuntimeException("Invalid Firebase token: " + e.getMessage());
            }
        } else {
            // Đăng nhập bằng username/password
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid password");
            }
            String token = jwtService.generateToken(user);
            return new AuthResponse(token, mapToDTO(user));
        }
    }

    public UserDTO updateProfile(Integer id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFullName(userDTO.getFullName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setEmail(userDTO.getEmail());
        user = userRepository.save(user);
        return mapToDTO(user);
    }

    public UserDTO updateMembership(Integer id, String membershipLevel) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setMembershipLevel(User.MembershipLevel.valueOf(membershipLevel));
        user = userRepository.save(user);
        return mapToDTO(user);
    }

    public LoyaltyPointDTO addLoyaltyPoints(LoyaltyPointDTO pointDTO) {
        return loyaltyPointService.addPoints(pointDTO);
    }

    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setFullName(user.getFullName());
        dto.setRole(user.getRole().name());
        dto.setMembershipLevel(user.getMembershipLevel().name());
        dto.setActive(user.isActive());
        return dto;
    }

    public static class AuthResponse {
        private String token;
        private UserDTO user;

        public AuthResponse(String token, UserDTO user) {
            this.token = token;
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public UserDTO getUser() {
            return user;
        }

        public void setUser(UserDTO user) {
            this.user = user;
        }
    }

    public static class AuthRequest {
        private String username;
        private String password;
        private String firebaseToken;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getFirebaseToken() { return firebaseToken; }
        public void setFirebaseToken(String firebaseToken) { this.firebaseToken = firebaseToken; }
    }
}