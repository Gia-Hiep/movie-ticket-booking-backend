# Đường dẫn đến thư mục dto
$DTO_DIR = "src/main/java/com.moviebooking/dto"

# Tạo thư mục nếu chưa tồn tại
if (-not (Test-Path $DTO_DIR)) {
    New-Item -Path $DTO_DIR -ItemType Directory -Force
}

# Nội dung các file DTO
$DTO_CONTENTS = @{
    "UserDTO.java" = @"
package com.moviebooking.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String email;
    private String phoneNumber;
    private String fullName;
    private String role;
    private String membershipLevel;
    private boolean isActive;
}
"@

    "MovieDTO.java" = @"
package com.moviebooking.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MovieDTO {
    private Integer id;
    private String title;
    private String description;
    private String posterUrl;
    private String trailerUrl;
    private Integer duration;
    private LocalDate releaseDate;
    private String director;
    private String cast;
    private Float rating;
    private boolean isActive;
}
"@

    "GenreDTO.java" = @"
package com.moviebooking.dto;

import lombok.Data;

@Data
public class GenreDTO {
    private Integer id;
    private String name;
    private String description;
}
"@

    "MovieGenreDTO.java" = @"
package com.moviebooking.dto;

import lombok.Data;

@Data
public class MovieGenreDTO {
    private Integer id;
    private Integer movieId;
    private Integer genreId;
}
"@

    "CinemaDTO.java" = @"
package com.moviebooking.dto;

import lombok.Data;

@Data
public class CinemaDTO {
    private Integer id;
    private String name;
    private String address;
    private String city;
    private String phone;
    private String description;
    private String imageUrl;
    private boolean isActive;
}
"@

    "CinemaRoomDTO.java" = @"
package com.moviebooking.dto;

import lombok.Data;

@Data
public class CinemaRoomDTO {
    private Integer id;
    private Integer cinemaId;
    private String name;
    private Integer capacity;
    private String roomType;
    private boolean isActive;
}
"@

    "ShowtimeDTO.java" = @"
package com.moviebooking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShowtimeDTO {
    private Integer id;
    private Integer movieId;
    private Integer cinemaRoomId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double ticketPrice;
    private String format;
    private String language;
    private boolean isActive;
}
"@

    "SeatDTO.java" = @"
package com.moviebooking.dto;

import lombok.Data;

@Data
public class SeatDTO {
    private Integer id;
    private Integer cinemaRoomId;
    private String seatNumber;
    private String seatRow;
    private String seatType;
    private Double priceMultiplier;
    private boolean isActive;
}
"@

    "TicketDTO.java" = @"
package com.moviebooking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketDTO {
    private Integer id;
    private Integer userId;
    private Integer showtimeId;
    private String qrCode;
    private Double totalAmount;
    private String status;
    private LocalDateTime purchaseTime;
}
"@

    "TicketSeatDTO.java" = @"
package com.moviebooking.dto;

import lombok.Data;

@Data
public class TicketSeatDTO {
    private Integer id;
    private Integer ticketId;
    private Integer seatId;
    private Double price;
}
"@

    "PaymentDTO.java" = @"
package com.moviebooking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Integer id;
    private Integer ticketId;
    private Double amount;
    private String paymentMethod;
    private String transactionId;
    private String status;
    private LocalDateTime paymentTime;
    private String paymentDetails;
}
"@

    "ReviewDTO.java" = @"
package com.moviebooking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private Integer id;
    private Integer userId;
    private Integer movieId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
"@

    "PromotionDTO.java" = @"
package com.moviebooking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PromotionDTO {
    private Integer id;
    private String name;
    private String description;
    private String discountType;
    private Double discountValue;
    private String code;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double minPurchase;
    private Double maxDiscount;
    private Integer usageLimit;
    private Integer usageCount;
    private boolean isActive;
}
"@

    "StatisticsDTO.java" = @"
package com.moviebooking.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StatisticsDTO {
    private Integer id;
    private Integer showtimeId;
    private Integer movieId;
    private Integer cinemaId;
    private Double revenue;
    private Integer ticketCount;
    private LocalDate statsDate;
}
"@

    "UserPreferenceDTO.java" = @"
package com.moviebooking.dto;

import lombok.Data;

@Data
public class UserPreferenceDTO {
    private Integer id;
    private Integer userId;
    private Integer genreId;
    private Integer preferenceLevel;
}
"@

    "NotificationDTO.java" = @"
package com.moviebooking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Integer id;
    private Integer userId;
    private String title;
    private String message;
    private String type;
    private boolean isRead;
    private LocalDateTime createdAt;
}
"@

    "LoyaltyPointDTO.java" = @"
package com.moviebooking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoyaltyPointDTO {
    private Integer id;
    private Integer userId;
    private Integer points;
    private LocalDateTime earnedDate;
    private LocalDateTime usedDate;
    private String transactionType;
    private String transactionDetails;
}
"@

    "FoodItemDTO.java" = @"
package com.moviebooking.dto;

import lombok.Data;

@Data
public class FoodItemDTO {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private String category;
    private boolean isActive;
}
"@

    "FoodOrderDTO.java" = @"
package com.moviebooking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FoodOrderDTO {
    private Integer id;
    private Integer ticketId;
    private Integer foodItemId;
    private Integer quantity;
    private Double price;
    private LocalDateTime orderTime;
}
"@
}

# Tạo các file DTO
foreach ($file_name in $DTO_CONTENTS.Keys) {
    Write-Host "Creating $DTO_DIR/$file_name..."
    $DTO_CONTENTS[$file_name] | Out-File -FilePath "$DTO_DIR/$file_name" -Encoding UTF8
}

Write-Host "All DTO files have been created in $DTO_DIR"