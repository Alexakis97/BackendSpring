package com.aed.demo.entity;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="reports")
public class Reports implements Serializable{


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@Column(name="report_id")
	private HasReports id= new HasReports();

	
	@Column(name="status")
	private String status;
	
	@Column(name="comment")
	private String comment;
	
	
	 @ManyToOne
	 @MapsId("userid") //This is the name of attr in EmployerDeliveryAgentPK class
	 @JoinColumn(name = "user_id")
	 private User user;

		
	 @ManyToOne
	 @MapsId("aedid") //This is the name of attr in EmployerDeliveryAgentPK class
	 @JoinColumn(name = "aed_id")
	 private AED aed;


	public HasReports getId() {
		return id;
	}


	public void setId(HasReports id) {
		this.id = id;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public AED getAed() {
		return aed;
	}


	public void setAed(AED aed) {
		this.aed = aed;
	}


	public Reports() {
		super();
	}
	

	 

	
		
}


