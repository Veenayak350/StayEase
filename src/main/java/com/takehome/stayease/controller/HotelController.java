package com.takehome.stayease.controller;





import com.takehome.stayease.Service.HotelService;
import com.takehome.stayease.dto.HotelRequest;
import com.takehome.stayease.dto.HotelResponse;
import com.takehome.stayease.dto.HotelResponse2;
import com.takehome.stayease.dto.HotelUpdateRequest;
import com.takehome.stayease.dto.HotelUpdateResponse;
import com.takehome.stayease.entity.Hotel;
import com.takehome.stayease.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService; 

    // Admin: Create Hotel
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
public ResponseEntity<HotelResponse> createHotel( @RequestBody @Valid HotelRequest request) {
    HotelResponse response = hotelService.createHotel(request);
    return ResponseEntity.ok(response);
}


    // Public: View All Hotels
    @GetMapping
    public ResponseEntity<List<HotelResponse2>> getAllHotels() {
        List<HotelResponse2> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }

    // Manager: Update Hotel
    
    @PutMapping("/{hotelId}")
    @PreAuthorize("hasRole('HOTEL_MANAGER')")
    public ResponseEntity<HotelUpdateResponse> updateHotel(
            @PathVariable Long hotelId,
            @Valid @RequestBody HotelUpdateRequest request) {
        HotelUpdateResponse updated = hotelService.updateHotel(hotelId, request);
        return ResponseEntity.ok(updated);
    }

    // Admin: Delete Hotel
    @DeleteMapping("/{hotelId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long hotelId) {
        hotelService.deleteHotel(hotelId);
        return ResponseEntity.noContent().build();
    }
}