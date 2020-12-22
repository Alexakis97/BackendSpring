package com.aed.demo.entity;


import java.io.Serializable;

import javax.persistence.*;

@Embeddable
public class HasReports implements Serializable  {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "aed_id")
    private int aedid;

     @Column(name = "user_id")
    private int userid;

     

	public int getAedid() {
		return aedid;
	}



	public void setAedid(int aedid) {
		this.aedid = aedid;
	}



	public int getUserid() {
		return userid;
	}



	public void setUserid(int userid) {
		this.userid = userid;
	}



	public HasReports() {
		super();
	}



	
	
     
     
}