package com.takehome.stayease.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import com.takehome.stayease.dto.*;
import com.takehome.stayease.entity.Booking;
import com.takehome.stayease.entity.Hotel;
import com.takehome.stayease.entity.User;
import com.takehome.stayease.exceptions.ResourceNotFoundException;
import com.takehome.stayease.repository.BookingRepository;
import com.takehome.stayease.repository.HotelRepository;
import com.takehome.stayease.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectProvider<ModelMapper> modelMapperProvider;

    @Override
    @Transactional
    public BookingResponse createBooking(Long hotelId, BookingRequest request, String customerEmail) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        if (hotel.getAvailableRooms() <= 0) {
            throw new ResourceNotFoundException("No rooms available in hotel " + hotel.getName());

        }

        LocalDate checkIn = LocalDate.parse(request.getCheckInDate());
        LocalDate checkOut = LocalDate.parse(request.getCheckOutDate());

        if (checkIn.isBefore(LocalDate.now()) || checkOut.isBefore(checkIn)) {
            throw new RuntimeException("Invalid check-in/check-out dates");
        }

        User user = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Booking booking = new Booking();
        booking.setHotel(hotel);
        booking.setUser(user);
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);

        hotel.setAvailableRooms(hotel.getAvailableRooms() - 1);
        hotelRepository.save(hotel);

        Booking saved = bookingRepository.save(booking);

        ModelMapper modelMapper = modelMapperProvider.getObject();
        BookingResponse response = modelMapper.map(saved, BookingResponse.class);
        response.setHotelId(saved.getHotel().getId());
        return response;
    }

    @Override
    public BookingResponse getBookingById(Long bookingId, String customerEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getUser().getEmail().equals(customerEmail)) {
            throw new RuntimeException("Unauthorized access to booking");
        }

        ModelMapper modelMapper = modelMapperProvider.getObject();
        BookingResponse response = modelMapper.map(booking, BookingResponse.class);
        response.setHotelId(booking.getHotel().getId());
        return response;
    }

    @Override
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        Hotel hotel = booking.getHotel();
        hotel.setAvailableRooms(hotel.getAvailableRooms() + 1);
        hotelRepository.save(hotel);

        bookingRepository.delete(booking);
    }
}
