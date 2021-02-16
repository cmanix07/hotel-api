package com.booking.recruitment.hotel.service.impl;

import com.booking.recruitment.hotel.exception.BadRequestException;
import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.repository.CityRepository;
import com.booking.recruitment.hotel.repository.HotelRepository;
import com.booking.recruitment.hotel.service.HotelService;
import com.booking.recruitment.hotel.util.Haversine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class DefaultHotelService implements HotelService {
  private final HotelRepository hotelRepository;
  CityRepository cityRepository;

  @Autowired
  DefaultHotelService(HotelRepository hotelRepository, CityRepository cityRepository) {
    this.hotelRepository = hotelRepository;
    this.cityRepository = cityRepository;
  }



  DefaultHotelService(HotelRepository hotelRepository) {
    this.hotelRepository = hotelRepository;
  }

  @Override
  public List<Hotel> getAllHotels() {
    return hotelRepository.findAll();
  }

  @Override
  public List<Hotel> getHotelsByCity(Long cityId) {
    return hotelRepository.findAll().stream()
        .filter((hotel) -> cityId.equals(hotel.getCity().getId()))
        .collect(Collectors.toList());
  }

  @Override
  public Hotel createNewHotel(Hotel hotel) {
    if (hotel.getId() != null) {
      throw new BadRequestException("The ID must not be provided when creating a new Hotel");
    }

    return hotelRepository.save(hotel);
  }

  @Override
  public Hotel geHotelById(Long id) {
    Optional<Hotel> hotelOptional = hotelRepository.findById(id);
    if(hotelOptional.isPresent()){
      return  hotelOptional.get();
    }
    return Hotel.builder().build();
  }

  @Override
  public void deleteHotelById(Long id) {
    Optional<Hotel> hotelOptional = hotelRepository.findById(id);
    if(hotelOptional.isPresent()){
      Hotel hotel = hotelOptional.get();
      hotel.setDeleted(true);
      hotelRepository.saveAndFlush(hotel);
    }

  }


  @Override
  public List<Hotel> findBestHotelForCustomer(Long cityId, Double distance) {

    List<Hotel> hotelList = hotelRepository.findHotelWithInDistance(cityId);

    List<Hotel> sortedHotelList = hotelList.stream().filter(hotel ->
            Haversine.distance(hotel.getLatitude(), hotel.getLongitude(), hotel.getLatitude(), hotel.getLongitude()) < distance
    ).sorted((h1, h2) -> {
      if ((Haversine.distance(h1.getLatitude(), h1.getLongitude(), h1.getLatitude(), h1.getLongitude()))
              > (Haversine.distance(h2.getLatitude(), h2.getLongitude(), h2.getLatitude(), h2.getLongitude())))
        return 1;
      else
        return -1;
    }).collect(Collectors.toList());

    List<Hotel> top3Hotels = new ArrayList<>();

    if (!sortedHotelList.isEmpty()) {
      for (int i = 0, j = 0; i < sortedHotelList.size() && j < 3; i++, j++) {
        top3Hotels.add(sortedHotelList.get(i));
      }
    }

    return top3Hotels;

  }
}
