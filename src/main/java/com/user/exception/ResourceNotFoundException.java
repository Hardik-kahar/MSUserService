package com.user.exception;

public class ResourceNotFoundException extends RuntimeException{

	/**
	 * auto generated
	 */
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException() {
		super("Resource not found on server!");
	}
	
	public ResourceNotFoundException(String message) {
		super(message);
	}
	
	
}
