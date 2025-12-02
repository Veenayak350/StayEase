package com.takehome.stayease.Service;



import java.util.List;
import com.takehome.stayease.dto.*;
import com.takehome.stayease.entity.Hotel;


public interface HotelService {
    HotelResponse createHotel(HotelRequest request);
    List<HotelResponse2> getAllHotels();
    HotelUpdateResponse updateHotel(Long hotelId, HotelUpdateRequest request);
    void deleteHotel(Long hotelId);
}
