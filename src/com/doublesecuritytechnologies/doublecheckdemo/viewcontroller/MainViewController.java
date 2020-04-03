package com.doublesecuritytechnologies.doublecheckdemo.viewcontroller;

import java.security.Principal;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name="main")
@SessionScoped
public class MainViewController {
	
	private static Logger log = Logger.getLogger("com.doublesecuritytechnologies.doublecheckdemo.viewcontroller");
	
	public MainViewController() {
		super();
		log.finest("ADSS: MainViewController (backing bean) instantiated.");
	}
	
	
	public boolean isUserLoggedIn() {
		boolean loggedIn = false;
		
		log.fine("ADSS: Checking if logged in...");
		FacesContext context = FacesContext.getCurrentInstance();
		Principal userPrincipal = context.getExternalContext().getUserPrincipal();
		if (userPrincipal != null) {
			loggedIn = true;
		}
		
		return loggedIn;
	}
	
	
	public String logout() {
		
		log.fine("ADSS: Logging out...");
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().invalidateSession();
		
		return "/pages/index.xhtml?faces-redirect=true";
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

}
