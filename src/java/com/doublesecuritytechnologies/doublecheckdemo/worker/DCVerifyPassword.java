package com.doublesecuritytechnologies.doublecheckdemo.worker;

import java.util.logging.Logger;

import javax.ws.rs.client.Invocation;

import com.doublesecuritytechnologies.doublecheck.model.DCVerificationResponse;

public class DCVerifyPassword {
	
	private String path = "/account/{tenantIdfr}/{appIdfr}/{userIdfr}/verifyPassword";
	private String tenant = "DEMO";
	private String app = "BANK";
	
	private DCClientManager dccm;
	
	private static Logger log = Logger.getLogger("com.doublesecuritytechnologies.doublecheckdemo.worker");
	
	
	public DCVerifyPassword() {

		// Prepare Client Manager
		dccm = new DCClientManager();
		dccm.setPath(path);
	}
	
	public DCVerifyPassword(String tenant, String app) {
		this();
		this.tenant = tenant;
		this.app = app;
	}

	
	
	public boolean verifyDoubleCheckPassword(String username, String password) {
		log.fine("DC: Verify DOUBLECheck password for: [" + username + "].");

		// Prepare invocation to ADSS Web Service
		dccm.resolveTemplate("tenantIdfr", tenant);
		dccm.resolveTemplate("appIdfr", app);
		dccm.resolveTemplate("userIdfr", username);
		dccm.queryParam("password", password);
		Invocation.Builder adssInvocationBuilder = dccm.getTarget().request()
                                                                     .accept("application/xml");
		Invocation adssVerifyPassword = adssInvocationBuilder.buildPost(null);   // POST Request (no contents)
		
		// Invoke ADSS Web Service
		try {
			DCVerificationResponse verification = adssVerifyPassword.invoke(DCVerificationResponse.class);
			log.fine("DC: Verification response: [" + verification + "]");
			
			if (verification.getCode().equals("000")) {
				return true;
			}
			
		} catch (Exception e) {
			log.fine("DC: Exception while verifying DOUBLECheck Password: [" + e.getMessage() + "]");

		} finally {
			dccm.close();
		}
		
		return false;
	}
	
	public String getTenant() {
		return tenant;
	}
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
	
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}

}
