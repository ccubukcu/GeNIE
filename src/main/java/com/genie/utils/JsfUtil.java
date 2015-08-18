package com.genie.utils;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ccubukcu
 */
public class JsfUtil {

    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }
    
    public static ExternalContext getExternalFacesContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }

    public static HttpServletRequest getHttpServletRequest() {
    	return (HttpServletRequest)getFacesContext().getExternalContext().getRequest();
    }
    
    public static HttpServletResponse getHttpServletResponse() {
    	return (HttpServletResponse)getFacesContext().getExternalContext().getResponse();
    }
    
    public static HttpSession getHttpSession() {
    	return (HttpSession) getFacesContext().getExternalContext().getSession(false);
    }
    
    public static String getRequestParameter(String key) {
    	return getHttpServletRequest().getParameter(key);
    }
    
    public static void putObjectToRequestMap(String key, Object obj) {
        getRequestMap().put(key, obj);
    }
    
    public static Object getObjectFromRequestMap(String key) {
        Object o = getRequestMap().get(key);
        return o;
    }
    
    public static Map<String, Object> getRequestMap() {
        return getFacesContext().getExternalContext().getRequestMap();
    }
    
    public static Object getObjectFromSession(String key) {
        Object o = null;
        o = getFacesContext().getExternalContext().getSessionMap().get(key);
        return o;
    }

    public static void putObjectToSession(String key, Object o) {
        getFacesContext().getExternalContext().getSessionMap().put(key, o);
    }

    public static Object getBean(String beanName) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        StringBuffer strBufELExpression = new StringBuffer("#{}");
        strBufELExpression.insert(2, beanName);
        ValueExpression valueExpression = expressionFactory.createValueExpression( //
                elContext, strBufELExpression.toString(), Object.class);

        return valueExpression.getValue(elContext);
    }

    public static Locale getCurrentJSFViewLocale() {

        Locale currentLocale = null;

        FacesContext ctx = FacesContext.getCurrentInstance();

        if (null != ctx) {

            UIViewRoot uiViewRoot = ctx.getViewRoot();

            if (null != uiViewRoot) {

                currentLocale = uiViewRoot.getLocale();
            }
        }

        return currentLocale;
    }
    
   public static void redirect(String page) {
		try {
			JsfUtil.getExternalFacesContext().redirect(page);
		} catch (IOException e) {
			e.printStackTrace();
		}
   }
   
   public static void addErrorMessage(String messageKey, Object...params) {
	   addMessage(null, FacesMessage.SEVERITY_ERROR, messageKey, params);
   }
   
   public static void addWarningMessage(String messageKey, Object...params) {
	   addMessage(null, FacesMessage.SEVERITY_WARN, messageKey, params);
   }
   
   public static void addInfoMessage(String messageKey, Object...params) {
	   addMessage(null, FacesMessage.SEVERITY_INFO, messageKey, params);
   }
   
   public static void addMessage(String componentId, Severity severity, String detailKey, Object... params) {
	   String summaryKey = "common.error";
	   if(severity == FacesMessage.SEVERITY_INFO) {
		   summaryKey = "common.info";
	   } else if (severity == FacesMessage.SEVERITY_WARN) {
		   summaryKey = "common.warning";
	   }
	   
	   String message = ResourceUtil.getMessage(detailKey, params);
	   String header = ResourceUtil.getMessage(summaryKey, params);
	   
	   FacesMessage msg = new FacesMessage(severity, header, message);
	   FacesContext.getCurrentInstance().addMessage(componentId == null ? "growlMessage" : componentId, msg);
   }
}
