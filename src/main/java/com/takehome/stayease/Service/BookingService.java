package com.takehome.stayease.Service;

import com.takehome.stayease.dto.*;

public interface BookingService {
    BookingResponse createBooking(Long hotelId, BookingRequest request, String customerEmail);
    BookingResponse getBookingById(Long bookingId, String customerEmail);
    void cancelBooking(Long bookingId);
}

