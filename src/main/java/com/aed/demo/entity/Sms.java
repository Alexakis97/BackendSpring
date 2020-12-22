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
@Table(name="sms")
public class Sms implements Serializable{


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="idsms")
	private int idsms;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="name")
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "user_id_fk")
	private User user;

	
	public Sms() {
		super();
	}
	
	public Sms(int idsms, String phone, String name, User user) {
		super();
		this.idsms = idsms;
		this.phone = phone;
		this.name = name;
		this.user = user;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public int getIdsms() {
		return idsms;
	}

	public void setIdsms(int idsms) {
		this.idsms = idsms;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	@Override
	public String toString() {
		return "Sms [idsms=" + idsms + ", phone=" + phone + ", user=" + user + "]";
	}
	
	
}
