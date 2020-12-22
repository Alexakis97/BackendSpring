package com.aed.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ambulance")
public class Ambulance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ambulance_id")
	private int ambulanceId;

	@Column(name = "user_name")
	private String username; //gia username tha valoyme ton arithmo tou asthenoforou

	@Column(name = "password")
	private String password;
	
	@Column(name = "license_plate")
	private String licensePlate;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "imei")
	private String imei;
	

	@Column(name = "is_active")
	private Integer isActive;

	@Column(name = "registration_date")
	private String registrationDate;

	@Column(name = "lat")
	private double lat;

	@Column(name = "lon")
	private double lon;
	
	@ManyToOne
	@JoinColumn(name = "hospital_id")
	private Hospital hospital;
	
	
	
	

	public Ambulance() {
		super();
	}
	
	
	

	public Hospital getHospital() {
		return this.hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}
	


	public String getImei() {
		return imei;
	}




	public void setImei(String imei) {
		this.imei = imei;
	}




	public String getCity() {
		return city;
	}




	public void setCity(String city) {
		this.city = city;
	}



	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getLicensePlate() {
		return licensePlate;
	}


	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}



	public int getAmbulanceId() {
		return this.ambulanceId;
	}

	public void setAmbulanceId(int ambulanceId) {
		this.ambulanceId = ambulanceId;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	


	public Integer getIsActive() {
		return isActive;
	}


	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}


	public String getRegistrationDate() {
		return registrationDate;
	}


	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}


	public double getLat() {
		return lat;
	}


	public void setLat(double lat) {
		this.lat = lat;
	}


	public double getLon() {
		return lon;
	}


	public void setLon(double lon) {
		this.lon = lon;
	}


	@Override
	public String toString() {
		return "{" +
			" ambulanceId='" + ambulanceId + "'" +
			", username='" + username + "'" +
			", password='" + password + "'" +
			", licensePlate='" + licensePlate + "'" +
			", city='" + city + "'" +
			", imei='" + imei + "'" +
			", isActive='" + isActive + "'" +
			", registrationDate='" + registrationDate + "'" +
			", lat='" + lat + "'" +
			", lon='" + lon + "'" +
			", hospital='" + hospital + "'" +
			"}";
	}


}
