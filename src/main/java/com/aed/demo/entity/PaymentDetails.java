package com.aed.demo.entity;

public class PaymentDetails {
	
	private String ambulances;
	private String users;
	private String admins;
	private String years;
	
	
	

	public PaymentDetails() {}



	public PaymentDetails(String ambulances, String users, String admins, String years) {
		super();
		this.ambulances = ambulances;
		this.users = users;
		this.admins = admins;
		this.years = years;
	}
	
	
	
	
	public String getAmbulances() {
		return ambulances;
	}
	public void setAmbulances(String ambulances) {
		this.ambulances = ambulances;
	}
	public String getUsers() {
		return users;
	}
	public void setUsers(String users) {
		this.users = users;
	}
	public String getAdmins() {
		return admins;
	}
	public void setAdmins(String admins) {
		this.admins = admins;
	}
	public String getYears() {
		return years;
	}
	public void setYears(String years) {
		this.years = years;
	}
	
	
	
	

	
	
	
	

}
