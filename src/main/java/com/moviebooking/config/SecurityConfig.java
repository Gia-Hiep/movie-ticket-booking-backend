package com.moviebooking.config;

import com.moviebooking.service.JwtService;
import com.moviebooking.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                        .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/movies/**", "/api/cinemas/**", "/api/cinema-rooms/**", "/api/seats/**", "/api/promotions/**", "/api/genres/**", "/api/showtimes/**", "/api/reviews/**", "/api/food-items/**", "/api/food-orders/**", "/api/tickets/**", "/api/ticket-seats/**", "/api/loyalty-points/**", "/api/notifications/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user-preferences/**", "/api/reviews/**", "/api/food-orders/**", "/api/payments/**", "/api/tickets/**", "/api/ticket-seats/**", "/api/loyalty-points/**").authenticated()
                        .requestMatchers("/api/users/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/statistics/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/showtimes/**", "/api/movies/**", "/api/cinemas/**", "/api/cinema-rooms/**", "/api/food-items/**", "/api/promotions/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/showtimes/**", "/api/movies/**", "/api/cinemas/**", "/api/cinema-rooms/**", "/api/food-items/**", "/api/promotions/**", "/api/reviews/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/showtimes/**", "/api/movies/**", "/api/cinemas/**", "/api/cinema-rooms/**", "/api/food-items/**", "/api/promotions/**", "/api/reviews/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAuthenticationFilter(jwtService, userDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}