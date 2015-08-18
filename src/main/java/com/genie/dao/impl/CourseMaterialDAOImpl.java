package com.genie.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Hibernate;

import com.genie.dao.CourseMaterialDAO;
import com.genie.model.CourseMaterial;

/**
 * @author ccubukcu
 *
 */
@SuppressWarnings("unchecked")
public class CourseMaterialDAOImpl extends BaseDAOImpl implements CourseMaterialDAO {

	private static final long serialVersionUID = -7348409278750775337L;

	@Override
	public List<CourseMaterial> getAll() {
		List<CourseMaterial> items = null;

		try {
			items = entityManager
					.createQuery("select a from CourseMaterial a").getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	@Override
	public CourseMaterial getById(long id) {
		return entityManager.find(CourseMaterial.class, id);
	}

	@Override
	public boolean delete(CourseMaterial item) {
		item = entityManager.getReference(CourseMaterial.class, item.getId());
		if (item == null)
			return false;
		item.setActive(false);
		update(item);
		return true;
	}

	@Override
	public List<CourseMaterial> getAllByPlanIdWithDocuments(Long id) {
		List<CourseMaterial> items = null;

		try {
			Query q = entityManager
					.createQuery("select a from CourseMaterial a where a.active = 1 AND a.coursePlanId = :planId");
			q.setParameter("planId", id);
			items = q.getResultList();
			
			for (CourseMaterial cm : items) {
				Hibernate.initialize(cm.getDocument());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

}
