package com.user.controller;

import java.util.List;

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

@RestController
@RequestMapping("/users")
public class UserController {

	 
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
	public ResponseEntity<User> getUserById(@PathVariable String userId ){
		User users = userServices.getUserById(userId);
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}
}
