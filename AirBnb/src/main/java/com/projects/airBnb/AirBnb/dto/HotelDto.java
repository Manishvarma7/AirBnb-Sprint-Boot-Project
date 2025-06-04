package com.projects.airBnb.AirBnb.dto;


import com.projects.airBnb.AirBnb.entities.HotelContactInfo;

import lombok.Data;


@Data // this will create all the getters and setters methods
public class HotelDto {

    private Long id;
    private String name;
    private String city;
    private String[] photos;
    private String[] amenities;
    private HotelContactInfo contactInfo;
    private Boolean active;
}
