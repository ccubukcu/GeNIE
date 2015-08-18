package com.genie.dao.impl;

import java.util.List;

import org.hibernate.Hibernate;

import com.genie.dao.SchoolYearDAO;
import com.genie.model.SchoolYear;

/**
 * @author ccubukcu
 *
 */
public class SchoolYearDAOImpl extends BaseDAOImpl implements SchoolYearDAO {

	private static final long serialVersionUID = 6523566827739031874L;

	@SuppressWarnings("unchecked")
	@Override
	public List<SchoolYear> getAll() {
		List<SchoolYear> items = null;

		try {
			items = entityManager
					.createQuery("select a from SchoolYear a where a.active = 1").getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SchoolYear> getAllWithSemesters() {
		List<SchoolYear> items = null;

		try {
			items = entityManager
					.createQuery("select a from SchoolYear a where a.active = 1").getResultList();
			
			for (SchoolYear schoolYear : items) {
				Hibernate.initialize(schoolYear.getSemesters());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	@Override
	public SchoolYear getById(long id) {
		return entityManager.find(SchoolYear.class, id);
	}

	@Override
	public boolean delete(SchoolYear item) {
		item = entityManager.getReference(SchoolYear.class, item.getId());
		if (item == null)
			return false;
		item.setActive(false);
		update(item);
		return true;
	}
}