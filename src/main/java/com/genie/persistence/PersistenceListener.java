package com.genie.persistence;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import com.genie.model.BaseEntity;
import com.genie.services.SessionService;
import com.genie.utils.PortalConstants;

/**
 * @author ccubukcu
 *
 */
public class PersistenceListener {
	
	@PrePersist
	public void prePersistListener(BaseEntity baseEntity) {
		Date current = Calendar.getInstance().getTime();
		
		String username = SessionService.getUsername();
		username = (username != null && !username.isEmpty() ? username : PortalConstants.DEFAULT_PORTAL_ACTION_USER);
		
		baseEntity.setCreationUser(username);
		baseEntity.setCreationTime(current);
		
		baseEntity.setLastUpdateUser(username);
		baseEntity.setLastUpdateTime(current);
		
		baseEntity.setActive(true);
	}

	@PreUpdate
	public void preUpdateListener(BaseEntity baseEntity) {
		Date current = Calendar.getInstance().getTime();

		String username = SessionService.getUsername();
		username = (username != null && !username.isEmpty() ? username : PortalConstants.DEFAULT_PORTAL_ACTION_USER);
		
		baseEntity.setLastUpdateUser(username);
		baseEntity.setLastUpdateTime(current);
	}
	
	@PreRemove
	public void preRemoveListener(BaseEntity baseEntity) {
		Date current = Calendar.getInstance().getTime();

		String username = SessionService.getUsername();
		username = (username != null && !username.isEmpty() ? username : PortalConstants.DEFAULT_PORTAL_ACTION_USER);
		
		baseEntity.setLastUpdateUser(username);
		baseEntity.setLastUpdateTime(current);
	}
}
