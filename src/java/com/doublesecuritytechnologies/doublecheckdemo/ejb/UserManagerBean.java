package com.doublesecuritytechnologies.doublecheckdemo.ejb;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.apache.catalina.CredentialHandler;
import org.jrrevuelta.security.passwords.JrrCredentialHandler;

import com.doublesecuritytechnologies.doublecheckdemo.model.User;

@Stateless(name="ejb/DCDemo/UserManager")
public class UserManagerBean implements UserManager {
	
	@PersistenceContext(name="DoubleCheckDemo") EntityManager em;
	
	private static Logger log = Logger.getLogger("com.doublesecuritytechnologies.doublecheckdemo.ejb");
	
	
	public UserManagerBean() {
		super();
		log.finest("DCDemo: UserManager (ejb) instantiated.");
	}
	
	
	@Override
	public User getUser(int id) {
		log.fine("DCDemo: Getting User with id: [" + id + "]");
		
		return em.find(User.class, id);
	}
	
	@Override
	public User getUser(String idfr) {
		log.fine("DCDemo: Getting User with idfr: [" + idfr + "]");
		
		User user = null;
		try {
			TypedQuery<User> q = em.createNamedQuery("User.byIdfr", User.class);
			q.setParameter("idfr", idfr);
			user = q.getSingleResult();
		} catch (NoResultException e) {
			log.fine("DCDemo: User not found. [" + idfr + "] - " + e.getMessage());
			//throw UserNotFoundException;
		} catch (NonUniqueResultException e) {
			log.warning("DCDemo: Inconsistency y DB. More than one User found with idfr [" + idfr + "] - " + e.getMessage());
			//throw UserException;
		} catch (PersistenceException e) {
			log.warning("DCDemo: Persistence exception in DB. - " + e.getMessage());
		}
		
		return user;
	}
	
	@Override
	public User createUser(User user) {
		log.fine("DCDemo: Creating User [" + user.getIdfr() + "]");
		
		CredentialHandler credentialHandler = new JrrCredentialHandler();
		user.setPassword(credentialHandler.mutate(user.getPassword()));
		
		em.persist(user);
		return user;
	}
	
	@Override
	public User updateUser(User user) {
		log.fine("DCDemo: Updating User [" + user.getIdfr() + "]");
		
		em.merge(user);
		return user;
	}
	
	@Override
	public void changePassword(User user, String currentPassword, String newPassord) {
		log.fine("DCDemo: Changing password for User [" + user.getIdfr() + "]");
		
		CredentialHandler credentialHandler = new JrrCredentialHandler();
		if (credentialHandler.matches(currentPassword, user.getPassword())) {
			user.setPassword(credentialHandler.mutate(newPassord));
			em.merge(user);
		}
	}
	
}
