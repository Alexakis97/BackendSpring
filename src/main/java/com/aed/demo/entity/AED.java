package com.aed.demo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="aed")
public class AED implements Serializable{
	
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="aed_id")
	private int aed_id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="description")
	private String description;
	
	@Column(name="lat")
	private String lat;
	
	@Column(name="lon")
	private String lon;
	
	@Column(name="photo")
	private String photo;
	
	@Column(name="available")
	private String available;
	
	@Column(name="city")
	private String city;
	
	@Column(name="country")
	private String country;
	
	@Column(name="address")
	private String address;
	
	@Column(name="isActive")
	private int isActive;
	
	@Column(name="building")
	private String building;
	
	@Column(name="floor")
	private String floor;
	
	@Column(name="brand")
	private String brand;
	
	@Column(name="type")
	private String type;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="periphery")
	private String periphery;
	
	
	@Column(name="department")
	private String department;
	
	@Column(name="municipality")
	private String municipality;
	
	
	
	
	public String getPeriphery() {
		return periphery;
	}


	public void setPeriphery(String periphery) {
		this.periphery = periphery;
	}


	public String getDepartment() {
		return department;
	}


	public void setDepartment(String department) {
		this.department = department;
	}


	public String getMunicipality() {
		return municipality;
	}


	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}


	@ManyToOne
	@JoinColumn(name = "user_id_fk")
	private User user;
	

	 
	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}
	
	


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}



	public String getBuilding() {
		return building;
	}

	
	public void setBuilding(String building) {
		this.building = building;
	}



	public String getFloor() {
		return floor;
	}


	public void setFloor(String floor) {
		this.floor = floor;
	}


	public String getBrand() {
		return brand;
	}


	public void setBrand(String brand) {
		this.brand = brand;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public int getIsActive() {
		return isActive;
	}



	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}



	public AED() {
		
	}
	
	

	public String getAvailable() {
		return available;
	}



	public void setAvailable(String available) {
		this.available = available;
	}



	public String getCity() {
		return city;
	}



	public void setCity(String city) {
		this.city = city;
	}




	public int getAed_id() {
		return aed_id;
	}


	public void setAed_id(int aed_id) {
		this.aed_id = aed_id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}


	@Override
	public String toString() {
		return "AED [aed_id=" + aed_id + ", name=" + name + ", description=" + description + ", lat=" + lat + ", lon="
				+ lon + ", photo=" + photo + ", available=" + available + ", city=" + city + ", country=" + country
				+ ", address=" + address + ", isActive=" + isActive + ", building=" + building + ", floor=" + floor
				+ ", brand=" + brand + ", type=" + type + ", phone=" + phone + ", user=" + user + "]";
	}


}