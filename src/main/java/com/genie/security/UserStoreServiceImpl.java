package com.genie.security;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ccubukcu
 */
public class UserStoreServiceImpl implements UserStoreService {

	private Map<Object, CustomUser> userDetailsMap = new HashMap<Object, CustomUser>();

	@Override
	public CustomUser getUserDetails(Object _principal) {
		CustomUser user = null;

		if (null != _principal) {

			user = userDetailsMap.get(_principal);
		}

		return user;
	}

	@Override
	public void setUserDetails(Object _principal, CustomUser _user) {

		if (null != _principal) {

			userDetailsMap.put(_principal, _user);
		}
	}

	@Override
	public void removeUserDetails(Object _principal) {

		if (null != _principal) {

			userDetailsMap.remove(_principal);
		}
	}
}