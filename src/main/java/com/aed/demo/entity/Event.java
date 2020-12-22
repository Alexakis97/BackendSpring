package com.aed.demo.entity;

import java.io.Serializable;

import javax.persistence.*;


@Entity
@Table(name="event")
public class Event implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="event_id")
	private int event_id;
	
	@Column(name="lat")
	private double lat;
	
	@Column(name="lon")
	private double lon;
	
	@Column(name="status")
	private String status;
	
	@ManyToOne
	@JoinColumn(name = "user_id_fk")
	private User user;
	
	@Column(name="persons")
	private int persons;
	
	@Column(name="public_ip")
	private String publicIp;
	
	@Column(name="creation_date")
	private String creationDate;  //timestamp dimiourgias peristatikou apo ton xristi
	
	@Column(name="assignment_date")
	private String assignmentDate; //timestamp anathesis peristatikou se asthenoforo
	
	@Column(name="receipt_date")
	private String receiptDate; //timestamp paralavis peristatikou
	
	@Column(name="finished_date")
	private String finishedDate; //timestamp oloklirosis peristatikou
	
	@Column(name="address")
	private String address;
	
	@Column(name="city")
	private String city;
	
	@Column(name="postal_code")
	private String postalCode;
	
	@Column(name="country")
	private String country;
	
	@Column(name="doorbell")
	private String doorbell;
	
	@Column(name="floor")
	private String floor;
	
	@Column(name="type")
	private String type;
	
	@Column(name="breathe")
	private boolean breathe;
	
	@Column(name="senses")
	private boolean senses;
	
	@Column(name="mind")
	private boolean mind;
	
	@Column(name="blood")
	private boolean blood;
	
	@Column(name="age_victim")
	private int ageVictim;
	
	@Column(name="the_same_user")
	private boolean theSameUser;
	
	@ManyToOne
	@JoinColumn(name = "ambulance_id_fk")
	private Ambulance ambulance;
	

	@Column(name="periphery")
	private String periphery;
	
	
	@Column(name="department")
	private String department;
	
	@Column(name="municipality")
	private String municipality;
	
	@ManyToOne
	@JoinColumn(name = "hospital_id")
	private Hospital hospital;
	
	
	public String getPeriphery() {
		return periphery;
	}




	public void setPeriphery(String periphery) {
		this.periphery = periphery;
	}
	
	
	




	public String getMunicipality() {
		return municipality;
	}




	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}




	public String getDepartment() {
		return department;
	}




	public void setDepartment(String department) {
		this.department = department;
	}




	public Event() {
		
	}
	
	
	
	
	public String getType() {
		return type;
	}




	public void setType(String type) {
		this.type = type;
	}




	public String getCreationDate() {
		return creationDate;
	}




	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}





	public String getAssignmentDate() {
		return assignmentDate;
	}





	public void setAssignmentDate(String assignmentDate) {
		this.assignmentDate = assignmentDate;
	}





	public String getReceiptDate() {
		return receiptDate;
	}





	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}





	public String getFinishedDate() {
		return finishedDate;
	}





	public void setFinishedDate(String finishedDate) {
		this.finishedDate = finishedDate;
	}





	public Ambulance getAmbulance() {
		return ambulance;
	}



	public void setAmbulance(Ambulance ambulance) {
		this.ambulance = ambulance;
	}



	public int getAgeVictim() {
		return ageVictim;
	}


	public void setAgeVictim(int ageVictim) {
		this.ageVictim = ageVictim;
	}


	

	public boolean isTheSameUser() {
		return theSameUser;
	}

	public void setTheSameUser(boolean theSameUser) {
		this.theSameUser = theSameUser;
	}

	public boolean isBreathe() {
		return breathe;
	}


	public void setBreathe(boolean breathe) {
		this.breathe = breathe;
	}


	public boolean isSenses() {
		return senses;
	}


	public void setSenses(boolean senses) {
		this.senses = senses;
	}


	public boolean isMind() {
		return mind;
	}


	public void setMind(boolean mind) {
		this.mind = mind;
	}


	public boolean isBlood() {
		return blood;
	}


	public void setBlood(boolean blood) {
		this.blood = blood;
	}



	public String getPublicIp() {
		return publicIp;
	}


	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDoorbell() {
		return doorbell;
	}

	public void setDoorbell(String doorbell) {
		this.doorbell = doorbell;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}


	public int getPersons() {
		return persons;
	}

	public void setPersons(int persons) {
		this.persons = persons;
	}

	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public int getEvent_id() {
		return event_id;
	}

	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}



	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	


	public Hospital getHospital() {
		return hospital;
	}




	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}



	public double getLat() {
		return this.lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return this.lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public boolean getBreathe() {
		return this.breathe;
	}


	public boolean getSenses() {
		return this.senses;
	}


	public boolean getMind() {
		return this.mind;
	}


	public boolean getBlood() {
		return this.blood;
	}


	public boolean getTheSameUser() {
		return this.theSameUser;
	}




	@Override
	public String toString() {
		return "{" +
			" event_id='" + event_id + "'" +
			", lat='" + lat + "'" +
			", lon='" + lon + "'" +
			", status='" + status + "'" +
			", user='" + user + "'" +
			", persons='" + persons + "'" +
			", publicIp='" + publicIp + "'" +
			", creationDate='" + creationDate + "'" +
			", assignmentDate='" + assignmentDate + "'" +
			", receiptDate='" + receiptDate + "'" +
			", finishedDate='" + finishedDate + "'" +
			", address='" + address + "'" +
			", city='" + city + "'" +
			", postalCode='" + postalCode + "'" +
			", country='" + country + "'" +
			", doorbell='" + doorbell + "'" +
			", floor='" + floor + "'" +
			", type='" + type + "'" +
			", breathe='" + breathe + "'" +
			", senses='" + senses + "'" +
			", mind='" + mind + "'" +
			", blood='" + blood + "'" +
			", ageVictim='" + ageVictim + "'" +
			", theSameUser='" + theSameUser + "'" +
			", ambulance='" + ambulance + "'" +
			", periphery='" + periphery + "'" +
			", department='" + department + "'" +
			", municipality='" + municipality + "'" +
			", hospital='" + hospital + "'" +
			"}";
	}




	


	
	
		
}
