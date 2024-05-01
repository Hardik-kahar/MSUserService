package com.user.external.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.user.entities.Hotel;

@FeignClient(name = "HOTEL-SERVICE")
public interface HotelServices {

	@GetMapping("/hotels/{hotelId}")
	Hotel getHotel(String hotelId);
	
}