package com.genie.beans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.genie.model.Badge;
import com.genie.model.User;
import com.genie.services.GamificationService;
import com.genie.services.UserService;

@ManagedBean
@ViewScoped
public class ProfileBean extends BaseBean {
	
	private static final long serialVersionUID = 4683882664753680026L;

	private static String urlUsername;
	
	private User user;
	
	private List<Badge> userBadges;
	
	@PostConstruct
	public void initBean() {
		
		if(urlUsername != null) {
			user = UserService.getByUsernameWithEverything(urlUsername);
			
			if(user != null) {
				userBadges = GamificationService.getAllBadgesForStudent(urlUsername);
			}
		}
	}
	
	public static void loadUrlParameters(String username) {
		urlUsername = username;
	}
	
	public boolean isBadgesRendered() {
		return userBadges != null && userBadges.size() > 0;
	}

	public int getRowCount() {
		return (userBadges != null && userBadges.size() > 0) ? 0 : userBadges.size();
	}
	public static String getUrlUsername() {
		return urlUsername;
	}

	public static void setUrlUsername(String urlUsername) {
		ProfileBean.urlUsername = urlUsername;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Badge> getUserBadges() {
		return userBadges;
	}

	public void setUserBadges(List<Badge> userBadges) {
		this.userBadges = userBadges;
	}
}
