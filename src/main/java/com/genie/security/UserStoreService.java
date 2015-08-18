package com.genie.security;

import org.springframework.stereotype.Component;

/**
 * @author ccubukcu
 */
@Component
public interface UserStoreService {

	CustomUser getUserDetails(Object _principal);

	void setUserDetails(Object _principal, CustomUser _user);

	void removeUserDetails(Object _principal);
}