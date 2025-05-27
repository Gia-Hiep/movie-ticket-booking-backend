# Đường dẫn đến thư mục service
$SERVICE_DIR = "src/main/java/com/moviebooking/service"

# Kiểm tra quyền truy cập thư mục gốc
try {
    $PROJECT_DIR = Get-Location
    Write-Host "Current project directory: $PROJECT_DIR"
    
    # Tạo thư mục nếu chưa tồn tại
    if (-not (Test-Path $SERVICE_DIR)) {
        New-Item -Path $SERVICE_DIR -ItemType Directory -Force
        Write-Host "Created directory: $SERVICE_DIR"
    } else {
        Write-Host "Directory already exists: $SERVICE_DIR"
    }
} catch {
    Write-Host "Error accessing or creating directory: $_"
    exit 1
}

# Nội dung các file Service
$SERVICE_CONTENTS = @{
    "UserService.java" = @"
package com.moviebooking.service;

import com.moviebooking.dto.LoyaltyPointDTO;
import com.moviebooking.dto.UserDTO;
import com.moviebooking.entity.User;
import com.moviebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TwilioService twilioService;
    private final SendGridService sendGridService;
    private final FirebaseAuthService firebaseAuthService;

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

    public String login(UserDTO userDTO) {
        User user = userRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return jwtService.generateToken(user);
    }

    public String verify2FA(UserDTO userDTO) {
        String otp = generateOTP();
        twilioService.sendSMS(userDTO.getPhoneNumber(), "Your OTP is: " + otp);
        sendGridService.sendEmail(userDTO.getEmail(), "OTP Verification", "Your OTP is: " + otp);
        // Lưu OTP tạm thời (Redis hoặc DB) để so sánh
        return "OTP sent";
    }

    public UserDTO updateProfile(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId())
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

    public void addLoyaltyPoints(LoyaltyPointDTO pointDTO) {
        // Logic thêm điểm thưởng (sẽ tích hợp với LoyaltyPointService)
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
        dto.setIsActive(user.isActive());
        return dto;
    }

    private String generateOTP() {
        return String.valueOf((int) (Math.random() * 9000) + 1000);
    }
}
"@

    "MovieService.java" = @"
package com.moviebooking.service;

import com.moviebooking.dto.MovieDTO;
import com.moviebooking.entity.Movie;
import com.moviebooking.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public List<MovieDTO> getAllMovies() {
        return movieRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public MovieDTO getMovieById(Integer id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        return mapToDTO(movie);
    }

    public List<MovieDTO> searchMovies(String query) {
        return movieRepository.searchMovies(query)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public MovieDTO createMovie(MovieDTO movieDTO) {
        Movie movie = new Movie();
        mapToEntity(movieDTO, movie);
        movie = movieRepository.save(movie);
        return mapToDTO(movie);
    }

    public MovieDTO updateMovie(Integer id, MovieDTO movieDTO) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        mapToEntity(movieDTO, movie);
        movie = movieRepository.save(movie);
        return mapToDTO(movie);
    }

    public void deleteMovie(Integer id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        movie.setIsActive(false);
        movieRepository.save(movie);
    }

    private MovieDTO mapToDTO(Movie movie) {
        MovieDTO dto = new MovieDTO();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setDescription(movie.getDescription());
        dto.setPosterUrl(movie.getPosterUrl());
        dto.setTrailerUrl(movie.getTrailerUrl());
        dto.setDuration(movie.getDuration());
        dto.setReleaseDate(movie.getReleaseDate());
        dto.setDirector(movie.getDirector());
        dto.setCast(movie.getCast());
        dto.setRating(movie.getRating());
        dto.setIsActive(movie.isActive());
        return dto;
    }

    private void mapToEntity(MovieDTO dto, Movie movie) {
        movie.setTitle(dto.getTitle());
        movie.setDescription(dto.getDescription());
        movie.setPosterUrl(dto.getPosterUrl());
        movie.setTrailerUrl(dto.getTrailerUrl());
        movie.setDuration(dto.getDuration());
        movie.setReleaseDate(dto.getReleaseDate());
        movie.setDirector(dto.getDirector());
        movie.setCast(dto.getCast());
        movie.setRating(dto.getRating());
        movie.setIsActive(dto.isIsActive());
    }
}
"@

    "GenreService.java" = @"
package com.moviebooking.service;

