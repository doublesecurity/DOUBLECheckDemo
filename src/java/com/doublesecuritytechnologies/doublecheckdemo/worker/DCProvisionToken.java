package com.doublesecuritytechnologies.doublecheckdemo.worker;

import java.util.logging.Logger;

import javax.ws.rs.client.Invocation;

import com.doublesecuritytechnologies.doublecheck.model.DCToken;

public class DCProvisionToken {
	
	private String path = "/account/{tenantIdfr}/{appIdfr}/{userIdfr}/provisionToken";
	private String tenant = "DEMO";
	private String app = "BANK";
	
	private DCClientManager dccm;
	
	private static Logger log = Logger.getLogger("com.doublesecuritytechnologies.doublecheckdemo.worker");
	
	
	public DCProvisionToken() {

		// Prepare Client Manager
		dccm = new DCClientManager();
		dccm.setPath(path);
	}
	
	public DCProvisionToken(String tenant, String app) {
		this();
		this.tenant = tenant;
		this.app = app;
	}

	
	
	public DCToken provisionDoubleCheckToken(String idfr) {
		log.fine("DC: Provision DOUBLECheck token for: [" + idfr + "].");
		
		// Prepare invocation to ADSS Web Service
		dccm.resolveTemplate("tenantIdfr", tenant);
		dccm.resolveTemplate("appIdfr", app);
		dccm.resolveTemplate("userIdfr", idfr);
		Invocation.Builder adssInvocationBuilder = dccm.getTarget().request()
                                                                     .accept("application/xml");
		Invocation adssProvisionToken = adssInvocationBuilder.buildPost(null);   // POST Request
		
		// Invoke ADSS Web Service
		try {
			DCToken provisionedToken = adssProvisionToken.invoke(DCToken.class);
			log.fine("DC: Provision Token response: [" + provisionedToken + "]");
			
			return provisionedToken;
			
		} catch (Exception e) {
			log.fine("DC: Exception while provisioning DOUBLECheck token: [" + e.getMessage() + "]");

		} finally {
			dccm.close();
		}
		
		return null;
	}

}
