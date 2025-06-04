package com.projects.airBnb.AirBnb.services;

import com.projects.airBnb.AirBnb.dto.*;
import com.projects.airBnb.AirBnb.entities.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {

    void initializeRoomForAYear(Room room);

    void deleteAllInventories(Room room);

    Page<HotelPriceResponseDto> searchHotels(HotelBrowseRequest hotelBrowseRequestRequest);

    List<InventoryDto> getAllInventoryByRoom(Long roomId);

    void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto);
}
