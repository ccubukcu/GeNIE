package com.genie.beans;

import java.sql.Blob;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.genie.model.User;
import com.genie.services.SessionService;
import com.genie.services.UserService;
import com.genie.utils.DaoUtil;
import com.genie.utils.DataFormatter;
import com.genie.utils.JsfUtil;
import com.genie.utils.PortalConstants;

/**
 * @author ccubukcu
 *
 */
@ManagedBean
@ViewScoped
public class SettingsBean extends BaseBean {
	
	private static final long serialVersionUID = -4443621575107755737L;

	private User currentUser;
	private String stringDate;
	
	private String currentPassword;
	private String newPassword;
	private String confirmNewPassword;
	
	private String email;
	private String confirmEmail;
	
	@PostConstruct
	public void postInit() {
		currentUser = DaoUtil.getUserDAO().getByUsername(SessionService.getUsername());
		
		SessionService.setCurrentUser(currentUser);
		
		email = currentUser.getEmail();
		stringDate = DataFormatter.dateToString(currentUser.getBirthdate());
	}
	
	public String getEncodedImage() {
		Blob imgBlob = currentUser.getCoverPicture();
		
		return DataFormatter.getBase64EncodedImage(imgBlob);
	}
	
	public void updatePersonalInfo() {
		currentUser.setBirthdate(DataFormatter.stringToDate(stringDate));
		try {
			UserService.updateUser(currentUser);

			JsfUtil.addInfoMessage("growl.succesfullySaved");
		} catch (Exception e) {
			e.printStackTrace();
			JsfUtil.addErrorMessage("growl.errorWhileSaving");
		}
	}
	
	public void handleFileUpload(FileUploadEvent event) {
		try {
			UploadedFile file = event.getFile();
			
			byte[] data = IOUtils.toByteArray(file.getInputstream());
			Blob picture = new SerialBlob(data);
			currentUser.setCoverPicture(picture);
			UserService.updateUser(currentUser);
			
			JsfUtil.addInfoMessage("growl.succesfullySaved");
		} catch (Exception e) {
			e.printStackTrace();
			JsfUtil.addErrorMessage("growl.errorWhileSaving");
		}
	}
	
	public void updateEmail() {
		if(validateEmail()) {
			try {
				currentUser.setEmail(email);
				UserService.updateUser(currentUser);
				
				JsfUtil.addInfoMessage("growl.succesfullySaved");
			} catch (Exception e) {
				e.printStackTrace();
				JsfUtil.addErrorMessage("growl.errorWhileSaving");
			}
		} else {
			JsfUtil.addErrorMessage("growl.errorWhileSaving");
		}
	}
	
	public boolean validateEmail() {
		boolean validation = true;
		addCustomCallbackParam("userCheckPassed", false);
		 if(!email.equals(confirmEmail)) {
			validation = false;
			JsfUtil.addMessage("emailSettingsForm:email", FacesMessage.SEVERITY_ERROR, "error.no_match_email");
			JsfUtil.addMessage("emailSettingsForm:confirmemail", FacesMessage.SEVERITY_ERROR, "error.no_match_email");
			FacesContext.getCurrentInstance().validationFailed();
		}
		return validation;
	}
	
	public void updatePassword() {
		if(validatePassword()) {
			try {
				currentUser.setPassword(DataFormatter.md5String(newPassword));
				UserService.updateUser(currentUser);
				
				JsfUtil.addInfoMessage("growl.succesfullySaved");
			} catch (Exception e) {
				e.printStackTrace();
				JsfUtil.addErrorMessage("growl.errorWhileSaving");
			}
		} else {
			JsfUtil.addErrorMessage("growl.errorWhileSaving");
		}
	}
	
	public boolean validatePassword() {
		boolean validation = true;
		addCustomCallbackParam("userCheckPassed", false);
		
		if(!DataFormatter.md5String(currentPassword).equals(currentUser.getPassword())) {
			validation = false;
			JsfUtil.addMessage("passwordSettingsForm:oldPassword", FacesMessage.SEVERITY_ERROR, "error.wrong_password");
			FacesContext.getCurrentInstance().validationFailed();
		} else if(!newPassword.equals(confirmNewPassword)) {
			validation = false;
			JsfUtil.addMessage("passwordSettingsForm:newPassword", FacesMessage.SEVERITY_ERROR, "error.no_match_password");
			JsfUtil.addMessage("passwordSettingsForm:confirmNewPassword", FacesMessage.SEVERITY_ERROR, "error.no_match_password");
			FacesContext.getCurrentInstance().validationFailed();
		}
		return validation;
	}
	
	public int getFileLimit() {
		return PortalConstants.PROFILE_PICTURE_FILE_LIMIT;
	}
	
	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public String getStringDate() {
		return stringDate;
	}

	public void setStringDate(String stringDate) {
		this.stringDate = stringDate;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getConfirmEmail() {
		return confirmEmail;
	}

	public void setConfirmEmail(String confirmEmail) {
		this.confirmEmail = confirmEmail;
	}
}
