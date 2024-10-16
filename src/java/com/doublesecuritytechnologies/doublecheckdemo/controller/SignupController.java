package com.doublesecuritytechnologies.doublecheckdemo.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.doublesecuritytechnologies.doublecheck.model.DCToken;
import com.doublesecuritytechnologies.doublecheckdemo.ejb.UserManager;
import com.doublesecuritytechnologies.doublecheckdemo.model.Role;
import com.doublesecuritytechnologies.doublecheckdemo.model.Role.RoleType;
import com.doublesecuritytechnologies.doublecheckdemo.model.User;
import com.doublesecuritytechnologies.doublecheckdemo.model.User.UserLifeCycleStatus;
import com.doublesecuritytechnologies.doublecheckdemo.worker.DCCreateUser;
import com.doublesecuritytechnologies.doublecheckdemo.worker.DCProvisionToken;

@Named
@ViewScoped
public class SignupController implements Serializable {
	
	private String userIdfr;
	private String userName;
	private String userLastname;
	private String newPassword;
	private String verifyPassword;
	private String tokenIdfr;
	private SignupLifeCycleStatus status = SignupLifeCycleStatus.CREATE_USER;
	
	
	@EJB(beanName="ejb/DCDemo/UserManager")
	private UserManager userManager;
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("com.doublesecuritytechnologies.doublecheckdemo.controller");
	
	public enum SignupLifeCycleStatus {
		CREATE_USER,
		PROVISION_TOKEN
	}
	
	public SignupController() {
		super();
		log.finest("DCDemo: Signup Controller instantiated.");
	}
	
	
	public String getUserIdfr() {
		return userIdfr;
	}
	public void setUserIdfr(String username) {
		this.userIdfr = username;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserLastname() {
		return userLastname;
	}
	public void setUserLastname(String userLastname) {
		this.userLastname = userLastname;
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
	
	public String getTokenIdfr() {
		return tokenIdfr;
	}
	public void setTokenIdfr(String tokenIdfr) {
		this.tokenIdfr = tokenIdfr;
	}
	
	public SignupLifeCycleStatus getStatus() {
		return status;
	}
	public void setStatus(SignupLifeCycleStatus status) {
		this.status = status;
	}
	public boolean isCreateUserStatus() {
		return status == SignupLifeCycleStatus.CREATE_USER;
	}
	public boolean isProvisionTokenStatus() {
		return status == SignupLifeCycleStatus.PROVISION_TOKEN;
	}
	
	
	public String signup() {
		log.fine("DCDemo: Signup [" + getUserIdfr() + "]");
		
		if (userManager.getUser(getUserIdfr()) == null) {
			
			// Create User in database
			User newUser = new User();
			newUser.setIdfr(getUserIdfr());
			newUser.setName(getUserName());
			newUser.setLasname(getUserLastname());
			if (getNewPassword().equals(getVerifyPassword())) {
				newUser.setPassword(getNewPassword());
			} else {
				// TODO Report password mismatch
				return null;
			}
			Role newRole = new Role();
			newRole.setIdfr(getUserIdfr());
			newRole.setUser(newUser);
			newRole.setRole(RoleType.USER);
			newUser.addRole(newRole);
			// TODO Collect and record all the rest of the User information (name, lastname, email, phoneNumber...)
			newUser.setStatus(UserLifeCycleStatus.CREATED);
			newUser.setTimestamp(new Date());
			
			userManager.createUser(newUser);
			
			// TODO Create User in DOUBLECheck
			DCCreateUser dcCreateUser = new DCCreateUser();
			if (dcCreateUser.createDoubleCheckUser(getUserIdfr()) != null) {
				// TODO Report User created successfully and provision token for user
				DCProvisionToken dcProvisionToken = new DCProvisionToken();
				DCToken provisionedToken = dcProvisionToken.provisionDoubleCheckToken(getUserIdfr()); 
				if (provisionedToken != null) {
					setTokenIdfr(provisionedToken.getIdfr());
					status = SignupLifeCycleStatus.PROVISION_TOKEN;
					log.info("DCDemo: New User created [" + getUserIdfr() + "]");
				}
			}
			
		} else {
			// TODO Report already existing UserId
		}
		
		return null;
	}
	
}
