package com.projects.airBnb.AirBnb.services;


import com.projects.airBnb.AirBnb.dto.ProfileUpdateRequestDto;
import com.projects.airBnb.AirBnb.dto.UserDto;
import com.projects.airBnb.AirBnb.entities.User;

public interface UserService {

    User getUserById(Long id);

    void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    UserDto getMyProfile();
}
