package com.takehome.stayease.Service;

import java.util.List;
import java.util.stream.Collectors;
import com.takehome.stayease.dto.*;
import com.takehome.stayease.entity.Hotel;
import com.takehome.stayease.exceptions.BadRequestException;
import com.takehome.stayease.exceptions.ResourceNotFoundException;
import com.takehome.stayease.repository.HotelRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelServiceImpl implements HotelService {

    private static final Logger log = LoggerFactory.getLogger(HotelServiceImpl.class);

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ObjectProvider<ModelMapper> modelMapperProvider;

    private ModelMapper getMapper() {
        return modelMapperProvider.getObject();
    }

    @Override
    public HotelResponse createHotel(HotelRequest request) {
        validateCreateRequest(request);

        log.info("Creating hotel: {}", request.getName());

        Hotel hotel = new Hotel();
        hotel.setName(request.getName());
        hotel.setLocation(request.getLocation());
        hotel.setDescription(request.getDescription());
        hotel.setTotalRooms(request.getAvailableRooms());
        hotel.setAvailableRooms(request.getAvailableRooms());

        Hotel saved = hotelRepository.save(hotel);
        

    

        return getMapper().map(saved, HotelResponse.class);

    }

    @Override
    public List<HotelResponse2> getAllHotels() {
        List<Hotel> hotels = hotelRepository.findAll();
        log.info("Fetched {} hotels", hotels.size());

        return hotels.stream()
                .map(hotel -> getMapper().map(hotel, HotelResponse2.class))
                .collect(Collectors.toList());
    }

    @Override
    public HotelUpdateResponse updateHotel(Long id, HotelUpdateRequest request) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel with ID " + id + " not found"));

        log.info("Updating hotel with ID {}: {}", id, request);

        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            hotel.setName(request.getName());
        }

        if (request.getAvailableRooms() != null) {
            if (request.getAvailableRooms() < 0 ) {
                throw new BadRequestException("Available rooms must be greater than zero ");
            }
            hotel.setAvailableRooms(request.getAvailableRooms());
        }

        Hotel updated = hotelRepository.save(hotel);
        return getMapper().map(updated, HotelUpdateResponse.class);
    }

    @Override
    public void deleteHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel with ID " + id + " not found"));
        log.info("Deleting hotel with ID {}", id);
        hotelRepository.delete(hotel);
    }

    private void validateCreateRequest(HotelRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BadRequestException("Hotel name must not be empty");
        }
        if (request.getLocation() == null || request.getLocation().trim().isEmpty()) {
            throw new BadRequestException("Hotel location must not be empty");
        }
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new BadRequestException("Hotel description must not be empty");
        }

        if (request.getAvailableRooms() == null ||
            request.getAvailableRooms() < 0 
            ) {
            throw new BadRequestException("Available rooms must be between 0 and total rooms");
        }
    }
}
