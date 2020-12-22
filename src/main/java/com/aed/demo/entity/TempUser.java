package com.aed.demo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="temp_user")
public class TempUser  implements Serializable{
	

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="idtemp_user")
	private int idtemp_user;

	@Column(name="email")
	private String email;

	@Column(name="token")
	private String token;
	
	
	
	public int getIdtemp_user() {
		return idtemp_user;
	}

	public void setIdtemp_user(int idtemp_user) {
		this.idtemp_user = idtemp_user;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public TempUser(String email, String token) {
		super();
		this.email = email;
		this.token = token;
	}
	
	public TempUser() {
		super();
	}

	@Override
	public String toString() {
		return "TempUser [idtemp_user=" + idtemp_user + ", email=" + email + ", token=" + token + "]";
	}
	
	
	
	
	
	
	
	
	
	
}
