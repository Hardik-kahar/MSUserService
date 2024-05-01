package com.user.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

		Rating[] ratingOfUser = restTemplate.getForObject("http://localhost:8083/ratings/users/" + userId,
				Rating[].class);

		List<Rating> ratings = Arrays.stream(ratingOfUser)
			    .map(rating -> {
			    	System.out.println("id: "+ rating.getHotelId());
//			        ResponseEntity<Hotel> responseEntity = restTemplate.getForEntity("http://localhost:8082/hotels/" + rating.getHotelId(), Hotel.class);
//			        Hotel hotel = responseEntity.getBody();
			    	
			    	Hotel hotel = hotelServices.getHotel(rating.getHotelId());

			        rating.setHotel(hotel);

			        return rating;
			    })
			    .collect(Collectors.toList());

		user.setRating(ratings);
//		user.setRating(ratingOfUser);
		return user;
	}

}
