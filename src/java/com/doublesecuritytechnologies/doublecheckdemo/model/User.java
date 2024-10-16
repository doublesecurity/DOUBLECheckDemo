package com.doublesecuritytechnologies.doublecheckdemo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="User")
@Access(AccessType.PROPERTY)
@NamedQueries({
	@NamedQuery(name="User.all", query="SELECT u FROM User u"),
	@NamedQuery(name="User.byIdfr", query="SELECT u FROM User u WHERE u.idfr = :idfr")
})
public class User implements Serializable {
	
	private int Id;
	private String idfr;
	private String password;
	private String name;
	private String lasname;
	private String email;
	private String phoneNumber;
	private byte[] photo;
	private UserLifeCycleStatus status;
	private Date timestamp;
	private List<Role> roles;
	
	private static Logger log = Logger.getLogger("com.doublesecuritytechnologies.doublecheckdemo.model");
	private static final long serialVersionUID = 1L;
	
	public enum UserLifeCycleStatus {
		CREATED,
		ACTIVE,
		SUSPENDED,
		DELETED
	}
	
	
	public User() {
		super();
		log.finest("DCDemo: User (model bean) instantiated.");
	}
	
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Id")
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	
	@Column(name="IDFR")
	public String getIdfr() {
		return idfr;
	}
	public void setIdfr(String idfr) {
		this.idfr = idfr;
	}
	
	@Column(name="Password")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name="Name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="Lastname")
	public String getLasname() {
		return lasname;
	}
	public void setLasname(String lasname) {
		this.lasname = lasname;
	}
	
	@Column(name="Email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name="PhoneNumber")
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Lob
	@Column(name="Photo")
	public byte[] getPhoto() {
		return photo;
	}
	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name="LCStatus")
	public UserLifeCycleStatus getStatus() {
		return status;
	}
	public void setStatus(UserLifeCycleStatus status) {
		this.status = status;
	}
	
	@Column(name="Timestamp")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL)
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public void addRole(Role role) {
		if (getRoles() == null) {
			setRoles(new ArrayList<Role>());
		}
		getRoles().add(role);
	}
	public void removeRole(Role role) {
		if (getRoles() != null && getRoles().contains(role)) {
			getRoles().remove(role);
		}
	}
	
	
	@Override
	public String toString() {
		return getName() + " " + getLasname();
	}
	
}
