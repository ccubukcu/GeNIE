package com.genie.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.security.core.Authentication;

import com.genie.model.SystemProperties;
import com.genie.model.User;
import com.genie.security.CustomUser;
import com.genie.services.AuthorizationService;

/**
 * @author ccubukcu
 *
 */
@ManagedBean
@SessionScoped
public class SessionBean implements Serializable{
	
	private static final long serialVersionUID = 5048977443280630601L;
	
	private CustomUser customUser;
	private HashMap<Integer, Boolean> permissionMap = new HashMap<Integer, Boolean>();
	private SystemProperties systemProperties;
	private Boolean showLogViewer;
	
	private User currentUser;
	private Authentication userAuth;
	
	public SessionBean() {
	}
	
	@PostConstruct
	public void init(){
	}
	
	public String getCurrentUsername() {
		return currentUser != null ? currentUser.getUsername() : "";
	}
	
	public boolean isLoggedIn() {
		return AuthorizationService.getCurrentUser() != null;
	}
	
	public void invalidateSession() {
		AuthorizationService.invalidateSession();
	}
	
	public List<String> getAuthorities() {
		return AuthorizationService.fetchAuthorities();
	}
    
	public CustomUser getCustomUser() {
		return customUser;
	}

	public void setCustomUser(CustomUser customUser) {
		this.customUser = customUser;
	}

	public HashMap<Integer, Boolean> getPermissionMap() {
		return permissionMap;
	}

	public void setPermissionMap(HashMap<Integer, Boolean> permissionMap) {
		this.permissionMap = permissionMap;
	}
	
	public Boolean getShowLogViewer() {
		return showLogViewer;
	}

	public void setShowLogViewer(Boolean showLogViewer) {
		this.showLogViewer = showLogViewer;
	}

	public void toggleShowLogViewer() {
		showLogViewer = !showLogViewer;
	}

	public SystemProperties getSystemProperties() {
		return systemProperties;
	}

	public void setSystemProperties(SystemProperties systemProperties) {
		this.systemProperties = systemProperties;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public Authentication getUserAuth() {
		return userAuth;
	}

	public void setUserAuth(Authentication userAuth) {
		this.userAuth = userAuth;
	}
	
	
	
}




