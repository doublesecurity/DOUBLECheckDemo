package com.doublesecuritytechnologies.doublecheckdemo.controller;

public class BankTransactionStatus {
	
	private String name;
	private String color;
	private String icon;
	
	public BankTransactionStatus() {
		super();
	}
	
	public BankTransactionStatus(String name, String color, String icon) {
		this();
		setName(name);
		setColor(color);
		setIcon(icon);
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
}
