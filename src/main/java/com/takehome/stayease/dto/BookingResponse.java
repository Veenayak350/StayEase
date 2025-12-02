package com.takehome.stayease.dto;



import lombok.Data;

@Data
public class BookingResponse {
    private Long bookingId;
    private Long hotelId;
    private String checkInDate;
    private String checkOutDate;
}

