package com.doublesecuritytechnologies.doublecheckdemo.controller;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.ws.rs.client.Invocation;

import com.doublesecuritytechnologies.doublecheck.model.DCToken;
import com.doublesecuritytechnologies.doublecheckdemo.worker.DCClientManager;

@Named("provisionToken")
@ViewScoped
public class ProvisionTokenController implements Serializable {
	
	private String tenantIdfr;
	private String appIdfr;
	private String userIdfr;
	private HtmlOutputText result;
	private String tokenIdfr;


	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("com.doublesecuritytechnologies.doublecheckdemo.controller");
	
	
	public ProvisionTokenController() {
		super();
		log.finest("ADSS: ProvisionTokenController (backing bean) instantiated.");
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
	
	public HtmlOutputText getResult() {
		return result;
	}
	public void setResult(HtmlOutputText result) {
		this.result = result;
	}
	
	public String getTokenIdfr() {
		return tokenIdfr;
	}
	
	public boolean isProvisionedTokenReady() {
		return tokenIdfr != null;
	}


	////////////////////////////////////////
	/////   ACTIONS     ////////////////////
	////////////////////////////////////////
	
	public String provisionToken() {
		log.fine("ADSS: Provisioning token for [" + getTenantIdfr() + "/" + getAppIdfr() + "/" + getUserIdfr() + "].");
		
		// Clear previous results
		result.setRendered(false);
		result.setValue(null);
		tokenIdfr = null;

		// Prepare invocation to ADSS Web Service
		DCClientManager adssCM = new DCClientManager();
		adssCM.setPath("account/{tenantIdfr}/{appIdfr}/{userIdfr}/provisionToken");
		adssCM.resolveTemplate("tenantIdfr", getTenantIdfr());
		adssCM.resolveTemplate("appIdfr", getAppIdfr());
		adssCM.resolveTemplate("userIdfr", getUserIdfr());
		Invocation.Builder adssInvocationBuilder = adssCM.getTarget().request()
                                                                     .accept("application/xml");
		Invocation adssProvisionToken = adssInvocationBuilder.buildPost(null);   // POST Request (no content)
		
		// Invoke ADSS Web Service
		try {
			DCToken provisionedToken = adssProvisionToken.invoke(DCToken.class);

			result.setValue(provisionedToken.getIdfr() + ": " + provisionedToken.getStatus() + " - " + provisionedToken.getType());
			result.setRendered(true);
			tokenIdfr = provisionedToken.getIdfr();
			
			FacesMessage message = new FacesMessage("ADSS: DCToken Provisioned:" + provisionedToken);
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			FacesContext.getCurrentInstance().addMessage(null, message);

			log.fine("ADSS: DCToken provisioned: [" + provisionedToken + "]");
			
		} catch (Exception e) {
			log.warning("ADSS: Exception while provisioning token: [" + e.getMessage() + "]");
			FacesMessage message = new FacesMessage("ADSS: Exception while provisioning token: [" + e.getMessage() + "]");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

		adssCM.close();
		return null;
	}
	
}
