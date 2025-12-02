package com.takehome.stayease.dto;


import lombok.Data;

@Data
public class BookingRequest {
    private String checkInDate;
    private String checkOutDate;
}
