package com.genie.beans;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import com.genie.utils.JsfUtil;

/**
 * @author ccubukcu
 *
 */
@ManagedBean
@ViewScoped
public class BaseBean implements Serializable {
	
	private static final long serialVersionUID = 9016283704879275237L;

	public boolean addCallbackParam(String key, Object obj) {
		boolean retval = false;
		RequestContext context = null;
		try {
			context = RequestContext.getCurrentInstance();
			if (context == null) {
				retval = false;
			} else {
				context.addCallbackParam(key, obj);
				retval = true;
			}
		} catch (Throwable t) {
			retval = true;
		}

		return retval;
	}

	public boolean addSuccessCallbackParamAsBoolean() {
		return addCallbackParam("success", true);
	}

	public boolean addFailCallbackParamAsBoolean() {
		return addCallbackParam("success", false);
	}

	public boolean addCustomCallbackParam(String key, boolean value) {
		return addCallbackParam(key, value);
	}
	
	public void ExceptionTester() throws IOException {
		
	}
	
	public boolean validationFailed(String componentId, String messageKey) {
		JsfUtil.addMessage(componentId, FacesMessage.SEVERITY_ERROR, messageKey);
		FacesContext.getCurrentInstance().validationFailed();
		addFailCallbackParamAsBoolean();
		return false;
	}
	
	public void saveSuccessful() {
		addSuccessCallbackParamAsBoolean();
		JsfUtil.addInfoMessage("growl.succesfullyUpdated");
	}
	
	public void updateSuccessful() {
		addSuccessCallbackParamAsBoolean();
		JsfUtil.addInfoMessage("growl.succesfullySaved");
	}
	
	public void deleteSucessful() {
		addSuccessCallbackParamAsBoolean();
		JsfUtil.addInfoMessage("growl.succesfullyRemoved");
	}
	
	public void saveOrUpdateFailed() {
		addFailCallbackParamAsBoolean();
		JsfUtil.addErrorMessage("growl.errorWhileSaving");
	}
	
	public void operationSuccessfulWithMessage(String messageKey) {
		addSuccessCallbackParamAsBoolean();
		JsfUtil.addInfoMessage(messageKey);
	}
	
	public void operationFailedWithMessage(String messageKey) {
		addFailCallbackParamAsBoolean();
		JsfUtil.addErrorMessage(messageKey);
	}
	
	public void operationFailed() {
		addFailCallbackParamAsBoolean();
		JsfUtil.addErrorMessage("growl.errorOccurred");
	}
	
	public String getDescriptionForTooltip(String desc) {
		if(desc == null)
			return "";
		if(desc.length() > 80) {
			return desc.substring(0, 77) + "...";
		}
		return desc;
	}
}
