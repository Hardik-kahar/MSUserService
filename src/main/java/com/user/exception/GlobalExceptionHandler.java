package com.user.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.user.payload.ApiResponse;

// for handling centralized exception 
@RestControllerAdvice 
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException exception){
		 String message =  exception.getMessage();
		 ApiResponse response = ApiResponse.builder().message(message).success(true).status(HttpStatus.NOT_FOUND).build();
		 return new ResponseEntity<ApiResponse>(response, HttpStatus.NOT_FOUND);
	}
	
//	 whene don't wanna user apiResponse class
//	@ExceptionHandler(ResourceNotFoundException.class)
//	public ResponseEntity< Map<String, Object>> notFoundHandler(ResourceNotFoundException exception){
//		HashMap<String, Object> map = new HashMap<>();
//		map.put("message", exception.getMessage());
//		map.put("success", false);
//		map.put("status", HttpStatus.NOT_FOUND);
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
//	}
}
