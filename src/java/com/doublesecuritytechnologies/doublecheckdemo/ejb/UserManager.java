package com.doublesecuritytechnologies.doublecheckdemo.ejb;

import javax.ejb.Local;

import com.doublesecuritytechnologies.doublecheckdemo.model.User;

@Local
public interface UserManager {
	
	// CRUD operations on User
	public User getUser(int id);
	public User getUser(String idfr);
	public User createUser(User user);
	public User updateUser(User user);
	
	// Access Control
	public void changePassword(User user, String currentPassword, String newPassord);
	
}
