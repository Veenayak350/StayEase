package com.takehome.stayease.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelResponse {
    private Long id;
    private String name;
    private String location;
    private String description;
    private Integer totalRooms;
    private Integer availableRooms;

}
