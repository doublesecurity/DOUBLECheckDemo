package com.doublesecuritytechnologies.doublecheckdemo.controller;

import java.io.Serializable;
import java.security.Principal;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.doublesecuritytechnologies.doublecheckdemo.ejb.UserManager;
import com.doublesecuritytechnologies.doublecheckdemo.model.User;
import com.doublesecuritytechnologies.doublecheckdemo.worker.DCVerifyPassword;

@Named
@SessionScoped
public class AccessController implements Serializable {
	
	//private User user;
	
	private String userIdfr;
	private String password;
	private String doubleCheckPassword;
	private String newPassword;
	private String verifyPassword;
	private User user;
	private Principal userPrincipal; 
	
	@EJB(beanName="ejb/DCDemo/UserManager")
	private UserManager userManager;
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("com.doublesecuritytechnologies.doublecheckdemo.controller");
	
	
	public AccessController() {
		super();
		log.finest("ADSS: AccessController (backing bean) instantiated.");
	}
	
	
	////////////////////////////////////////
	/////   PROPERTIES     /////////////////
	////////////////////////////////////////
	
	public String getUserIdfr() {
		return userIdfr;
	}
	public void setUserIdfr(String userId) {
		this.userIdfr = userId;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getDoubleCheckPassword() {
		return doubleCheckPassword;
	}
	public void setDoubleCheckPassword(String doubleCheckPassword) {
		this.doubleCheckPassword = doubleCheckPassword;
	}
	
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	public String getVerifyPassword() {
		return verifyPassword;
	}
	public void setVerifyPassword(String verifyPassword) {
		this.verifyPassword = verifyPassword;
	}
	
	public User getUser() {
		if (user == null && isUserLoggedIn() && userPrincipal != null) {
			user = userManager.getUser(userPrincipal.getName());
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getSessionMap().put("user", user);
		}
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public boolean isUserLoggedIn() {
		boolean loggedIn = false;
		
		FacesContext context = FacesContext.getCurrentInstance();
		userPrincipal = context.getExternalContext().getUserPrincipal();
		if (userPrincipal != null) {
			loggedIn = true;
		}
		
		return loggedIn;
	}
	
	
	////////////////////////////////////////
	/////   ACTIONS     ////////////////////
	////////////////////////////////////////
	
	public String login() {
		log.fine("DCDemo: Login user [" + getUserIdfr() + "]");
		
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		
		try {
			request.login(getUserIdfr(), getPassword());
			if (!(new DCVerifyPassword().verifyDoubleCheckPassword(getUserIdfr(),getDoubleCheckPassword()))) {
				log.fine("DCDemo: Login failed. (DOUBLECheck verification)");
				externalContext.invalidateSession();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Login", "Invalid Login: Credentials are not valid."));
				return null;
			}
			setUser(userManager.getUser(getUserIdfr()));
			externalContext.getSessionMap().put("user", getUser());
			return "secure/digital-bank?faces-redirect=true";  // TODO: Check which page to show next (bank, shop, api, etc...)
		} catch (ServletException e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Login", "Invalid Login: Credentials are not valid."));
		}
		
		return null;
	}

	public String logout() {
		//log.fine("DCDemo: Logging out [" + user.username + "]");
		log.fine("DCDemo: Logging out...");
		
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().invalidateSession();
		setUser(null);
		
		return "/pages/index?faces-redirect=true";
	}
	
	
	//// TODO: Pending functionality for several 'user management' activities...
	private String recoverPasswordEmail;
	public String getRecoverPasswordEmail() {
		return this.recoverPasswordEmail;
	}
	public void setRecoverPasswordEmail(String recoverPasswordEmail) {
		this.recoverPasswordEmail = recoverPasswordEmail;
	}
	
	public String sendPassword() {
		return null;
	}
	
	
	  /////////////////////////////
	 ///  Ancillary functions  ///
	/////////////////////////////
	
	
}
