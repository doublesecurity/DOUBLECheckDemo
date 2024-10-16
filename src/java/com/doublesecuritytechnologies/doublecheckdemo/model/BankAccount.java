package com.doublesecuritytechnologies.doublecheckdemo.model;

public class BankAccount {
	
	private String type;
	private String idfr;
	private double balance;
	
	public BankAccount(String type, String idfr, double balance) {
		setType(type);
		setIdfr(idfr);
		setBalance(balance);
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getIdfr() {
		return idfr;
	}
	public void setIdfr(String idfr) {
		this.idfr = idfr;
	}
	
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
}
