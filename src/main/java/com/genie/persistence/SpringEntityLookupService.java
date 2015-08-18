package com.genie.persistence;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author ccubukcu
 */
@Transactional
public class SpringEntityLookupService implements Serializable { 
	
	private static final long serialVersionUID = -192119027484777133L;
	protected EntityManager entityManager;
	
	@PersistenceContext(unitName = "geniePU")
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Object find(Class<?> arg0, Object arg1) {
		return entityManager.find(arg0, arg1);
	}

	public Object find(Class<?> arg0, Object arg1, String arg2) {
		return entityManager.find(arg0, arg1);
	}

}