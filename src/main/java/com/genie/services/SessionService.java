package com.genie.services;

import com.genie.beans.SessionBean;
import com.genie.model.User;
import com.genie.utils.DaoUtil;

/**
 * @author ccubukcu
 *
 */
public class SessionService {
	public static void refreshCurrentUser() {
		User newCurrUser = DaoUtil.getUserDAO().getByUsernameWithAuthority(getUsername());
		DaoUtil.getSessionBean().setCurrentUser(newCurrUser);
	}
	
	public static SessionBean getSessionBean(){
		return DaoUtil.getSessionBean();	
	}
	
	public static String getUsername() {
		String username = null;
		
		try {
			username = getSessionBean().getCurrentUser().getUsername();
		} catch (Exception e) { }
		
		return username;
	}
	
	public static void setCurrentUser(User u) {
		getSessionBean().setCurrentUser(u);
	}
	
	public static User getCurrentUser() {
		return getSessionBean().getCurrentUser();
	}
}
