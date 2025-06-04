package com.projects.airBnb.AirBnb.repositories;

import com.projects.airBnb.AirBnb.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoomRepository extends JpaRepository<Room, Long> {
}
