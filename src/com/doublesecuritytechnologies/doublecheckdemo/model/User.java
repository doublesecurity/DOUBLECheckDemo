package com.doublesecuritytechnologies.doublecheckdemo.model;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: Core class of the Data Model for the Double Security Service.
 * 
 * A User represents a person that is a token holder. It is a user of some 
 * application within any of the service's clients that has a Token in their 
 * device and that any of the clients can relate to and identify uniquely.
 * 
 * @author José Ramón Revuelta
 *
 */
@XmlRootElement(name="user")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class User implements Serializable {
	
	private int id;
	private String idfr;
	private String name;
	private String phoneNumber;
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("com.doublesecuritytechnologies.doublecheckdemo.model");

	
	public User() {
		super();
		log.finest("ADSS: User (managed model bean) instantiated.");
	}
	
	public User(String idfr, String name) {
		this();
		setIdfr(idfr);
		setName(name);
	}
	
	
	/**
	 * id: Property with the internal identity of this entity.
	 */
	@XmlAttribute
	public int getId() {
		return id;
	}
	protected void setId(int id) {
		this.id = id;
	}
	
	
	/**
	 * idfr: Property with the external identity of this entity. 
	 * 
	 * This would be the appropriate unique 'userId' (unique within the Application or Tenant).
	 * Usually a good identifier is the user's email, or at least the 'user' part of the email
	 * address (sans the domain).
	 */
	@XmlElement(name="idfr")
	public String getIdfr() {
		return idfr;
	}
	public void setIdfr(String idfr) {
		this.idfr = idfr;
	}
	
	
	/**
	 * name: Property that fully describes the user. Optional.
	 * 
	 * It may be the proper name of the person. 
	 */
	@XmlElement(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * phoneNumber: Property that holds the phoneNumber to be used for SMS. Optional.
	 * 
	 * It has to be in the proper E.164 format (+xxx xxx xxxx xxxx - without spaces). 
	 */
	@XmlElement(name="phoneNumber")
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
	@Override
	public String toString() {
		String result = null;
		
		try {
			JAXBContext ctx = JAXBContext.newInstance(User.class);
			Marshaller m = ctx.createMarshaller();
			StringWriter sw = new StringWriter();
			m.marshal(this, sw);
			result = sw.toString();
		} catch (Exception e) {
			log.warning("ADSS: No XML/String representation for User [" + this.hashCode() + "].");
		}
		
		return result;
	}
	
}
