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
@Table(name="marketing")
public class MarketingObject {
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="marketing_id")
    private int marketingId;

    @Column(name="title")
    private String title;
    
    @Column(name="body")
    private String body;

    @Column(name="receiver")
    private String receiver;

    @Column(name="date_created")
    private String dateCreated;

    @ManyToOne
	@JoinColumn(name = "user_id_fk")
	private User user;


    public MarketingObject() {
    }

    public MarketingObject(String title, String body, String receiver, String dateCreated) {
        this.title = title;
        this.body = body;
        this.receiver = receiver;
        this.dateCreated = dateCreated;
    }


    public MarketingObject(int marketingId, String title, String body, String receiver, String dateCreated, User user) {
        this.marketingId = marketingId;
        this.title = title;
        this.body = body;
        this.receiver = receiver;
        this.dateCreated = dateCreated;
        this.user = user;
    }
   


    public int getMarketingId() {
        return this.marketingId;
    }

    public void setMarketingId(int marketingId) {
        this.marketingId = marketingId;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public MarketingObject title(String title) {
        this.title = title;
        return this;
    }

    public MarketingObject body(String body) {
        this.body = body;
        return this;
    }

    public MarketingObject receiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public MarketingObject dateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    @Override
    public String toString() {
        return "{" + " title='" + getTitle() + "'" + ", body='" + getBody() + "'" + ", receiver='" + getReceiver() + "'"
                + ", dateCreated='" + getDateCreated() + "'" + "}";
    }

}
