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
@Table(name = "payment")
public class Payment implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private int payment_id;

    @Column(name = "subscription_name")
    private String subscriptionName;

    @Column(name = "amount")
    private int amount;

    @Column(name = "payment_date")
    private String paymentDate;

    @Column(name = "user_unit")
    private int userUnit;

    @Column(name = "ambulances_unit")
    private int ambulancesUnit;

    @Column(name = "master_unit")
    private int masterUnit;

    @Column(name = "user_price")
    private double userPrice;

    @Column(name = "ambulances_price")
    private double ambulancesPrice;

    @Column(name = "master_price")
    private double masterPrice;

    @Column(name = "years_of_subscription")
    private int yearsOfSubscription;

    @Column(name = "payment_interval")
    private String paymentInterval;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    public Payment() {
    }


    public Payment(int payment_id, String subscriptionName, int amount, String paymentDate, int userUnit, int ambulancesUnit, int masterUnit, double userPrice, double ambulancesPrice, double masterPrice, int yearsOfSubscription, String paymentInterval, User user,Hospital hospital) {
        this.payment_id = payment_id;
        this.subscriptionName = subscriptionName;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.userUnit = userUnit;
        this.ambulancesUnit = ambulancesUnit;
        this.masterUnit = masterUnit;
        this.userPrice = userPrice;
        this.ambulancesPrice = ambulancesPrice;
        this.masterPrice = masterPrice;
        this.yearsOfSubscription = yearsOfSubscription;
        this.paymentInterval = paymentInterval;
        this.user = user;
        this.hospital=hospital;
    }


    public Hospital getHospital() {
        return this.hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }



    public int getPayment_id() {
        return this.payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public String getSubscriptionName() {
        return this.subscriptionName;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPaymentDate() {
        return this.paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public int getUserUnit() {
        return this.userUnit;
    }

    public void setUserUnit(int userUnit) {
        this.userUnit = userUnit;
    }

    public int getAmbulancesUnit() {
        return this.ambulancesUnit;
    }

    public void setAmbulancesUnit(int ambulancesUnit) {
        this.ambulancesUnit = ambulancesUnit;
    }

    public int getMasterUnit() {
        return this.masterUnit;
    }

    public void setMasterUnit(int masterUnit) {
        this.masterUnit = masterUnit;
    }

    public double getUserPrice() {
        return this.userPrice;
    }

    public void setUserPrice(double userPrice) {
        this.userPrice = userPrice;
    }

    public double getAmbulancesPrice() {
        return this.ambulancesPrice;
    }

    public void setAmbulancesPrice(double ambulancesPrice) {
        this.ambulancesPrice = ambulancesPrice;
    }

    public double getMasterPrice() {
        return this.masterPrice;
    }

    public void setMasterPrice(double masterPrice) {
        this.masterPrice = masterPrice;
    }

    public int getYearsOfSubscription() {
        return this.yearsOfSubscription;
    }

    public void setYearsOfSubscription(int yearsOfSubscription) {
        this.yearsOfSubscription = yearsOfSubscription;
    }

    public String getPaymentInterval() {
        return this.paymentInterval;
    }

    public void setPaymentInterval(String paymentInterval) {
        this.paymentInterval = paymentInterval;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    @Override
    public String toString() {
        return "{" +
            " payment_id='" + payment_id + "'" +
            ", subscriptionName='" + subscriptionName + "'" +
            ", amount='" + amount + "'" +
            ", paymentDate='" + paymentDate + "'" +
            ", userUnit='" + userUnit + "'" +
            ", ambulancesUnit='" + ambulancesUnit + "'" +
            ", masterUnit='" + masterUnit + "'" +
            ", userPrice='" + userPrice + "'" +
            ", ambulancesPrice='" + ambulancesPrice + "'" +
            ", masterPrice='" + masterPrice + "'" +
            ", yearsOfSubscription='" + yearsOfSubscription + "'" +
            ", paymentInterval='" + paymentInterval + "'" +
            ", user='" + user + "'" +
            ", hospital='" + hospital + "'" +
            "}";
    }

   
   
   

}
