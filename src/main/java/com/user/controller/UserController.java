package com.user.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.entities.User;
import com.user.service.UserServices;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/users")
public class UserController {
	
    private Logger logger = LoggerFactory.getLogger(UserController.class);
	 
	@Autowired
	private UserServices userServices;
	
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user){
		User responseUser = userServices.createUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
	}
	
	@GetMapping
	public ResponseEntity<List<User>> getAllUsers(){
		List<User> users = userServices.getAllUser(null);
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}
	
	@GetMapping("{userId}")
    @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
	public ResponseEntity<User> getUserById(@PathVariable String userId ){
        logger.info("Get Single User Handler: UserController");
		User users = userServices.getUserById(userId);
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}
	
	//creating fall back  method for circuitbreaker
    public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex) {
        logger.info("Fallback is executed because service is down : ", ex.getMessage());
        User user = User.builder()
                .email("dummy@gmail.com")
                .name("Dummy")
                .about("This user is created dummy because some service is down")
                .id("141234")
                .build();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
