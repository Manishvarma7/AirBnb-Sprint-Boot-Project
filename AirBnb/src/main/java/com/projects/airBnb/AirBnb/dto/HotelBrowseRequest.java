package com.projects.airBnb.AirBnb.dto;

import lombok.Data;

import java.time.LocalDate;


@Data
public class HotelBrowseRequest {

    private String city;
    private LocalDate startDate; //checkin date
    private LocalDate endDate; //checkout Date
    private Integer roomsCount;
    private Integer page=0;
    private Integer size=10;


    //We need to have hotels with aleast one room
    // that have inventory available between
    // start date and end date

    // hotel search request aim is to
    // get the hotels of a particular inventory
    // in a paginated format
}
