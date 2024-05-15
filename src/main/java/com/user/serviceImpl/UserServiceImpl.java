package com.user.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.user.controller.UserController;
import com.user.entities.Hotel;
import com.user.entities.Rating;
import com.user.entities.User;
import com.user.exception.ResourceNotFoundException;
import com.user.external.services.HotelServices;
import com.user.repositories.UserRespository;
import com.user.service.UserServices;

@Service
public class UserServiceImpl implements UserServices {

	@Autowired
	private UserRespository respository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private HotelServices hotelServices;

	@Autowired
	private WebClient.Builder webClientBuilder;

	@Autowired
	private DiscoveryClient discoveryClient;

	private Logger logger = LoggerFactory.getLogger(UserServices.class);

	@Override
	public User createUser(User user) {
		String uuid = UUID.randomUUID().toString();
		user.setId(uuid);
		return respository.save(user);
	}

	@Override
	public List<User> getAllUser(User user) {
		return respository.findAll();
	}

	@Override
	public User getUserById(String userId) {

		User user = respository.findById(userId).orElseThrow(
				() -> new ResourceNotFoundException("User with given Id is not found on server! " + userId));

		String otherServicesUrl = "http://localhost:8083";
		String hotelServicesUrl = "http://localhost:8082";

//	    List<ServiceInstance> otherServicesInstances = discoveryClient.getInstances("OTHER-SERVICE");
//	    List<ServiceInstance> hotelServicesInstances = discoveryClient.getInstances("HOTEL-SERVICE");
//
//	    if (otherServicesInstances.isEmpty() || hotelServicesInstances.isEmpty()) {
//	        throw new ResourceNotFoundException("Service instances not found for other services or hotel services.");
//	    }
//	    
//	    String otherServicesBaseUrl = otherServicesInstances.get(0).getUri().toString();
//	    String hotelServicesBaseUrl = hotelServicesInstances.get(0).getUri().toString();

		Rating[] ratingOfUser = webClientBuilder
				.baseUrl(otherServicesUrl).build() // Create WebClient instance
				.get()
				.uri("/ratings/users/" + userId)
				.retrieve()
				.bodyToMono(Rating[].class)
				.block();

		List<Rating> ratings = Arrays.stream(ratingOfUser).map(rating -> {
			Hotel hotel = webClientBuilder
					.baseUrl(hotelServicesUrl)
					.build()
					.get()
					.uri("/hotels/" + rating.getHotelId())
					.retrieve()
					.bodyToMono(Hotel.class).block();

			rating.setHotel(hotel);

			return rating;
		}).collect(Collectors.toList());

		user.setRating(ratings);
//		user.setRating(ratingOfUser);
		return user;
	}

}
