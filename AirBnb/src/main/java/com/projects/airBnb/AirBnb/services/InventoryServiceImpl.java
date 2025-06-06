package com.projects.airBnb.AirBnb.services;

import com.projects.airBnb.AirBnb.dto.*;
import com.projects.airBnb.AirBnb.entities.Hotel;
import com.projects.airBnb.AirBnb.entities.Inventory;
import com.projects.airBnb.AirBnb.entities.Room;
import com.projects.airBnb.AirBnb.entities.User;
import com.projects.airBnb.AirBnb.exceptions.ResourceNotFoundException;
import com.projects.airBnb.AirBnb.repositories.HotelMinPriceRepository;
import com.projects.airBnb.AirBnb.repositories.InventoryRepository;
import com.projects.airBnb.AirBnb.repositories.RoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.projects.airBnb.AirBnb.util.AppUtils.getCurrentUser;

@Service

@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService{
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;

    @Override
    public void initializeRoomForAYear(Room room) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);
        for(; !today.isAfter(endDate);today = today.plusDays(1)){
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .bookedCount(0)
                    .reservedCount(0)
                    .city(room.getHotel().getCity())
                    .date(today)
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE)
                    .totalCount(room.getTotalCount())
                    .closed(false)
                    .build();
            inventoryRepository.save(inventory);
        }
    }

    @Override
    public void deleteAllInventories(Room room) {
        log.info("Deleting the inventories of room with id: {}", room.getId());
        inventoryRepository.deleteByRoom(room);

    }

    @Override
    public Page<HotelPriceResponseDto> searchHotels(HotelBrowseRequest hotelBrowseRequest) {
        log.info("Searching hotels for {} city, from {} to {}", hotelBrowseRequest.getCity(), hotelBrowseRequest.getStartDate(), hotelBrowseRequest.getEndDate());
        Pageable pageable = PageRequest.of(hotelBrowseRequest.getPage(), hotelBrowseRequest.getSize());
        long dateCount =
                ChronoUnit.DAYS.between(hotelBrowseRequest.getStartDate(), hotelBrowseRequest.getEndDate()) + 1;

        // business logic - 90 days
        Page<HotelPriceDto> hotelPage =  
		        hotelMinPriceRepository.findHotelsWithAvailableInventory(hotelBrowseRequest.getCity(),
                hotelBrowseRequest.getStartDate(), hotelBrowseRequest.getEndDate(), hotelBrowseRequest.getRoomsCount(),
                dateCount, pageable);

        return hotelPage.map(hotelPriceDto -> {
            HotelPriceResponseDto hotelPriceResponseDto = modelMapper.map(hotelPriceDto.getHotel(), HotelPriceResponseDto.class);
            hotelPriceResponseDto.setPrice(hotelPriceDto.getPrice());
            return hotelPriceResponseDto;
        });
    }

    @Override
    public List<InventoryDto> getAllInventoryByRoom(Long roomId) {
        log.info("Getting All inventory by room for room with id: {}", roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: "+roomId));

        User user = getCurrentUser();
        if(!user.equals(room.getHotel().getOwner())) throw new AccessDeniedException("You are not the owner of room with id: "+roomId);

        return inventoryRepository.findByRoomOrderByDate(room).stream()
                .map((element) -> modelMapper.map(element,
                InventoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto) {
        log.info("Updating All inventory by room for room with id: {} between date range: {} - {}", roomId,
                updateInventoryRequestDto.getStartDate(), updateInventoryRequestDto.getEndDate());

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: "+roomId));

        User user = getCurrentUser();
        if(!user.equals(room.getHotel().getOwner())) throw new AccessDeniedException("You are not the owner of room with id: "+roomId);

        inventoryRepository.getInventoryAndLockBeforeUpdate(roomId, updateInventoryRequestDto.getStartDate(),
                updateInventoryRequestDto.getEndDate());

        inventoryRepository.updateInventory(roomId, updateInventoryRequestDto.getStartDate(),
                updateInventoryRequestDto.getEndDate(), updateInventoryRequestDto.getClosed(),
                updateInventoryRequestDto.getSurgeFactor());
    }
}
