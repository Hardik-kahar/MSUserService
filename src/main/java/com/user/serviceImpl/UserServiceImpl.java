package com.user.serviceImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.entities.User;
import com.user.exception.ResourceNotFoundException;
import com.user.repositories.UserRespository;
import com.user.service.UserServices;

@Service
public class UserServiceImpl implements UserServices {

	@Autowired
	private UserRespository respository;
	
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
		return respository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User with given Id is not found on server! "+ userId));
	}

}
