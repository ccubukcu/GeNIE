/**
 * 
 */
package com.genie.dao.impl;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.transaction.annotation.Transactional;

import com.genie.model.AbstractBaseEntity;

/**
 * @author ccubukcu
 *
 */
@Transactional
public class BaseDAOImpl implements Serializable {

	private static final long serialVersionUID = -6910366692335580092L;

	protected EntityManager entityManager;
	
	@PersistenceContext(unitName = "geniePU")
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public boolean save(AbstractBaseEntity entity) {
		entityManager.persist(entity);
		entityManager.flush();
		return true;
	}
	
	public boolean update(AbstractBaseEntity entity) {
		entityManager.merge(entity);
		entityManager.flush();
		return true;
	}
        
    public Object getReference(Object o) {
        Object t = entityManager.getReference(o.getClass(), o);
        return t;
    }
    
    public Object getReference(Object o, Object id) {
        Object t = entityManager.getReference(o.getClass(), id);
        return t;
    }
    
    public boolean delete(AbstractBaseEntity entity) {
        entityManager.remove(entity);
        entityManager.flush();
        return true;
    }
    
    public AuditReader getAuditReader() {
    	Session session = (Session) entityManager.getDelegate();
    	return AuditReaderFactory.get(session);
    }
}

