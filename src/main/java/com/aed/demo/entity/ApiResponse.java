package com.aed.demo.entity;

public class ApiResponse {

	private int statusCode;
	private String message;
	private User user;
	private Ambulance ambulance;
	
	
	public ApiResponse(int statusCode, String message) {
		super();
		this.statusCode = statusCode;
		this.message = message;
	}
	
	public ApiResponse(int statusCode, User user) {
		super();
		this.statusCode = statusCode;
		this.user = user;
	}
	
	public ApiResponse(int statusCode, Ambulance ambulance) {
		super();
		this.statusCode = statusCode;
		this.ambulance = ambulance;
	}


	public ApiResponse(int statusCode, String message, User user) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.user = user;
	}
	
	public ApiResponse(int statusCode, String message, Ambulance ambulance) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.ambulance = ambulance;
	}

	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Ambulance getAmbulance() {
		return ambulance;
	}

	public void setAmbulance(Ambulance ambulance) {
		this.ambulance = ambulance;
	}


	
}
