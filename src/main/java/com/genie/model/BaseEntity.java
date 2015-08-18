package com.genie.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.genie.persistence.PersistenceListener;
import com.genie.utils.DataFormatter;

/**
 * @author ccubukcu
 *
 */
@Audited
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EntityListeners(value = PersistenceListener.class)
public class BaseEntity extends AbstractBaseEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "creation_user", length = 50)
	private String creationUser;
	@Column(name = "creation_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Column(name = "last_update_user", length = 50)
	private String lastUpdateUser;
	@Column(name = "last_update_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdateTime;
	
	@Column(name = "active")
	private Boolean active;
	
	public String getCreationUser() {
		return creationUser;
	}

	public void setCreationUser(String creationUser) {
		this.creationUser = creationUser;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getLastUpdateTimeString() {
		if (lastUpdateTime == null)
			return "-";
		return DataFormatter.formatDate(lastUpdateTime);
	}

	public String getCreationTimeString() {
		if (creationTime == null)
			return "";
		else
			return DataFormatter.formatDate(creationTime);
	}
	
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public Boolean getActive() {
		return active;
	}
}
