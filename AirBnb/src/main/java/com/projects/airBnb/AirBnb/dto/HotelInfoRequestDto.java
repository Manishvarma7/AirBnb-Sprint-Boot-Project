package com.projects.airBnb.AirBnb.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HotelInfoRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private Long roomsCount;
}
