package com.takehome.stayease.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class HotelResponse2{
    private Long id;
    private String name;
    private String location;
    private String description;
    private Integer availableRooms;

}