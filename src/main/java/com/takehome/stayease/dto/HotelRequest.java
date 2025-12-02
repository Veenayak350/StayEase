package com.takehome.stayease.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String location;
    @NotBlank
    private String description;
    
    @NotNull
    private Integer availableRooms;

}
