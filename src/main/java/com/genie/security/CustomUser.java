package com.genie.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.genie.model.Authority;

/**
 * @author ccubukcu
 *
 */
public class CustomUser extends org.springframework.security.core.userdetails.User {
	
	private static final long serialVersionUID = 451477618056959981L;
	
	private String email;	
	private String lastName;	
	private String firstName;	
	private String username;	
	private Authority authority;
	
	public CustomUser(String username, String password, Collection<GrantedAuthority> grantedAuthorities) throws IllegalArgumentException {
		super(username, password, true,true,true,true, grantedAuthorities);
		this.username = username;
	}
	
	public CustomUser(String username, String password, Collection<GrantedAuthority> grantedAuthorities, String email
			, String lastName, String firstName, Authority authority) throws IllegalArgumentException {
		super(username, password, true,true,true,true, grantedAuthorities);
		this.username = username;
		this.email = email;
		this.lastName = lastName;
		this.firstName = firstName;
		this.authority = authority;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public Authority getAuthority() {
		return authority;
	}

	public void setAuthority(Authority authority) {
		this.authority = authority;
	}
}
