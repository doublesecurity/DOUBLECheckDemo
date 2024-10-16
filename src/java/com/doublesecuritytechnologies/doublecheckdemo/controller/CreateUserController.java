package com.doublesecuritytechnologies.doublecheckdemo.controller;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;

import com.doublesecuritytechnologies.doublecheck.model.DCUser;
import com.doublesecuritytechnologies.doublecheckdemo.worker.DCClientManager;

@Named("createUser")
@ViewScoped
public class CreateUserController implements Serializable {
	
	private String tenantIdfr;
	private String appIdfr;
	private String userIdfr;
	private String userName;
	private String userPhoneNumber;
	private HtmlOutputText result;

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("com.doublesecuritytechnologies.doublecheckdemo.controller");
	
	
	public CreateUserController() {
		super();
		log.finest("ADSS: CreateUserController (backing bean) instantiated.");
	}
	
	
	////////////////////////////////////////
	/////   PROPERTIES     /////////////////
	////////////////////////////////////////
	
	public String getTenantIdfr() {
		return tenantIdfr;
	}
	public void setTenantIdfr(String idfr) {
		this.tenantIdfr = idfr;
	}
	
	public String getAppIdfr() {
		return appIdfr;
	}
	public void setAppIdfr(String appIdfr) {
		this.appIdfr = appIdfr;
	}
	
	public String getUserIdfr() {
		return userIdfr;
	}
	public void setUserIdfr(String userIdfr) {
		this.userIdfr = userIdfr;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserPhoneNumber() {
		return userPhoneNumber;
	}
	public void setUserPhoneNumber(String userPhoneNumber) {
		this.userPhoneNumber = userPhoneNumber;
	}
	
	public HtmlOutputText getResult() {
		return result;
	}
	public void setResult(HtmlOutputText result) {
		this.result = result;
	}


	////////////////////////////////////////
	/////   ACTIONS     ////////////////////
	////////////////////////////////////////
	
	public String createUser() {
		log.fine("ADSS: Adding user [" + getTenantIdfr() + "/" + getAppIdfr() + "/" + getUserIdfr() + "].");
		
		// Clear previous results
		result.setRendered(false);
		result.setValue(null);

		// Prepare invocation to ADSS Web Service
		DCUser newUser = new DCUser(getUserIdfr(), getUserName());
		if (getUserPhoneNumber() != null) {
			newUser.setPhoneNumber(getUserPhoneNumber()); 
		}
		DCClientManager adssCM = new DCClientManager();
		adssCM.setPath("account/{tenantIdfr}/{appIdfr}/user");
		adssCM.resolveTemplate("tenantIdfr", getTenantIdfr());
		adssCM.resolveTemplate("appIdfr", getAppIdfr());
		Invocation.Builder adssInvocationBuilder = adssCM.getTarget().request()
                                                                     .accept("application/xml");
		Invocation adssCreateUser = adssInvocationBuilder.buildPost(Entity.xml(newUser));   // POST Request
		
		// Invoke ADSS Web Service
		try {
			DCUser createdUser = adssCreateUser.invoke(DCUser.class);

			result.setValue(createdUser.getIdfr() + "[" + createdUser.getId() + "]: " + createdUser.getName());
			result.setRendered(true);
			
			FacesMessage message = new FacesMessage("ADSS: DCUser created:" + createdUser);
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			FacesContext.getCurrentInstance().addMessage(null, message);

			log.fine("ADSS: DCUser created: [" + createdUser + "]");
			
		} catch (Exception e) {
			log.warning("ADSS: Exception while creating DCUser: [" + e.getMessage() + "]");
			FacesMessage message = new FacesMessage("ADSS: Exception while creating DCUser: [" + e.getMessage() + "]");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

		adssCM.close();
		return null;
	}
	
}
