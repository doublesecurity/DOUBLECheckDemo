package com.doublesecuritytechnologies.doublecheckdemo.model;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Token: Core class of the Data Model for the Double Security Service.
 * 
 * A Token represents a virtual device capable of generating one-time passwords, based on a 
 * specific algorithm and a key that has been exchanged with the system.
 * 
 * @author José Ramón Revuelta
 *
 */
@XmlRootElement(name="token")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Token implements Serializable {
	
	private int id;
	private String idfr;
	private String numDigits;
	private String status;
	private String type;
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("com.doublesecuritytechnologies.doublecheckdemo.model");

	
	public Token() {
		super();
		log.finest("ADSS: Token (managed model bean) instantiated.");
	}
	
	
	/**
	 * id: Property with the internal identity of this entity.
	 */
	@XmlAttribute
	public int getId() {
		return id;
	}
	protected void setId(int id) {
		this.id = id;
	}
	
	
	/**
	 * idfr: Property with the external identity of this entity. 
	 * 
	 * This would be the appropriate unique 'tokenId' (unique within the Application or Tenant).
	 */
	@XmlElement(name="idfr")
	public String getIdfr() {
		return idfr;
	}
	public void setIdfr(String idfr) {
		this.idfr = idfr;
	}
	
	
	/**
	 * numDigits: Property that reports the size (in digits) of the generated passwords.
	 */
	@XmlElement(name="numDigits")
	public String getNumDigits() {
		return numDigits;
	}
	public void setNumDigits(String numDigits) {
		this.numDigits = numDigits;
	}
		
	
	/**
	 * status: Property that manages the Life-Cycle Status of the Token object.
	 */
	@XmlElement(name="status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
		
	
	/**
	 * type: Property that manages the TYPE of Token object that is represented.
	 */
	@XmlElement(name="type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
		
	
	@Override
	public String toString() {
		String result = null;
		
		try {
			JAXBContext ctx = JAXBContext.newInstance(Token.class);
			Marshaller m = ctx.createMarshaller();
			StringWriter sw = new StringWriter();
			m.marshal(this, sw);
			result = sw.toString();
		} catch (Exception e) {
			log.warning("ADSS: No XML/String representation for Token [" + this.hashCode() + "].");
		}
		
		return result;
	}
	
}
