package com.projects.airBnb.AirBnb.dto;

import com.projects.airBnb.AirBnb.entities.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileUpdateRequestDto {
    private String name;
    private LocalDate dateOfBirth;
    private Gender gender;
}
