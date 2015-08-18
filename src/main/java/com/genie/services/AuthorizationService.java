package com.genie.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.genie.security.CustomUser;
import com.genie.security.Permissions;
import com.genie.security.Role;

/**
 * @author ccubukcu
 *
 */
public class AuthorizationService {

	public AuthorizationService() {
	}

	public static void invalidateSession() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);		
		session.invalidate();

		SecurityContextHolder.getContext().setAuthentication(null);
	}
	
	public static User getCurrentUser() {

		User user = null;
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			if(auth == null && SessionService.getCurrentUser() != null) {
		        SecurityContextHolder.getContext().setAuthentication(SessionService.getSessionBean().getUserAuth());
		        auth = SecurityContextHolder.getContext().getAuthentication();
			}
			
			if (auth != null) {
				Object principal = auth.getPrincipal();
				if (principal instanceof User) {
					return ((User) principal);
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;
	}

	@SuppressWarnings("unchecked")
    public static List<String> fetchAuthorities() {

		List<String> authorityList = new ArrayList<String>();
		try {

			Collection<GrantedAuthority> authoritiesList = (Collection<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
			for (GrantedAuthority a : authoritiesList) {
				authorityList.add(a.getAuthority());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return authorityList;
	}
    
	public static HashMap<Integer, Boolean> getPermissionMapForAuthority(CustomUser user) {
		String authority = "";
		
		if(user != null && user.getAuthority() != null) {
			authority = user.getAuthority().getAuthority();
		}
		HashMap<Integer, Boolean> permissionMap = getEmptyPermissionMap();

		if(authority.equals(Role.ROLE_ADMINISTRATOR.toString())) {
			permissionMap.put(Permissions.system_admin, true);
		}
		
		return permissionMap;
	}

	private static HashMap<Integer, Boolean> getEmptyPermissionMap() {
		HashMap<Integer, Boolean> permissionMap = new HashMap<Integer, Boolean>();
		
		for(int i = 0; i<Permissions.PERMISSIONS_LENGTH; i++) {
			permissionMap.put(i, false);
		}
		return permissionMap;
	}
}
