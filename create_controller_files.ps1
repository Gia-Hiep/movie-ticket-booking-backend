# Đường dẫn đến thư mục controller
$CONTROLLER_DIR = "src/main/java/com/moviebooking/controller"

# Kiểm tra quyền truy cập thư mục gốc
try {
    $PROJECT_DIR = Get-Location
    Write-Host "Current project directory: $PROJECT_DIR"
    
    # Tạo thư mục nếu chưa tồn tại
    if (-not (Test-Path $CONTROLLER_DIR)) {
        New-Item -Path $CONTROLLER_DIR -ItemType Directory -Force
        Write-Host "Created directory: $CONTROLLER_DIR"
    } else {
        Write-Host "Directory already exists: $CONTROLLER_DIR"
    }
} catch {
    Write-Host "Error accessing or creating directory: $_"
    exit 1
}

# Nội dung các file Controller
$CONTROLLER_CONTENTS = @{
    "UserController.java" = @"
package com.moviebooking.controller;

import com.moviebooking.dto.LoyaltyPointDTO;
import com.moviebooking.dto.UserDTO;
import com.moviebooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.register(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.login(userDTO));
    }

    @PostMapping("/2fa")
    public ResponseEntity<String> verify2FA(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.verify2FA(userDTO));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDTO> updateProfile(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateProfile(userDTO));
    }

    @PutMapping("/{id}/membership")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateMembership(@PathVariable Integer id, @RequestBody String membershipLevel) {
        return ResponseEntity.ok(userService.updateMembership(id, membershipLevel));
    }

    @PostMapping("/loyalty-points")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> addLoyaltyPoints(@RequestBody LoyaltyPointDTO pointDTO) {
        userService.addLoyaltyPoints(pointDTO);
        return ResponseEntity.ok().build();
    }
}
"@

    "MovieController.java" = @"
package com.moviebooking.controller;

