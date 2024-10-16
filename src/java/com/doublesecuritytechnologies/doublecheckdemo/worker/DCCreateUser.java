package com.doublesecuritytechnologies.doublecheckdemo.worker;

import java.util.logging.Logger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;

import com.doublesecuritytechnologies.doublecheck.model.DCUser;

public class DCCreateUser {
	
	private String path = "/account/{tenantIdfr}/{appIdfr}/user";
	private String tenant = "DEMO";
	private String app = "BANK";
	
	private DCClientManager dccm;
	
	private static Logger log = Logger.getLogger("com.doublesecuritytechnologies.doublecheckdemo.worker");
	
	
	public DCCreateUser() {

		// Prepare Client Manager
		dccm = new DCClientManager();
		dccm.setPath(path);
	}
	
	public DCCreateUser(String tenant, String app) {
		this();
		this.tenant = tenant;
		this.app = app;
	}

	
	
	public DCUser createDoubleCheckUser(String idfr) {
		log.fine("DC: Creating DOUBLECheck account for: [" + idfr + "].");
		
		// Create DC User object to create
		DCUser user = new DCUser();
		user.setIdfr(idfr);
		
		// Prepare invocation to ADSS Web Service
		dccm.resolveTemplate("tenantIdfr", tenant);
		dccm.resolveTemplate("appIdfr", app);
		Invocation.Builder adssInvocationBuilder = dccm.getTarget().request()
                                                                     .accept("application/xml");
		Invocation adssCreateUser = adssInvocationBuilder.buildPost(Entity.xml(user));   // POST Request
		
		// Invoke ADSS Web Service
		try {
			DCUser createdUser = adssCreateUser.invoke(DCUser.class);
			log.fine("DC: Create User response: [" + createdUser + "]");
			
			return createdUser;
			
		} catch (Exception e) {
			log.fine("DC: Exception while creating DOUBLECheck user: [" + e.getMessage() + "]");

		} finally {
			dccm.close();
		}
		
		return null;
	}

}
