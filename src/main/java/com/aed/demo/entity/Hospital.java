package com.aed.demo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="hospital")
public class Hospital implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="hospital_id")
	private int hospital_id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="lat")
	private double lat;
	
	@Column(name="lon")
	private double lon;
	
	@Column(name="address")
	private String address;
	
	@Column(name="country")
	private String country;

	@Column(name="phone")
	private String phone;
	
	
	@Column(name="cluster_municipality")
	private String clusterMunicipality;
	
	@Column(name="event_processor")
	private String eventProcessor;
	

	

	public String getEventProcessor() {
		return eventProcessor;
	}

	public void setEventProcessor(String eventProcessor) {
		this.eventProcessor = eventProcessor;
	}

	public String getClusterMunicipality() {
		return clusterMunicipality;
	}

	public void setClusterMunicipality(String clusterMunicipality) {
		this.clusterMunicipality = clusterMunicipality;
	}

	public int getHospital_id() {
		return hospital_id;
	}

	public void setHospital_id(int hospital_id) {
		this.hospital_id = hospital_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "Hospital [hospital_id=" + hospital_id + ", name=" + name + ", lat=" + lat + ", lon=" + lon
				+ ", address=" + address + ", country=" + country + ", phone=" + phone + "]";
	}

	public Hospital(int hospital_id, String name, double lat, double lon, String address, String country,
			String phone,String eventProcessor) {
		super();
		this.hospital_id = hospital_id;
		this.name = name;
		this.lat = lat;
		this.lon = lon;
		this.address = address;
		this.country = country;
		this.phone = phone;
		this.eventProcessor=eventProcessor;
	}

	public Hospital() {
		super();
	}
	
	
	
	


}
