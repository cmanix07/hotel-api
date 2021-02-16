package com.booking.recruitment.hotel.repository;

import com.booking.recruitment.hotel.model.City;
import com.booking.recruitment.hotel.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Query(value = "select * from Hotel where city_id =?1", nativeQuery = true)
    List<Hotel> findHotelWithInDistance(Long cityId);


}
