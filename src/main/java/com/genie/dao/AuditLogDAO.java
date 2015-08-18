package com.genie.dao;

import java.util.Date;
import java.util.List;

import com.genie.model.AuditLog;

/**
 * @author ccubukcu
 *
 */
public interface AuditLogDAO {

	public boolean save(AuditLog auditLog);

	public List<AuditLog> getByDates(Date startDate, Date endDate);

}
