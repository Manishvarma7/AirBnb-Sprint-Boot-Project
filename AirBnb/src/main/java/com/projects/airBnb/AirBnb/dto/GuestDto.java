package com.projects.airBnb.AirBnb.dto;

import com.projects.airBnb.AirBnb.entities.User;
import com.projects.airBnb.AirBnb.entities.enums.Gender;
import lombok.Data;

import java.time.LocalDate;
@Data
public class GuestDto {
    private Long id;
    private String name;
    private Gender gender;
    private LocalDate dateOfBirth;
}
