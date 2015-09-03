package com.genie.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.joda.time.DateTime;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.genie.enums.UserGender;
import com.genie.model.Authority;
import com.genie.model.User;
import com.genie.security.Role;
import com.genie.services.UserService;
import com.genie.utils.DataFormatter;
import com.genie.utils.JsfUtil;

/**
 * @author ccubukcu
 *
 */
@ManagedBean
@ViewScoped
public class UserBean extends BaseBean {
	
	private static final long serialVersionUID = -8738108462590274832L;
	
	// It might seem like there are multiple variables but it is intentional
	// to prevent mix ups after the bean is initialized.
	
	// Login variables
	private String username;
	private String password;
	
	// Register variables
	private User newUser;
	private String confirmEmail;
	private String confirmPassword;
	private int userRole;
	
	// Pass recovery variables
	private static String urlPassToken;
	private User recoveryUser;
	private String newPassword;
	private String confirmNewPassword;
	private int emailSent;

	private List<SelectItem> roleSelectItems;
	private String selectedRole;
	
	@PostConstruct
	public void initBean() {
		newUser = new User();
		emailSent = -1;
		
		roleSelectItems = Role.getAsRegisterSelectItems();
	}
	
	public static void loadUrlParameters(String passToken) {
		urlPassToken = passToken;
	}