import com.moviebooking.dto.MovieDTO;
import com.moviebooking.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Integer id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieDTO>> searchMovies(@RequestParam String query) {
        return ResponseEntity.ok(movieService.searchMovies(query));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieDTO> createMovie(@RequestBody MovieDTO movieDTO) {
        return ResponseEntity.ok(movieService.createMovie(movieDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable Integer id, @RequestBody MovieDTO movieDTO) {
        return ResponseEntity.ok(movieService.updateMovie(id, movieDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMovie(@PathVariable Integer id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok().build();
    }
}
"@

    "GenreController.java" = @"
package com.moviebooking.controller;

import com.moviebooking.dto.GenreDTO;
import com.moviebooking.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public ResponseEntity<List<GenreDTO>> getAllGenres() {
        return ResponseEntity.ok(genreService.getAllGenres());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDTO> getGenreById(@PathVariable Integer id) {
        return ResponseEntity.ok(genreService.getGenreById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenreDTO> createGenre(@RequestBody GenreDTO genreDTO) {
        return ResponseEntity.ok(genreService.createGenre(genreDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenreDTO> updateGenre(@PathVariable Integer id, @RequestBody GenreDTO genreDTO) {
        return ResponseEntity.ok(genreService.updateGenre(id, genreDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteGenre(@PathVariable Integer id) {
        genreService.deleteGenre(id);
        return ResponseEntity.ok().build();
    }
}
"@

    "MovieGenreController.java" = @"
package com.moviebooking.controller;

import com.moviebooking.dto.MovieGenreDTO;
import com.moviebooking.service.MovieGenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie-genres")
@RequiredArgsConstructor
public class MovieGenreController {
    private final MovieGenreService movieGenreService;

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<MovieGenreDTO>> getGenresByMovieId(@PathVariable Integer movieId) {
        return ResponseEntity.ok(movieGenreService.getGenresByMovieId(movieId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieGenreDTO> createMovieGenre(@RequestBody MovieGenreDTO movieGenreDTO) {
        return ResponseEntity.ok(movieGenreService.createMovieGenre(movieGenreDTO));
    }
}
"@

    "CinemaController.java" = @"
package com.moviebooking.controller;

import com.moviebooking.dto.CinemaDTO;
import com.moviebooking.service.CinemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cinemas")
@RequiredArgsConstructor
public class CinemaController {
    private final CinemaService cinemaService;

    @GetMapping
    public ResponseEntity<List<CinemaDTO>> getAllCinemas() {
        return ResponseEntity.ok(cinemaService.getAllCinemas());
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<CinemaDTO>> getCinemasByCity(@PathVariable String city) {
        return ResponseEntity.ok(cinemaService.getCinemasByCity(city));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CinemaDTO> createCinema(@RequestBody CinemaDTO cinemaDTO) {
        return ResponseEntity.ok(cinemaService.createCinema(cinemaDTO));
    }
}
"@

    "CinemaRoomController.java" = @"
package com.moviebooking.controller;

import com.moviebooking.dto.CinemaRoomDTO;
import com.moviebooking.service.CinemaRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cinema-rooms")
@RequiredArgsConstructor
public class CinemaRoomController {
    private final CinemaRoomService cinemaRoomService;

    @GetMapping("/cinema/{cinemaId}")
    public ResponseEntity<List<CinemaRoomDTO>> getRoomsByCinemaId(@PathVariable Integer cinemaId) {
        return ResponseEntity.ok(cinemaRoomService.getRoomsByCinemaId(cinemaId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CinemaRoomDTO> createCinemaRoom(@RequestBody CinemaRoomDTO roomDTO) {
        return ResponseEntity.ok(cinemaRoomService.createCinemaRoom(roomDTO));
    }
}
"@

    "ShowtimeController.java" = @"
package com.moviebooking.controller;

import com.moviebooking.dto.ShowtimeDTO;
import com.moviebooking.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/showtimes")
@RequiredArgsConstructor
public class ShowtimeController {
    private final ShowtimeService showtimeService;

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ShowtimeDTO>> getShowtimesByMovieId(@PathVariable Integer movieId) {
        return ResponseEntity.ok(showtimeService.getShowtimesByMovieId(movieId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShowtimeDTO> createShowtime(@RequestBody ShowtimeDTO showtimeDTO) {
        return ResponseEntity.ok(showtimeService.createShowtime(showtimeDTO));
    }
}
"@

    "SeatController.java" = @"
package com.moviebooking.controller;

import com.moviebooking.dto.SeatDTO;
import com.moviebooking.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @GetMapping("/cinema-room/{cinemaRoomId}")
    public ResponseEntity<List<SeatDTO>> getSeatsByCinemaRoomId(@PathVariable Integer cinemaRoomId) {
        return ResponseEntity.ok(seatService.getSeatsByCinemaRoomId(cinemaRoomId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SeatDTO> createSeat(@RequestBody SeatDTO seatDTO) {
        return ResponseEntity.ok(seatService.createSeat(seatDTO));
    }
}
"@

    "TicketController.java" = @"
package com.moviebooking.controller;

import com.moviebooking.dto.TicketDTO;
import com.moviebooking.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TicketDTO> bookTicket(@RequestBody TicketDTO ticketDTO) {
        return ResponseEntity.ok(ticketService.bookTicket(ticketDTO));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TicketDTO>> getUserTickets(@PathVariable Integer userId) {
        return ResponseEntity.ok(ticketService.getUserTickets(userId));
    }
}
"@

    "TicketSeatController.java" = @"
package com.moviebooking.controller;

import com.moviebooking.dto.TicketSeatDTO;
import com.moviebooking.service.TicketSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ticket-seats")
@RequiredArgsConstructor
public class TicketSeatController {
    private final TicketSeatService ticketSeatService;

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<TicketSeatDTO>> getSeatsByTicketId(@PathVariable Integer ticketId) {
        return ResponseEntity.ok(ticketSeatService.getSeatsByTicketId(ticketId));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TicketSeatDTO> createTicketSeat(@RequestBody TicketSeatDTO ticketSeatDTO) {
        return ResponseEntity.ok(ticketSeatService.createTicketSeat(ticketSeatDTO));
    }
}
"@

    "PaymentController.java" = @"
package com.moviebooking.controller;

import com.moviebooking.dto.PaymentDTO;
import com.moviebooking.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PaymentDTO> processPayment(@RequestBody PaymentDTO paymentDTO) {
        return ResponseEntity.ok(paymentService.processPayment(paymentDTO));
    }
}
"@

    "ReviewController.java" = @"
package com.moviebooking.controller;

import com.moviebooking.dto.ReviewDTO;
import com.moviebooking.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByMovieId(@PathVariable Integer movieId) {
        return ResponseEntity.ok(reviewService.getReviewsByMovieId(movieId));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(reviewService.createReview(reviewDTO));
    }
}
"@

    "PromotionController.java" = @"
package com.moviebooking.controller;

import com.moviebooking.dto.PromotionDTO;
import com.moviebooking.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;

    @GetMapping("/apply/{code}")
    public ResponseEntity<PromotionDTO> applyPromotion(@PathVariable String code) {
        return ResponseEntity.ok(promotionService.applyPromotion(code));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PromotionDTO> createPromotion(@RequestBody PromotionDTO promotionDTO) {
        return ResponseEntity.ok(promotionService.createPromotion(promotionDTO));
    }
}
"@

    "StatisticsController.java" = @"
package com.moviebooking.controller;

import com.moviebooking.dto.StatisticsDTO;
import com.moviebooking.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StatisticsDTO>> getStatisticsByDateRange(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return ResponseEntity.ok(statisticsService.getStatisticsByDateRange(startDate, endDate));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatisticsDTO> createStatistics(@RequestBody StatisticsDTO statisticsDTO) {
        return ResponseEntity.ok(statisticsService.createStatistics(statisticsDTO));
    }
}
"@

    "UserPreferenceController.java" = @"
package com.moviebooking.controller;

import com.moviebooking.dto.UserPreferenceDTO;
import com.moviebooking.service.UserPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-preferences")
@RequiredArgsConstructor
public class UserPreferenceController {
    private final UserPreferenceService userPreferenceService;

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UserPreferenceDTO>> getPreferencesByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(userPreferenceService.getPreferencesByUserId(userId));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserPreferenceDTO> createPreference(@RequestBody UserPreferenceDTO preferenceDTO) {
        return ResponseEntity.ok(userPreferenceService.createPreference(preferenceDTO));
    }
}
"@

    "NotificationController.java" = @"
package com.moviebooking.controller;

import com.moviebooking.dto.NotificationDTO;
import com.moviebooking.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/user/{userId}/unread")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications(@PathVariable Integer userId) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody NotificationDTO notificationDTO) {
        return ResponseEntity.ok(notificationService.createNotification(notificationDTO));
    }
}
"@

    "LoyaltyPointController.java" = @"
package com.moviebooking.controller;

import com.moviebooking.dto.LoyaltyPointDTO;
import com.moviebooking.service.LoyaltyPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loyalty-points")
@RequiredArgsConstructor
public class LoyaltyPointController {
    private final LoyaltyPointService loyaltyPointService;

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<LoyaltyPointDTO>> getPointsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(loyaltyPointService.getPointsByUserId(userId));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LoyaltyPointDTO> addPoints(@RequestBody LoyaltyPointDTO pointDTO) {
        return ResponseEntity.ok(loyaltyPointService.addPoints(pointDTO));
    }
}
"@

    "FoodItemController.java" = @"
package com.moviebooking.controller;

import com.moviebooking.dto.FoodItemDTO;
import com.moviebooking.service.FoodItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food-items")
@RequiredArgsConstructor
public class FoodItemController {
    private final FoodItemService foodItemService;

    @GetMapping("/category/{category}")
    public ResponseEntity<List<FoodItemDTO>> getFoodItemsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(foodItemService.getFoodItemsByCategory(category));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FoodItemDTO> createFoodItem(@RequestBody FoodItemDTO foodItemDTO) {
        return ResponseEntity.ok(foodItemService.createFoodItem(foodItemDTO));
    }
}
"@

    "FoodOrderController.java" = @"
package com.moviebooking.controller;

import com.moviebooking.dto.FoodOrderDTO;
import com.moviebooking.service.FoodOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food-orders")
@RequiredArgsConstructor
public class FoodOrderController {
    private final FoodOrderService foodOrderService;

    @GetMapping("/ticket/{ticketId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<FoodOrderDTO>> getOrdersByTicketId(@PathVariable Integer ticketId) {
        return ResponseEntity.ok(foodOrderService.getOrdersByTicketId(ticketId));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<FoodOrderDTO> createFoodOrder(@RequestBody FoodOrderDTO foodOrderDTO) {
        return ResponseEntity.ok(foodOrderService.createFoodOrder(foodOrderDTO));
    }
}
"@
}

# Tạo các file Controller
foreach ($file_name in $CONTROLLER_CONTENTS.Keys) {
    try {
        $file_path = Join-Path $CONTROLLER_DIR $file_name
        Write-Host "Creating $file_path..."
        $CONTROLLER_CONTENTS[$file_name] | Out-File -FilePath $file_path -Encoding UTF8
        Write-Host "Created $file_path"
    } catch {
        Write-Host "Error creating $file_name : $_"
    }
}

Write-Host "All Controller files have been created in $CONTROLLER_DIR"