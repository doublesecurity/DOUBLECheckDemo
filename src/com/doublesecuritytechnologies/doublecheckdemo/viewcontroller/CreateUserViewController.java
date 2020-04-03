package com.doublesecuritytechnologies.doublecheckdemo.viewcontroller;

import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;

import com.doublesecuritytechnologies.doublecheckdemo.model.User;
import com.doublesecuritytechnologies.doublecheckdemo.worker.ADSSClientManager;

@ManagedBean(name="createUser")
@RequestScoped
public class CreateUserViewController {
	
	private String tenantIdfr;
	private String appIdfr;
	private String userIdfr;
	private String userName;
	private String userPhoneNumber;
	private HtmlOutputText result;

	private static Logger log = Logger.getLogger("com.doublesecuritytechnologies.doublecheckdemo.viewcontroller");
	
	
	public CreateUserViewController() {
		super();
		log.finest("ADSS: CreateUserViewController (backing bean) instantiated.");
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
		User newUser = new User(getUserIdfr(), getUserName());
		if (getUserPhoneNumber() != null) {
			newUser.setPhoneNumber(getUserPhoneNumber()); 
		}
		ADSSClientManager adssCM = new ADSSClientManager();
		adssCM.setPath("account/{tenantIdfr}/{appIdfr}/user");
		adssCM.resolveTemplate("tenantIdfr", getTenantIdfr());
		adssCM.resolveTemplate("appIdfr", getAppIdfr());
		Invocation.Builder adssInvocationBuilder = adssCM.getTarget().request()
                                                                     .accept("application/xml");
		Invocation adssCreateUser = adssInvocationBuilder.buildPost(Entity.xml(newUser));   // POST Request
		
		// Invoke ADSS Web Service
		try {
			User createdUser = adssCreateUser.invoke(User.class);

			result.setValue(createdUser.getIdfr() + "[" + createdUser.getId() + "]: " + createdUser.getName());
			result.setRendered(true);
			
			FacesMessage message = new FacesMessage("ADSS: User created:" + createdUser);
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			FacesContext.getCurrentInstance().addMessage(null, message);

			log.fine("ADSS: User created: [" + createdUser + "]");
			
		} catch (Exception e) {
			log.warning("ADSS: Exception while creating User: [" + e.getMessage() + "]");
			FacesMessage message = new FacesMessage("ADSS: Exception while creating User: [" + e.getMessage() + "]");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

		adssCM.close();
		return null;
	}
	
}
