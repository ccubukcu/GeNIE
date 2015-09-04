/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genie.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

import com.genie.enums.AuditLogOperation;
import com.genie.utils.DataFormatter;

/**
 * 
 * @author ccubukcu
 */
@Entity
@Table(name = "AUDIT_LOG")
public class AuditLog extends AbstractBaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@Column(name = "ENTITY_ID")
	private Long entityId;

	@Column(name = "ENTITY_NAME")
	private String entityName;

	@Column(name = "OPERATION")
	private Integer operation;

	@Column(name = "ENTITY_VALUES", length = 4096)
	private String entityValues;

	@Column(name = "LOG_TIME", insertable = false)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date logTime;

	@Column(name = "USERNAME")
	private String username;

	public AuditLog() {
	}

	public AuditLog(Long id, Long entityId, String entityName,
			Integer operation, String entityValues, Date logTime,
			String username) {
		this.id = id;
		this.entityId = entityId;
		this.entityName = entityName;
		this.operation = operation;
		this.entityValues = entityValues;
		this.logTime = logTime;
		this.username = username;
	}

	public String getOperationName() {
		return AuditLogOperation.getLabel(operation);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Integer getOperation() {
		return operation;
	}

	public void setOperation(Integer operation) {
		this.operation = operation;
	}

	public String getEntityValues() {
		return entityValues;
	}

	public void setEntityValues(String entityValues) {
		this.entityValues = entityValues;
	}

	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLogTimeString() {
		return DataFormatter.formatDateTime(logTime);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + (this.id != null ? this.id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AuditLog other = (AuditLog) obj;
		if (this.id != other.id
				&& (this.id == null || !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "AuditLog{" + "id=" + id + ", entityId=" + entityId
				+ ", entityName=" + entityName + ", operation=" + operation
				+ ", entityValues=" + entityValues + ", logTime=" + logTime
				+ ", username=" + username + '}';
	}

}
