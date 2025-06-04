package com.projects.airBnb.AirBnb.repositories;


import com.projects.airBnb.AirBnb.entities.Guest;
import com.projects.airBnb.AirBnb.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    List<Guest> findByUser(User user);
}