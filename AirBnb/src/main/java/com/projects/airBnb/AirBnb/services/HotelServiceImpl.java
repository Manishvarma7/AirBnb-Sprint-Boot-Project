package com.projects.airBnb.AirBnb.services;


import com.projects.airBnb.AirBnb.dto.*;
import com.projects.airBnb.AirBnb.entities.Hotel;
import com.projects.airBnb.AirBnb.entities.Room;
import com.projects.airBnb.AirBnb.entities.User;
import com.projects.airBnb.AirBnb.exceptions.ResourceNotFoundException;
import com.projects.airBnb.AirBnb.exceptions.UnAuthorisedException;
import com.projects.airBnb.AirBnb.repositories.HotelRepository;
import com.projects.airBnb.AirBnb.repositories.InventoryRepository;
import com.projects.airBnb.AirBnb.repositories.RoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import static com.projects.airBnb.AirBnb.util.AppUtils.getCurrentUser;
@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService{


    //RequiredArgsConstructor is going to create a constructor with required arguments
    // so this is a constructor dependency injection
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;


    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("creating a new hotel with  name : {}",hotelDto.getName());
        Hotel hotel= modelMapper.map(hotelDto,Hotel.class); //Here we are mapping all the fields of Hotel DTo
        // to the Hotel entity that is hotel.class and converts all the fields from hotel DTO to hotel entity
        hotel.setActive(false); // initially the hotel is inactive so that it is not available directly when it is created
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(user);
        hotel= hotelRepository.save(hotel);
        log.info("created a new hotel with ID : {}",hotelDto.getId());
        return modelMapper.map(hotel,HotelDto.class);
    }


    @Override
    public HotelDto getHotelbyId(Long id) {
        log.info("getting the hotel with ID : {}",id);
        Hotel hotel= hotelRepository
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with ID:" + id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+id);
        }
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long id , HotelDto hotelDto){
        log.info("updating hotel with ID : {}",id);
        Hotel hotel= hotelRepository
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with ID:" + id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+id);
        }
        modelMapper.map(hotelDto, hotel); // here all the fields will get transfered
        // to the hotel entity (hotel.class) and will get updated
        hotel.setId(id);
        hotel = hotelRepository.save(hotel);
        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id){
        Hotel hotel= hotelRepository
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with ID:" + id));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+id);
        }

        for(Room room : hotel.getRooms()){
            inventoryService.deleteAllInventories(room);
            roomRepository.deleteById(room.getId());
        }
        hotelRepository.deleteById(id);
    }
    @Override
    @Transactional
    public void activeHotelById(Long id){
        log.info("Activating the hotel with ID : {}",id);
        Hotel hotel= hotelRepository
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with ID:" + id));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+id);
        }
        hotel.setActive(true);
        //assuming only activation once
        for(Room room : hotel.getRooms()){
            inventoryService.initializeRoomForAYear(room);

        }
    }


    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId, HotelInfoRequestDto hotelInfoRequestDto) {
        log.info("getting the hotelinfo with ID : {}",hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+hotelId));

        long daysCount = ChronoUnit.DAYS.between(hotelInfoRequestDto.getStartDate(), hotelInfoRequestDto.getEndDate())+1;

        List<RoomPriceDto> roomPriceDtoList = inventoryRepository.findRoomAveragePrice(hotelId,
                hotelInfoRequestDto.getStartDate(), hotelInfoRequestDto.getEndDate(),
                hotelInfoRequestDto.getRoomsCount(), daysCount);

        List<RoomPriceResponseDto> rooms = roomPriceDtoList.stream()
                .map(roomPriceDto -> {
                    RoomPriceResponseDto roomPriceResponseDto = modelMapper.map(roomPriceDto.getRoom(),
                            RoomPriceResponseDto.class);
                    roomPriceResponseDto.setPrice(roomPriceDto.getPrice());
                    return roomPriceResponseDto;
                })
                .collect(Collectors.toList());

        return new HotelInfoDto(modelMapper.map(hotel, HotelDto.class), rooms);
    }

    @Override
    public List<HotelDto> getAllHotels() {
        User user = getCurrentUser();
        log.info("Getting all hotels for the admin user with ID: {}", user.getId());
        List<Hotel> hotels = hotelRepository.findByOwner(user);

        return hotels
                .stream()
                .map((element) -> modelMapper.map(element, HotelDto.class))
                .collect(Collectors.toList());
    }

}
