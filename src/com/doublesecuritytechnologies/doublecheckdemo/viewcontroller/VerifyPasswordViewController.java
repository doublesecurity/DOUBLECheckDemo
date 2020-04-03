package com.doublesecuritytechnologies.doublecheckdemo.viewcontroller;

import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Invocation;

import com.doublesecuritytechnologies.doublecheckdemo.model.VerificationResponse;
import com.doublesecuritytechnologies.doublecheckdemo.worker.ADSSClientManager;

@ManagedBean(name="verifyPassword")
@RequestScoped
public class VerifyPasswordViewController {
	
	private String tenantIdfr;
	private String appIdfr;
	private String userIdfr;
	private String accountPassword;
	private HtmlOutputText result;
	
	private static Logger log = Logger.getLogger("com.doublesecuritytechnologies.doublecheckdemo.viewcontroller");
	
	
	public VerifyPasswordViewController() {
		super();
		log.finest("ADSS: VerifyPasswordViewController (backing bean) instantiated.");
	}
	
	
	////////////////////////////////////////
	/////   PROPERTIES     /////////////////
	////////////////////////////////////////
	
	public String getTenantIdfr() {
		return tenantIdfr;
	}
	public void setTenantIdfr(String tenantIdfr) {
		this.tenantIdfr = tenantIdfr;
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
	
	public String getAccountPassword() {
		return accountPassword;
	}
	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
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
	
	public String verifyPasswordForAccount() {
		log.fine("ADSS: Verifying tokenPassword for Account [" + getTenantIdfr() + "/" + getAppIdfr() + "/" + getUserIdfr() + "].");
		
		// Clear previous results
		result.setRendered(false);
		result.setValue(null);

		// Check that account and password have a value (not null). In theory; not possible, since html validates not empty field.
		if (getTenantIdfr() == null || getAppIdfr() == null || getUserIdfr() == null || getAccountPassword() == null) return null;
		
		// Prepare invocation to ADSS Web Service
		ADSSClientManager adssCM = new ADSSClientManager();
		adssCM.setPath("/account/{tenantIdfr}/{appIdfr}/{userIdfr}/verifyPassword");
		adssCM.resolveTemplate("tenantIdfr", getTenantIdfr());
		adssCM.resolveTemplate("appIdfr", getAppIdfr());
		adssCM.resolveTemplate("userIdfr", getUserIdfr());
		adssCM.queryParam("password", getAccountPassword());
		Invocation.Builder adssInvocationBuilder = adssCM.getTarget().request()
                                                                     .accept("application/xml");
		Invocation adssVerifyPassword = adssInvocationBuilder.buildPost(null);   // POST Request (no contents)
		
		// Invoke ADSS Web Service
		try {
			VerificationResponse verification = adssVerifyPassword.invoke(VerificationResponse.class);
			
			result.setValue(verification.getResult() + "[" + verification.getCode() + "]: " + verification.getReason());
			result.setRendered(true);
			
			FacesMessage message = new FacesMessage("DOUBLECheck verification result: " + verification);
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			FacesContext.getCurrentInstance().addMessage(null, message);

			log.fine("ADSS: Verification response: [" + verification + "]");
			
		} catch (NotFoundException e) {
			log.fine("ADSS: Account not found: [" + getTenantIdfr() + "/" + getAppIdfr() + "/" + getUserIdfr() + "]");
			FacesMessage message = new FacesMessage("ADSS: Account not found: [" + getTenantIdfr() + "/" + getAppIdfr() + "/" + getUserIdfr() + "]");
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			FacesContext.getCurrentInstance().addMessage(null, message);
			
		} catch (Exception e) {
			log.warning("ADSS: Exception while verifying tokenPassword: [" + e.getMessage() + "]");
			FacesMessage message = new FacesMessage("ADSS: Exception while verifying tokenPassword: [" + e.getMessage() + "]");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

		adssCM.close();
		return null;
	}
	
}
