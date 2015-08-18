/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genie.persistence;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.joda.time.DateTime;

import com.genie.dao.AuditLogDAO;
import com.genie.model.AuditLog;
import com.genie.model.LoggableEntity;
import com.genie.model.User;
import com.genie.services.SessionService;
import com.genie.utils.DaoUtil;
import com.genie.utils.PortalConstants;

/**
 *
 * @author ccubukcu
 */
public class GenieInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 7580962826196421264L;

	private AuditLogDAO auditLogService;
    public static final Integer OPERATION_TYPE_INSERT = 1;
    public static final Integer OPERATION_TYPE_UPDATE = 2;
    public static final Integer OPERATION_TYPE_DELETE = 3;
    public static final String FIELD_SEPARATOR = "||";

    public AuditLogDAO getAuditLogService() {
        if (auditLogService == null) {
            auditLogService = DaoUtil.getAuditLogDAO();
        }
        return auditLogService;
    }

    public void setAuditLogService(AuditLogDAO auditLogService) {
        this.auditLogService = auditLogService;
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) throws CallbackException {

        if (!(entity instanceof LoggableEntity)) {
            return true;
        }
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < currentState.length; i++) {
            if (!types[i].isEqual(currentState[i], previousState[i])) {
                sbf.append(propertyNames[i] + "=" + (currentState[i] != null ? currentState[i].toString() : "null") + FIELD_SEPARATOR);
            }
        }
        if (sbf.length() > 0) {
            AuditLog log = new AuditLog();
            log.setLogTime(new DateTime().toDate());
            log.setEntityId(new Long(id.toString()));
            log.setEntityName(entity.getClass().getName());
            log.setEntityValues(sbf.toString());
            log.setOperation(OPERATION_TYPE_UPDATE);
            User currentUser = SessionService.getCurrentUser();

            if (currentUser != null && currentUser.getUsername() != null) {
                String loggedInUser = currentUser.getUsername();
                log.setUsername(loggedInUser);
            } else {
                log.setUsername(PortalConstants.DEFAULT_PORTAL_ACTION_USER);
            }
            getAuditLogService().save(log);
        }
        return true;
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        if (!(entity instanceof LoggableEntity)) {
            return true;
        }

        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < state.length; i++) {
            sbf.append(propertyNames[i] + "=" + (state[i] != null ? state[i].toString() : "null") + FIELD_SEPARATOR);
        }
        AuditLog log = new AuditLog();
        log.setLogTime(new DateTime().toDate());
        log.setEntityId(new Long(id.toString()));
        log.setEntityName(entity.getClass().getName());
        log.setEntityValues(sbf.toString());
        log.setOperation(OPERATION_TYPE_INSERT);
        User currentUser = SessionService.getCurrentUser();

        if (currentUser != null && currentUser.getUsername() != null) {
            String loggedInUser = currentUser.getUsername();
            log.setUsername(loggedInUser);
        } else {
            log.setUsername(PortalConstants.DEFAULT_PORTAL_ACTION_USER);
        }

        getAuditLogService().save(log);
        //System.out.println(sbf.toString());
        return true;
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        if (!(entity instanceof LoggableEntity)) {
            return;
        }

        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < state.length; i++) {
            sbf.append(propertyNames[i] + "=" + (state[i] != null ? state[i].toString() : "null") + FIELD_SEPARATOR);
        }
        AuditLog log = new AuditLog();
        log.setLogTime(new DateTime().toDate());
        log.setEntityId(new Long(id.toString()));
        log.setEntityName(entity.getClass().getName());
        log.setEntityValues(sbf.toString());
        log.setOperation(OPERATION_TYPE_DELETE);
        User currentUser = SessionService.getCurrentUser();
        
        if (currentUser != null && currentUser.getUsername() != null) {
            String loggedInUser = currentUser.getUsername();
            log.setUsername(loggedInUser);
        } else {
            log.setUsername(PortalConstants.DEFAULT_PORTAL_ACTION_USER);
        }
        getAuditLogService().save(log);
        //System.out.println("TestInterceptor.onDelete");
    }
}