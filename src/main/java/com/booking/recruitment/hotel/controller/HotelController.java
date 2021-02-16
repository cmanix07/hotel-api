package com.booking.recruitment.hotel.controller;

import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotel")
public class HotelController {
  private final HotelService hotelService;

  @Autowired
  public HotelController(HotelService hotelService) {
    this.hotelService = hotelService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Hotel> getAllHotels() {
    return hotelService.getAllHotels();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Hotel createHotel(@RequestBody Hotel hotel) {
    return hotelService.createNewHotel(hotel);
  }

  @GetMapping("/{id}")
  @ResponseBody
  public Hotel getHotelById(@PathVariable("id") Long id){
    return hotelService.geHotelById(id);

  }

  @PatchMapping(value = "/{id}")
  @ResponseBody
  public void deleteHotelById(@PathVariable("id") Long id){
    hotelService.deleteHotelById(id);
  }

  @GetMapping(value = "/search/{cityId}")
  @ResponseBody
  public List<Hotel> findBestHotelForCustomer(@PathVariable("cityId") Long cityId, @RequestParam("sortBy") Double distance){
    return hotelService.findBestHotelForCustomer(cityId,distance);

  }
}
