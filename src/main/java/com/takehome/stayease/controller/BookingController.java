package com.takehome.stayease.controller;

import jakarta.servlet.http.HttpServletRequest;
import com.takehome.stayease.Service.BookingServiceImpl;
import com.takehome.stayease.Service.JwtService;
import com.takehome.stayease.dto.BookingRequest;
import com.takehome.stayease.dto.BookingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingServiceImpl bookingService;

    @Autowired
    private JwtService jwtService;

    
    private String extractEmailFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader != null ? authHeader.replace("Bearer ", "") : "";
        return jwtService.extractUsername(token);
    }

    @PostMapping("/{hotelId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BookingResponse> createBooking(
            @PathVariable Long hotelId,
            @RequestBody BookingRequest request,
            HttpServletRequest httpRequest) {
        String email = extractEmailFromToken(httpRequest);
        return ResponseEntity.ok(bookingService.createBooking(hotelId, request, email));
    }

    @GetMapping("/{bookingId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BookingResponse> getBookingById(
            @PathVariable Long bookingId,
            HttpServletRequest httpRequest) {
        String email = extractEmailFromToken(httpRequest);
        return ResponseEntity.ok(bookingService.getBookingById(bookingId, email));
    }

    @DeleteMapping("/{bookingId}")
    @PreAuthorize("hasRole('HOTEL_MANAGER')")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }
}