	@SuppressWarnings("unchecked")
	public void userSignIn() {
	    try {
			String md5Password = DataFormatter.md5String(password);
			boolean isLoggedIn = UserService.checkAuthentication(username, md5Password);
			
			if(isLoggedIn) {
				UserService.authenticateUser(username, md5Password);
				Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)    SecurityContextHolder.getContext().getAuthentication().getAuthorities();
				
				for (SimpleGrantedAuthority simpleGrantedAuthority : authorities) {
					System.out.println(simpleGrantedAuthority.getAuthority());
				}
				
		    	JsfUtil.redirect("main");
			} else {
		    	JsfUtil.addMessage("registerForm:email", FacesMessage.SEVERITY_ERROR, "error.failed_login");
		    	JsfUtil.addMessage("signinForm:username", FacesMessage.SEVERITY_ERROR, "error.failed_login");
				FacesContext.getCurrentInstance().validationFailed();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	}
	
	public void saveUser() {
		if(validate()) {
			Authority a = new Authority();
			a.setAuthority(selectedRole);
			a.setUsername(newUser.getUsername());
			
			List<Authority> authList = new ArrayList<Authority>();
			authList.add(a);
			
			newUser.setAuthorities(authList);
			
			try {
				newUser.setPassword(DataFormatter.md5String(newUser.getPassword()));
				newUser.setGender(UserGender.UNSPECIFIED.getIndex());
				newUser.setActive(true);
				
				if(UserService.saveUserWithAuthority(newUser)){
					UserService.authenticateUser(newUser.getUsername(), newUser.getPassword());					
			    	JsfUtil.redirect("settings");
				} else {
					operationFailed();
				}
			} catch (Exception e){
				operationFailed();
				e.printStackTrace();
			}
		}
	}
	
	// check if username or email exists
	public boolean validate(){
		boolean validation = true;
		addCustomCallbackParam("userCheckPassed", false);
		if(!newUser.getPassword().equals(confirmPassword)){
			validation = false;
			JsfUtil.addMessage("registerForm:password", FacesMessage.SEVERITY_ERROR, "error.no_match_password");
			JsfUtil.addMessage("registerForm:confirmpassword", FacesMessage.SEVERITY_ERROR, "error.no_match_password");
			FacesContext.getCurrentInstance().validationFailed();
		} else if(!newUser.getEmail().equals(confirmEmail)) {
			validation = false;
			JsfUtil.addMessage("registerForm:email", FacesMessage.SEVERITY_ERROR, "error.no_match_email");
			JsfUtil.addMessage("registerForm:confirmemail", FacesMessage.SEVERITY_ERROR, "error.no_match_email");
			FacesContext.getCurrentInstance().validationFailed();
		} else if(UserService.getUserByEmail(newUser.getEmail()) != null) {
			validation = false;
			JsfUtil.addMessage("registerForm:email", FacesMessage.SEVERITY_ERROR, "error.email_in_use");
			JsfUtil.addMessage("registerForm:confirmemail", FacesMessage.SEVERITY_ERROR, "error.email_in_use");
			FacesContext.getCurrentInstance().validationFailed();
		} else if(UserService.getUserByUsername(newUser.getUsername()) != null) {
			validation = false;
			JsfUtil.addMessage("registerForm:username", FacesMessage.SEVERITY_ERROR, "error.username_in_use");
			addCustomCallbackParam("userCheckFailed", true);
			FacesContext.getCurrentInstance().validationFailed();
		}
		return validation;
	}
	
	public void sendPasswordReminder() {
		try {
			User user = UserService.getUserByUsername(username);
			if(user != null && user.getUsername() != null) {
				UserService.sendPasswordReminder(user);
			} else {
				validationFailed("recoveryForm:username", "error.forgotpass.usernamenotfound");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateUserPassword() {
		if(validateToken(false) && recoveryUser != null) {
			recoveryUser.setPassword(DataFormatter.md5String(newPassword));
			try {
				if(UserService.updateUser(recoveryUser)){
//					recoveryUser.setTokenValidUntil(null);
//					recoveryUser.setPassToken(null);
					UserService.authenticateUser(recoveryUser.getUsername(), recoveryUser.getPassword());					
			    	JsfUtil.redirect("settings");
				} else {
					JsfUtil.addErrorMessage("common.errorOnSave");
				}
			} catch (Exception e){
				JsfUtil.addErrorMessage("common.errorOnSave");
				e.printStackTrace();
			}
		}
	}
	
	public boolean validateToken(boolean isInitialCheck) {
		if(urlPassToken == null || urlPassToken.isEmpty())
			return false;
		
		if(recoveryUser == null) {
			recoveryUser = UserService.getUserFromToken(urlPassToken);
			if(recoveryUser == null) {
				return false;
			}
		}
		
		if(DateTime.now().isAfterNow())
			return false;
		
		if(!isInitialCheck && !newPassword.equals(confirmNewPassword)) {
			JsfUtil.addMessage("recoveryForm:password", FacesMessage.SEVERITY_ERROR, "error.no_match_password");
			JsfUtil.addMessage("recoveryForm:confirmpassword", FacesMessage.SEVERITY_ERROR, "error.no_match_password");
			FacesContext.getCurrentInstance().validationFailed();
			return false;
		}
		
		return true;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User getNewUser() {
		return newUser;
	}

	public void setNewUser(User newUser) {
		this.newUser = newUser;
	}

	public String getConfirmEmail() {
		return confirmEmail;
	}

	public void setConfirmEmail(String confirmEmail) {
		this.confirmEmail = confirmEmail;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public int getEmailSent() {
		return emailSent;
	}

	public void setEmailSent(int emailSent) {
		this.emailSent = emailSent;
	}

	public int getUserRole() {
		return userRole;
	}

	public void setUserRole(int userRole) {
		this.userRole = userRole;
	}

	public static String getUrlPassToken() {
		return urlPassToken;
	}

	public static void setUrlPassToken(String urlPassToken) {
		UserBean.urlPassToken = urlPassToken;
	}

	public User getRecoveryUser() {
		return recoveryUser;
	}

	public void setRecoveryUser(User recoveryUser) {
		this.recoveryUser = recoveryUser;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}

	public List<SelectItem> getRoleSelectItems() {
		return roleSelectItems;
	}

	public void setRoleSelectItems(List<SelectItem> roleSelectItems) {
		this.roleSelectItems = roleSelectItems;
	}

	public String getSelectedRole() {
		return selectedRole;
	}

	public void setSelectedRole(String selectedRole) {
		this.selectedRole = selectedRole;
	}
}
