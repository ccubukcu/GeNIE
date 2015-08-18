package com.genie.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.genie.dao.UserDAO;
import com.genie.model.Authority;

/**
 * @author ccubukcu
 */
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserDAO userDAO;
	
	private CustomUser customUser;
	
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		com.genie.model.User user = userDAO.getByUsernameWithAuthority(username);
		
		if(user == null)
			throw new UsernameNotFoundException("A user with the specified username cannot be found.");
		
		List<GrantedAuthority> authorities = buildUserAuthority(user.getAuthorities());
		 
		return buildUserForAuthentication(user, authorities);
	}
	
	private User buildUserForAuthentication(com.genie.model.User user, 
		List<GrantedAuthority> authorities) {
		return new User(user.getUsername(), user.getPassword(), user.getActive(), 
				true, true, true, authorities);
	}
 
	private List<GrantedAuthority> buildUserAuthority(List<Authority> userRoles) {
 
		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
 
		for (Authority userRole : userRoles) {
			setAuths.add(new SimpleGrantedAuthority(userRole.getAuthority()));
		}
 
		List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);
 
		return Result;
	}

	public CustomUser getCustomUser() {
		return customUser;
	}

	public void setCustomUser(CustomUser customUser) {
		this.customUser = customUser;
	}

}