import com.moviebooking.dto.GenreDTO;
import com.moviebooking.entity.Genre;
import com.moviebooking.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public List<GenreDTO> getAllGenres() {
        return genreRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public GenreDTO getGenreById(Integer id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found"));
        return mapToDTO(genre);
    }

    public GenreDTO createGenre(GenreDTO genreDTO) {
        if (genreRepository.existsByName(genreDTO.getName())) {
            throw new RuntimeException("Genre already exists");
        }
        Genre genre = new Genre();
        genre.setName(genreDTO.getName());
        genre.setDescription(genreDTO.getDescription());
        genre = genreRepository.save(genre);
        return mapToDTO(genre);
    }

    public GenreDTO updateGenre(Integer id, GenreDTO genreDTO) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found"));
        genre.setName(genreDTO.getName());
        genre.setDescription(genreDTO.getDescription());
        genre = genreRepository.save(genre);
        return mapToDTO(genre);
    }

    public void deleteGenre(Integer id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found"));
        genreRepository.delete(genre);
    }

    private GenreDTO mapToDTO(Genre genre) {
        GenreDTO dto = new GenreDTO();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        dto.setDescription(genre.getDescription());
        return dto;
    }
}
"@

    "MovieGenreService.java" = @"
package com.moviebooking.service;

