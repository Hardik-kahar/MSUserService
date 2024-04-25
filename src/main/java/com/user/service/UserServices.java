package com.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.user.entities.User;

public interface UserServices {

//	create user
	User createUser(User user);
	
//	get all user;
	List<User> getAllUser(User user);
	
//	get User by id
	User getUserById(String userId);
}
