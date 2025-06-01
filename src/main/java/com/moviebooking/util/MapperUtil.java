package com.moviebooking.util;

import com.moviebooking.dto.*;
import com.moviebooking.entity.*;

public class MapperUtil {

    public static UserDTO toUserDTO(User user) {
        if (user == null) return null;
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

    public static MovieDTO toMovieDTO(Movie movie) {
        if (movie == null) return null;
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
        dto.setActive(movie.isActive());
        return dto;
    }

    public static GenreDTO toGenreDTO(Genre genre) {
        if (genre == null) return null;
        GenreDTO dto = new GenreDTO();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        dto.setDescription(genre.getDescription());
        return dto;
    }

    public static MovieGenreDTO toMovieGenreDTO(MovieGenre movieGenre) {
        if (movieGenre == null) return null;
        MovieGenreDTO dto = new MovieGenreDTO();
        dto.setId(movieGenre.getId());
        dto.setMovieId(movieGenre.getMovie().getId());
        dto.setGenreId(movieGenre.getGenre().getId());
        return dto;
    }

    public static CinemaDTO toCinemaDTO(Cinema cinema) {
        if (cinema == null) return null;
        CinemaDTO dto = new CinemaDTO();
        dto.setId(cinema.getId());
        dto.setName(cinema.getName());
        dto.setAddress(cinema.getAddress());
        dto.setCity(cinema.getCity());
        dto.setPhone(cinema.getPhone());
        dto.setDescription(cinema.getDescription());
        dto.setImageUrl(cinema.getImageUrl());
        dto.setActive(cinema.isActive());
        return dto;
    }

    public static CinemaRoomDTO toCinemaRoomDTO(CinemaRoom room) {
        if (room == null) return null;
        CinemaRoomDTO dto = new CinemaRoomDTO();
        dto.setId(room.getId());

        // Populate CinemaDTO
        CinemaDTO cinemaDTO = toCinemaDTO(room.getCinema());
        dto.setCinema(cinemaDTO);

        dto.setName(room.getName());
        dto.setCapacity(room.getCapacity());
        dto.setRoomType(room.getRoomType().name());
        dto.setActive(room.isActive());
        return dto;
    }

    public static ShowtimeDTO toShowtimeDTO(Showtime showtime) {
        if (showtime == null) return null;
        ShowtimeDTO dto = new ShowtimeDTO();
        dto.setId(showtime.getId());

        // Populate MovieDTO
        MovieDTO movieDTO = toMovieDTO(showtime.getMovie());
        dto.setMovie(movieDTO);

        // Populate CinemaRoomDTO
        CinemaRoomDTO cinemaRoomDTO = toCinemaRoomDTO(showtime.getCinemaRoom());
        dto.setCinemaRoom(cinemaRoomDTO);

        dto.setStartTime(showtime.getStartTime());
        dto.setEndTime(showtime.getEndTime());
        dto.setTicketPrice(showtime.getTicketPrice());
        dto.setFormat(showtime.getFormat().name());
        dto.setLanguage(showtime.getLanguage());
        dto.setActive(showtime.isActive());
        return dto;
    }

    public static SeatDTO toSeatDTO(Seat seat) {
        if (seat == null) return null;
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

    public static TicketDTO toTicketDTO(Ticket ticket) {
        if (ticket == null) return null;
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

    public static TicketSeatDTO toTicketSeatDTO(TicketSeat ticketSeat) {
        if (ticketSeat == null) return null;
        TicketSeatDTO dto = new TicketSeatDTO();
        dto.setId(ticketSeat.getId());
        dto.setTicketId(ticketSeat.getTicket().getId());
        dto.setSeatId(ticketSeat.getSeat().getId());
        dto.setPrice(ticketSeat.getPrice());
        return dto;
    }

    public static PaymentDTO toPaymentDTO(Payment payment) {
        if (payment == null) return null;
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

    public static ReviewDTO toReviewDTO(Review review) {
        if (review == null) return null;
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setUserId(review.getUser().getId());
        dto.setMovieId(review.getMovie().getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }

    public static PromotionDTO toPromotionDTO(Promotion promotion) {
        if (promotion == null) return null;
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
        dto.setActive(promotion.isActive());
        return dto;
    }

    public static StatisticsDTO toStatisticsDTO(Statistics statistics) {
        if (statistics == null) return null;
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

    public static UserPreferenceDTO toUserPreferenceDTO(UserPreference preference) {
        if (preference == null) return null;
        UserPreferenceDTO dto = new UserPreferenceDTO();
        dto.setId(preference.getId());
        dto.setUserId(preference.getUser().getId());
        dto.setGenreId(preference.getGenre().getId());
        dto.setPreferenceLevel(preference.getPreferenceLevel());
        return dto;
    }

    public static NotificationDTO toNotificationDTO(Notification notification) {
        if (notification == null) return null;
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

    public static LoyaltyPointDTO toLoyaltyPointDTO(LoyaltyPoint point) {
        if (point == null) return null;
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

    public static FoodItemDTO toFoodItemDTO(FoodItem foodItem) {
        if (foodItem == null) return null;
        FoodItemDTO dto = new FoodItemDTO();
        dto.setId(foodItem.getId());
        dto.setName(foodItem.getName());
        dto.setDescription(foodItem.getDescription());
        dto.setPrice(foodItem.getPrice());
        dto.setImageUrl(foodItem.getImageUrl());
        dto.setCategory(foodItem.getCategory().name());
        dto.setActive(foodItem.isActive());
        return dto;
    }

    public static FoodOrderDTO toFoodOrderDTO(FoodOrder foodOrder) {
        if (foodOrder == null) return null;
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