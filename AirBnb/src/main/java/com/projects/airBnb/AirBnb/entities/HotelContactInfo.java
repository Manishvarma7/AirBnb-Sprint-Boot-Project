package com.projects.airBnb.AirBnb.entities;


import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable  //Marks a class (e.g., HotelContactInfo) as a reusable component that can be embedded in entities.
public class HotelContactInfo {
    private String address;
    private String phoneNumber;
    private String email;
    private String location;
}
