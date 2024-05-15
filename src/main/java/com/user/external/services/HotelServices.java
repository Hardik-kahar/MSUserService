package com.user.external.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties.FeignClientConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import com.user.entities.Hotel;

@Component
@FeignClient(name = "HOTEL-SERVICE", url = "http://localhost:8082/hotels", configuration = FeignClientConfiguration.class)
public interface HotelServices {

	@GetMapping("/{hotelId}")
	Hotel getHotel(String hotelId);
	
}