import com.moviebooking.dto.MovieGenreDTO;
import com.moviebooking.entity.MovieGenre;
import com.moviebooking.repository.MovieGenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieGenreService {
    private final MovieGenreRepository movieGenreRepository;

    public List<MovieGenreDTO> getGenresByMovieId(Integer movieId) {
        return movieGenreRepository.findByMovieId(movieId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public MovieGenreDTO createMovieGenre(MovieGenreDTO movieGenreDTO) {
        MovieGenre movieGenre = new MovieGenre();
        movieGenre.setMovie(new Movie(movieGenreDTO.getMovieId()));
        movieGenre.setGenre(new Genre(movieGenreDTO.getGenreId()));
        movieGenre = movieGenreRepository.save(movieGenre);
        return mapToDTO(movieGenre);
    }

    private MovieGenreDTO mapToDTO(MovieGenre movieGenre) {
        MovieGenreDTO dto = new MovieGenreDTO();
        dto.setId(movieGenre.getId());
        dto.setMovieId(movieGenre.getMovie().getId());
        dto.setGenreId(movieGenre.getGenre().getId());
        return dto;
    }
}
"@

    "CinemaService.java" = @"
package com.moviebooking.service;

import com.moviebooking.dto.CinemaDTO;
import com.moviebooking.entity.Cinema;
import com.moviebooking.repository.CinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CinemaService {
    private final CinemaRepository cinemaRepository;

    public List<CinemaDTO> getAllCinemas() {
        return cinemaRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<CinemaDTO> getCinemasByCity(String city) {
        return cinemaRepository.findByCityAndIsActiveTrue(city)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CinemaDTO createCinema(CinemaDTO cinemaDTO) {
        Cinema cinema = new Cinema();
        mapToEntity(cinemaDTO, cinema);
        cinema = cinemaRepository.save(cinema);
        return mapToDTO(cinema);
    }

    private CinemaDTO mapToDTO(Cinema cinema) {
        CinemaDTO dto = new CinemaDTO();
        dto.setId(cinema.getId());
        dto.setName(cinema.getName());
        dto.setAddress(cinema.getAddress());
        dto.setCity(cinema.getCity());
        dto.setPhone(cinema.getPhone());
        dto.setDescription(cinema.getDescription());
        dto.setImageUrl(cinema.getImageUrl());
        dto.setIsActive(cinema.isActive());
        return dto;
    }

    private void mapToEntity(CinemaDTO dto, Cinema cinema) {
        cinema.setName(dto.getName());
        cinema.setAddress(dto.getAddress());
        cinema.setCity(dto.getCity());
        cinema.setPhone(dto.getPhone());
        cinema.setDescription(dto.getDescription());
        cinema.setImageUrl(dto.getImageUrl());
        cinema.setIsActive(dto.isIsActive());
    }
}
"@

    "CinemaRoomService.java" = @"
package com.moviebooking.service;

import com.moviebooking.dto.CinemaRoomDTO;
import com.moviebooking.entity.CinemaRoom;
import com.moviebooking.repository.CinemaRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CinemaRoomService {
    private final CinemaRoomRepository cinemaRoomRepository;

    public List<CinemaRoomDTO> getRoomsByCinemaId(Integer cinemaId) {
        return cinemaRoomRepository.findByCinemaIdAndIsActiveTrue(cinemaId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CinemaRoomDTO createCinemaRoom(CinemaRoomDTO roomDTO) {
        CinemaRoom room = new CinemaRoom();
        mapToEntity(roomDTO, room);
        room = cinemaRoomRepository.save(room);
        return mapToDTO(room);
    }

    private CinemaRoomDTO mapToDTO(CinemaRoom room) {
        CinemaRoomDTO dto = new CinemaRoomDTO();
        dto.setId(room.getId());
        dto.setCinemaId(room.getCinema().getId());
        dto.setName(room.getName());
        dto.setCapacity(room.getCapacity());
        dto.setRoomType(room.getRoomType().name());
        dto.setIsActive(room.isActive());
        return dto;
    }

    private void mapToEntity(CinemaRoomDTO dto, CinemaRoom room) {
        room.setCinema(new Cinema(dto.getCinemaId()));
        room.setName(dto.getName());
        room.setCapacity(dto.getCapacity());
        room.setRoomType(CinemaRoom.RoomType.valueOf(dto.getRoomType()));
        room.setIsActive(dto.isIsActive());
    }
}
"@

    "ShowtimeService.java" = @"
package com.moviebooking.service;

import com.moviebooking.dto.ShowtimeDTO;
import com.moviebooking.entity.Showtime;
import com.moviebooking.repository.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowtimeService {
    private final ShowtimeRepository showtimeRepository;

    public List<ShowtimeDTO> getShowtimesByMovieId(Integer movieId) {
        return showtimeRepository.findByMovieIdAndIsActiveTrue(movieId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ShowtimeDTO createShowtime(ShowtimeDTO showtimeDTO) {
        Showtime showtime = new Showtime();
        mapToEntity(showtimeDTO, showtime);
        showtime = showtimeRepository.save(showtime);
        return mapToDTO(showtime);
    }

    private ShowtimeDTO mapToDTO(Showtime showtime) {
        ShowtimeDTO dto = new ShowtimeDTO();
        dto.setId(showtime.getId());
        dto.setMovieId(showtime.getMovie().getId());
        dto.setCinemaRoomId(showtime.getCinemaRoom().getId());
        dto.setStartTime(showtime.getStartTime());
        dto.setEndTime(showtime.getEndTime());
        dto.setTicketPrice(showtime.getTicketPrice());
        dto.setFormat(showtime.getFormat().name());
        dto.setLanguage(showtime.getLanguage());
        dto.setIsActive(showtime.isActive());
        return dto;
    }

    private void mapToEntity(ShowtimeDTO dto, Showtime showtime) {
        showtime.setMovie(new Movie(dto.getMovieId()));
        showtime.setCinemaRoom(new CinemaRoom(dto.getCinemaRoomId()));
        showtime.setStartTime(dto.getStartTime());
        showtime.setEndTime(dto.getEndTime());
        showtime.setTicketPrice(dto.getTicketPrice());
        showtime.setFormat(Showtime.Format.valueOf(dto.getFormat()));
        showtime.setLanguage(dto.getLanguage());
        showtime.setIsActive(dto.isIsActive());
    }
}
"@

    "SeatService.java" = @"
package com.moviebooking.service;

import com.moviebooking.dto.SeatDTO;
import com.moviebooking.entity.Seat;
import com.moviebooking.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;

    public List<SeatDTO> getSeatsByCinemaRoomId(Integer cinemaRoomId) {
        return seatRepository.findByCinemaRoomIdAndIsActiveTrue(cinemaRoomId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public SeatDTO createSeat(SeatDTO seatDTO) {
        Seat seat = new Seat();
        mapToEntity(seatDTO, seat);
        seat = seatRepository.save(seat);
        return mapToDTO(seat);
    }

    private SeatDTO mapToDTO(Seat seat) {
        SeatDTO dto = new SeatDTO();
        dto.setId(seat.getId());
        dto.setCinemaRoomId(seat.getCinemaRoom().getId());
        dto.setSeatNumber(seat.getSeatNumber());
        dto.setSeatRow(seat.getSeatRow());
        dto.setSeatType(seat.getSeatType().name());
        dto.setPriceMultiplier(seat.getPriceMultiplier());
        dto.setIsActive(seat.isActive());
        return dto;
    }

    private void mapToEntity(SeatDTO dto, Seat seat) {
        seat.setCinemaRoom(new CinemaRoom(dto.getCinemaRoomId()));
        seat.setSeatNumber(dto.getSeatNumber());
        seat.setSeatRow(dto.getSeatRow());
        seat.setSeatType(Seat.SeatType.valueOf(dto.getSeatType()));
        seat.setPriceMultiplier(dto.getPriceMultiplier());
        seat.setIsActive(dto.isIsActive());
    }
}
"@

    "TicketService.java" = @"
package com.moviebooking.service;

import com.moviebooking.dto.TicketDTO;
import com.moviebooking.entity.Ticket;
import com.moviebooking.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final QRCodeService qrCodeService;
    private final SendGridService sendGridService;
    private final TwilioService twilioService;

    public TicketDTO bookTicket(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        ticket.setUser(new User(ticketDTO.getUserId()));
        ticket.setShowtime(new Showtime(ticketDTO.getShowtimeId()));
        ticket.setTotalAmount(ticketDTO.getTotalAmount());
        ticket.setQrCode(qrCodeService.generateQRCode(ticketDTO));
        ticket = ticketRepository.save(ticket);
        
        // Gửi email/SMS xác nhận
        sendGridService.sendEmail(ticket.getUser().getEmail(), "Ticket Confirmation", "Your ticket QR code: " + ticket.getQrCode());
        twilioService.sendSMS(ticket.getUser().getPhoneNumber(), "Your ticket QR code: " + ticket.getQrCode());
        
        return mapToDTO(ticket);
    }

    public List<TicketDTO> getUserTickets(Integer userId) {
        return ticketRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private TicketDTO mapToDTO(Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        dto.setUserId(ticket.getUser().getId());
        dto.setShowtimeId(ticket.getShowtime().getId());
        dto.setQrCode(ticket.getQrCode());
        dto.setTotalAmount(ticket.getTotalAmount());
        dto.setStatus(ticket.getStatus().name());
        dto.setPurchaseTime(ticket.getPurchaseTime());
        return dto;
    }
}
"@

    "TicketSeatService.java" = @"
package com.moviebooking.service;

import com.moviebooking.dto.TicketSeatDTO;
import com.moviebooking.entity.TicketSeat;
import com.moviebooking.repository.TicketSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketSeatService {
    private final TicketSeatRepository ticketSeatRepository;

    public List<TicketSeatDTO> getSeatsByTicketId(Integer ticketId) {
        return ticketSeatRepository.findByTicketId(ticketId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public TicketSeatDTO createTicketSeat(TicketSeatDTO ticketSeatDTO) {
        TicketSeat ticketSeat = new TicketSeat();
        ticketSeat.setTicket(new Ticket(ticketSeatDTO.getTicketId()));
        ticketSeat.setSeat(new Seat(ticketSeatDTO.getSeatId()));
        ticketSeat.setPrice(ticketSeatDTO.getPrice());
        ticketSeat = ticketSeatRepository.save(ticketSeat);
        return mapToDTO(ticketSeat);
    }

    private TicketSeatDTO mapToDTO(TicketSeat ticketSeat) {
        TicketSeatDTO dto = new TicketSeatDTO();
        dto.setId(ticketSeat.getId());
        dto.setTicketId(ticketSeat.getTicket().getId());
        dto.setSeatId(ticketSeat.getSeat().getId());
        dto.setPrice(ticketSeat.getPrice());
        return dto;
    }
}
"@

    "PaymentService.java" = @"
package com.moviebooking.service;

import com.moviebooking.dto.PaymentDTO;
import com.moviebooking.entity.Payment;
import com.moviebooking.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final StripeService stripeService;

    public PaymentDTO processPayment(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setTicket(new Ticket(paymentDTO.getTicketId()));
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(Payment.PaymentMethod.valueOf(paymentDTO.getPaymentMethod()));
        
        // Thanh toán qua Stripe
        if (payment.getPaymentMethod() != Payment.PaymentMethod.CASH) {
            String clientSecret = stripeService.createPaymentIntent(paymentDTO.getAmount(), "usd");
            payment.setTransactionId(clientSecret);
            payment.setStatus(Payment.Status.COMPLETED);
        } else {
            payment.setStatus(Payment.Status.PENDING);
        }
        
        payment = paymentRepository.save(payment);
        return mapToDTO(payment);
    }

    private PaymentDTO mapToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setTicketId(payment.getTicket().getId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod().name());
        dto.setTransactionId(payment.getTransactionId());
        dto.setStatus(payment.getStatus().name());
        dto.setPaymentTime(payment.getPaymentTime());
        dto.setPaymentDetails(payment.getPaymentDetails());
        return dto;
    }
}
"@

    "ReviewService.java" = @"
package com.moviebooking.service;

import com.moviebooking.dto.ReviewDTO;
import com.moviebooking.entity.Review;
import com.moviebooking.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public List<ReviewDTO> getReviewsByMovieId(Integer movieId) {
        return reviewRepository.findByMovieId(movieId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        if (reviewRepository.findByUserIdAndMovieId(reviewDTO.getUserId(), reviewDTO.getMovieId()).isPresent()) {
            throw new RuntimeException("User already reviewed this movie");
        }
        Review review = new Review();
        review.setUser(new User(reviewDTO.getUserId()));
        review.setMovie(new Movie(reviewDTO.getMovieId()));
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review = reviewRepository.save(review);
        return mapToDTO(review);
    }

    private ReviewDTO mapToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setUserId(review.getUser().getId());
        dto.setMovieId(review.getMovie().getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }
}
"@

    "PromotionService.java" = @"
package com.moviebooking.service;

import com.moviebooking.dto.PromotionDTO;
import com.moviebooking.entity.Promotion;
import com.moviebooking.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final PromotionRepository promotionRepository;

    public PromotionDTO applyPromotion(String code) {
        Promotion promotion = promotionRepository.findByCodeAndIsActiveTrue(code)
                .orElseThrow(() -> new RuntimeException("Invalid promotion code"));
        if (promotion.getUsageCount() >= promotion.getUsageLimit()) {
            throw new RuntimeException("Promotion usage limit reached");
        }
        promotion.setUsageCount(promotion.getUsageCount() + 1);
        promotion = promotionRepository.save(promotion);
        return mapToDTO(promotion);
    }

    public PromotionDTO createPromotion(PromotionDTO promotionDTO) {
        Promotion promotion = new Promotion();
        mapToEntity(promotionDTO, promotion);
        promotion = promotionRepository.save(promotion);
        return mapToDTO(promotion);
    }

    private PromotionDTO mapToDTO(Promotion promotion) {
        PromotionDTO dto = new PromotionDTO();
        dto.setId(promotion.getId());
        dto.setName(promotion.getName());
        dto.setDescription(promotion.getDescription());
        dto.setDiscountType(promotion.getDiscountType().name());
        dto.setDiscountValue(promotion.getDiscountValue());
        dto.setCode(promotion.getCode());
        dto.setStartDate(promotion.getStartDate());
        dto.setEndDate(promotion.getEndDate());
        dto.setMinPurchase(promotion.getMinPurchase());
        dto.setMaxDiscount(promotion.getMaxDiscount());
        dto.setUsageLimit(promotion.getUsageLimit());
        dto.setUsageCount(promotion.getUsageCount());
        dto.setIsActive(promotion.isActive());
        return dto;
    }

    private void mapToEntity(PromotionDTO dto, Promotion promotion) {
        promotion.setName(dto.getName());
        promotion.setDescription(dto.getDescription());
        promotion.setDiscountType(Promotion.DiscountType.valueOf(dto.getDiscountType()));
        promotion.setDiscountValue(dto.getDiscountValue());
        promotion.setCode(dto.getCode());
        promotion.setStartDate(dto.getStartDate());
        promotion.setEndDate(dto.getEndDate());
        promotion.setMinPurchase(dto.getMinPurchase());
        promotion.setMaxDiscount(dto.getMaxDiscount());
        promotion.setUsageLimit(dto.getUsageLimit());
        promotion.setUsageCount(dto.getUsageCount());
        promotion.setIsActive(dto.isIsActive());
    }
}
"@

    "StatisticsService.java" = @"
package com.moviebooking.service;

import com.moviebooking.dto.StatisticsDTO;
import com.moviebooking.entity.Statistics;
import com.moviebooking.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;

    public List<StatisticsDTO> getStatisticsByDateRange(LocalDate start, LocalDate end) {
        return statisticsRepository.findByStatsDateBetween(start, end)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public StatisticsDTO createStatistics(StatisticsDTO statisticsDTO) {
        Statistics statistics = new Statistics();
        mapToEntity(statisticsDTO, statistics);
        statistics = statisticsRepository.save(statistics);
        return mapToDTO(statistics);
    }

    private StatisticsDTO mapToDTO(Statistics statistics) {
        StatisticsDTO dto = new StatisticsDTO();
        dto.setId(statistics.getId());
        dto.setShowtimeId(statistics.getShowtime() != null ? statistics.getShowtime().getId() : null);
        dto.setMovieId(statistics.getMovie() != null ? statistics.getMovie().getId() : null);
        dto.setCinemaId(statistics.getCinema() != null ? statistics.getCinema().getId() : null);
        dto.setRevenue(statistics.getRevenue());
        dto.setTicketCount(statistics.getTicketCount());
        dto.setStatsDate(statistics.getStatsDate());
        return dto;
    }

    private void mapToEntity(StatisticsDTO dto, Statistics statistics) {
        statistics.setShowtime(dto.getShowtimeId() != null ? new Showtime(dto.getShowtimeId()) : null);
        statistics.setMovie(dto.getMovieId() != null ? new Movie(dto.getMovieId()) : null);
        statistics.setCinema(dto.getCinemaId() != null ? new Cinema(dto.getCinemaId()) : null);
        statistics.setRevenue(dto.getRevenue());
        statistics.setTicketCount(dto.getTicketCount());
        statistics.setStatsDate(dto.getStatsDate());
    }
}
"@

    "UserPreferenceService.java" = @"
package com.moviebooking.service;

import com.moviebooking.dto.UserPreferenceDTO;
import com.moviebooking.entity.UserPreference;
import com.moviebooking.repository.UserPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPreferenceService {
    private final UserPreferenceRepository userPreferenceRepository;

    public List<UserPreferenceDTO> getPreferencesByUserId(Integer userId) {
        return userPreferenceRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public UserPreferenceDTO createPreference(UserPreferenceDTO preferenceDTO) {
        UserPreference preference = new UserPreference();
        preference.setUser(new User(preferenceDTO.getUserId()));
        preference.setGenre(new Genre(preferenceDTO.getGenreId()));
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
"@

    "NotificationService.java" = @"
package com.moviebooking.service;

import com.moviebooking.dto.NotificationDTO;
import com.moviebooking.entity.Notification;
import com.moviebooking.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final FirebaseService firebaseService;

    public List<NotificationDTO> getUnreadNotifications(Integer userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        notification.setUser(new User(notificationDTO.getUserId()));
        notification.setTitle(notificationDTO.getTitle());
        notification.setMessage(notificationDTO.getMessage());
        notification.setType(Notification.Type.valueOf(notificationDTO.getType()));
        notification = notificationRepository.save(notification);
        
        // Gửi thông báo đẩy qua Firebase
        firebaseService.sendPushNotification(notificationDTO.getUserId(), notificationDTO.getTitle(), notificationDTO.getMessage());
        
        return mapToDTO(notification);
    }

    private NotificationDTO mapToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUser() != null ? notification.getUser().getId() : null);
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType().name());
        dto.setIsRead(notification.isRead());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }
}
"@

    "LoyaltyPointService.java" = @"
package com.moviebooking.service;

import com.moviebooking.dto.LoyaltyPointDTO;
import com.moviebooking.entity.LoyaltyPoint;
import com.moviebooking.repository.LoyaltyPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoyaltyPointService {
    private final LoyaltyPointRepository loyaltyPointRepository;

    public List<LoyaltyPointDTO> getPointsByUserId(Integer userId) {
        return loyaltyPointRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public LoyaltyPointDTO addPoints(LoyaltyPointDTO pointDTO) {
        LoyaltyPoint point = new LoyaltyPoint();
        point.setUser(new User(pointDTO.getUserId()));
        point.setPoints(pointDTO.getPoints());
        point.setTransactionType(LoyaltyPoint.TransactionType.valueOf(pointDTO.getTransactionType()));
        point.setTransactionDetails(pointDTO.getTransactionDetails());
        point = loyaltyPointRepository.save(point);
        return mapToDTO(point);
    }

    private LoyaltyPointDTO mapToDTO(LoyaltyPoint point) {
        LoyaltyPointDTO dto = new LoyaltyPointDTO();
        dto.setId(point.getId());
        dto.setUserId(point.getUser().getId());
        dto.setPoints(point.getPoints());
        dto.setEarnedDate(point.getEarnedDate());
        dto.setUsedDate(point.getUsedDate());
        dto.setTransactionType(point.getTransactionType().name());
        dto.setTransactionDetails(point.getTransactionDetails());
        return dto;
    }
}
"@

    "FoodItemService.java" = @"
package com.moviebooking.service;

import com.moviebooking.dto.FoodItemDTO;
import com.moviebooking.entity.FoodItem;
import com.moviebooking.repository.FoodItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodItemService {
    private final FoodItemRepository foodItemRepository;

    public List<FoodItemDTO> getFoodItemsByCategory(String category) {
        return foodItemRepository.findByCategoryAndIsActiveTrue(FoodItem.Category.valueOf(category))
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public FoodItemDTO createFoodItem(FoodItemDTO foodItemDTO) {
        FoodItem foodItem = new FoodItem();
        mapToEntity(foodItemDTO, foodItem);
        foodItem = foodItemRepository.save(foodItem);
        return mapToDTO(foodItem);
    }

    private FoodItemDTO mapToDTO(FoodItem foodItem) {
        FoodItemDTO dto = new FoodItemDTO();
        dto.setId(foodItem.getId());
        dto.setName(foodItem.getName());
        dto.setDescription(foodItem.getDescription());
        dto.setPrice(foodItem.getPrice());
        dto.setImageUrl(foodItem.getImageUrl());
        dto.setCategory(foodItem.getCategory().name());
        dto.setIsActive(foodItem.isActive());
        return dto;
    }

    private void mapToEntity(FoodItemDTO dto, FoodItem foodItem) {
        foodItem.setName(dto.getName());
        foodItem.setDescription(dto.getDescription());
        foodItem.setPrice(dto.getPrice());
        foodItem.setImageUrl(dto.getImageUrl());
        foodItem.setCategory(FoodItem.Category.valueOf(dto.getCategory()));
        foodItem.setIsActive(dto.isIsActive());
    }
}
"@

    "FoodOrderService.java" = @"
package com.moviebooking.service;

import com.moviebooking.dto.FoodOrderDTO;
import com.moviebooking.entity.FoodOrder;
import com.moviebooking.repository.FoodOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodOrderService {
    private final FoodOrderRepository foodOrderRepository;

    public List<FoodOrderDTO> getOrdersByTicketId(Integer ticketId) {
        return foodOrderRepository.findByTicketId(ticketId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public FoodOrderDTO createFoodOrder(FoodOrderDTO foodOrderDTO) {
        FoodOrder foodOrder = new FoodOrder();
        foodOrder.setTicket(new Ticket(foodOrderDTO.getTicketId()));
        foodOrder.setFoodItem(new FoodItem(foodOrderDTO.getFoodItemId()));
        foodOrder.setQuantity(foodOrderDTO.getQuantity());
        foodOrder.setPrice(foodOrderDTO.getPrice());
        foodOrder = foodOrderRepository.save(foodOrder);
        return mapToDTO(foodOrder);
    }

    private FoodOrderDTO mapToDTO(FoodOrder foodOrder) {
        FoodOrderDTO dto = new FoodOrderDTO();
        dto.setId(foodOrder.getId());
        dto.setTicketId(foodOrder.getTicket().getId());
        dto.setFoodItemId(foodOrder.getFoodItem().getId());
        dto.setQuantity(foodOrder.getQuantity());
        dto.setPrice(foodOrder.getPrice());
        dto.setOrderTime(foodOrder.getOrderTime());
        return dto;
    }
}
"@
}

# Tạo các file Service
foreach ($file_name in $SERVICE_CONTENTS.Keys) {
    try {
        $file_path = Join-Path $SERVICE_DIR $file_name
        Write-Host "Creating $file_path..."
        $SERVICE_CONTENTS[$file_name] | Out-File -FilePath $file_path -Encoding UTF8
        Write-Host "Created $file_path"
    } catch {
        Write-Host "Error creating $file_name : $_"
    }
}

Write-Host "All Service files have been created in $SERVICE_DIR"