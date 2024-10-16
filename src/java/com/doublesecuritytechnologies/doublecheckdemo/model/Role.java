package com.doublesecuritytechnologies.doublecheckdemo.model;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="Role")
@Access(AccessType.PROPERTY)
public class Role implements Serializable {
	
	private int id;
	private String idfr;
	private RoleType role;
	private User user;
	
	private static Logger log = Logger.getLogger("com.doublesecuritytechnologies.doublecheckdemo.model");
	private static final long serialVersionUID = 1L;
	
	public enum RoleType {
		USER,
		POWERUSER,
		MANAGER,
		ADMIN
	}
	
	public Role() {
		super();
		log.finest("DCDemo: Role (model bean) instantiated.");
	}
	
	
	@Id
	@Column(name="Id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="IDFR")
	public String getIdfr() {
		return idfr;
	}
	public void setIdfr(String idfr) {
		this.idfr = idfr;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name="Role")
	public RoleType getRole() {
		return role;
	}
	public void setRole(RoleType role) {
		this.role = role;
	}
	
	@ManyToOne
	@JoinColumn(name="UserId")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
