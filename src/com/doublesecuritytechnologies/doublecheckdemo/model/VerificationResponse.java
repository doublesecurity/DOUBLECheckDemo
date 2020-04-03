package com.doublesecuritytechnologies.doublecheckdemo.model;

import java.io.StringWriter;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * Verification Codes:
 * 	000	SUCCESS - Verification OK	
 * 	010 USED PASSWORD - Already used password
 *  011 NEED SYNCHRONIZATION - Synchronization missed by one step (needs synchronization)
 *  012 Device in wrong time-zone (missed by multiples of 1 hr / 3600 secs)
 *  
 *  100 TOKEN ERROR - Generic token problem
 *  101 TOKEN ERROR - Token inactive (needs activation)
 *  102 TOKEN ERROR - Token not found
 *  103 TOKEN ERROR - Token cancelled (marked as terminated)
 *  
 *  200 Account problems
 *  201 Account not active (needs activation)
 *  202 Account not found
 *  203 Account inactive (marked as terminated)
 *  204 Invalid account (malformed, etc)
 *  205 Invalid Tenant
 *  206 Invalid Application
 *  207 Invalid UserId
 *  
 *  500 Fail - Wrong password
 *  
 *  900 SYSTEM ERROR - System/Service problem
 */

@XmlRootElement(name="verificationResponse")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class VerificationResponse {
	
	private String code;
	private String result;
	private String reason;
	
	private static Logger log = Logger.getLogger("com.doublesecuritytechnologies.doublecheckdemo.model");
	

	@XmlElement(name="code")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
	@XmlElement(name="result")
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	
	@XmlElement(name="reason")
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	@Override
	public String toString() {
		String result = null;
		
		try {
			JAXBContext ctx = JAXBContext.newInstance(VerificationResponse.class);
			Marshaller m = ctx.createMarshaller();
			StringWriter sw = new StringWriter();
			m.marshal(this, sw);
			result = sw.toString();
		} catch (Exception e) {
			log.warning("ADSS: No XML/String representation for VerificationResponse [" + this.hashCode() + "].");
		}
		
		return result;
	}

}
