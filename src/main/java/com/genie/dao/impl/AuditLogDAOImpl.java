package com.genie.dao.impl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.genie.dao.AuditLogDAO;
import com.genie.model.AuditLog;

/**
 * @author ccubukcu
 *
 */
@Transactional
public class AuditLogDAOImpl implements AuditLogDAO, Serializable {

	private static final long serialVersionUID = -7078620872354678635L;

	private EntityManager entityManager;

	private static final Logger logger = Logger
			.getLogger(AuditLogDAOImpl.class);

	@PersistenceContext(unitName = "geniePU")
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public boolean save(AuditLog auditLog) {
		auditLog.setLogTime(new Date(System.currentTimeMillis()));
		entityManager.persist(auditLog);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AuditLog> getByDates(Date startDate, Date endDate) {

		List<AuditLog> logs = null;

		Calendar cl = Calendar.getInstance();
		cl.setTime(endDate);
		cl.set(Calendar.HOUR_OF_DAY, 23);
		cl.set(Calendar.MINUTE, 59);
		cl.set(Calendar.SECOND, 59);

		endDate = cl.getTime();
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("getting log list");
			}
			logs = entityManager
					.createQuery(
							"select a from AuditLog a "
									+ " where (a.logTime) >= :startDate and (a.logTime) <= :endDate")
					.setParameter("startDate", startDate)
					.setParameter("endDate", endDate).getResultList();

		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug("listing error", e);
			}
		}
		return logs;
	}

}
