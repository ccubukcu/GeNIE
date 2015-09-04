package com.genie.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Hibernate;

import com.genie.dao.SemesterDAO;
import com.genie.model.Semester;
import com.genie.model.SemesterCourse;

/**
 * @author ccubukcu
 *
 */
public class SemesterDAOImpl extends BaseDAOImpl implements SemesterDAO {

	private static final long serialVersionUID = -2625126559308439119L;

	@SuppressWarnings("unchecked")
	@Override
	public List<Semester> getAll() {
		List<Semester> items = null;

		try {
			items = entityManager
					.createQuery("select a from Semester a where a.active = 1").getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Semester> getAllWithCourses() {
		List<Semester> items = null;

		try {
			items = entityManager
					.createQuery("select a from Semester a where a.active = 1 ").getResultList();
			
			for (Semester sem : items) {
				Hibernate.initialize(sem.getSchoolYear());
				Hibernate.initialize(sem.getSemesterCourses());
				int size = sem.getSemesterCourses().size();
				if(size > 0) {
					for (SemesterCourse sc : sem.getSemesterCourses()) {
						Hibernate.initialize(sc.getCourse());
						Hibernate.initialize(sc.getSemester());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}
	
	@Override
	public Semester getByIdWithYear(Long id) {
		Semester sem = getById(id);

		Hibernate.initialize(sem.getSchoolYear());
		Hibernate.initialize(sem.getSemesterCourses());
		
		return sem;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Semester> getAllByYearId(long yearId) {
		List<Semester> items = null;

		try {
			Query q = entityManager
					.createQuery("select a from Semester a where a.active = 1 and a.schoolYearId = :yearId ");
			q.setParameter("yearId", yearId);
			items = q.getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}
	
	@Override
	public Semester getById(long id) {
		return entityManager.find(Semester.class, id);
	}

	@Override
	public boolean delete(Semester item) {
		item = entityManager.getReference(Semester.class, item.getId());
		if (item == null)
			return false;
		item.setActive(false);
		update(item);
		return true;
	}
}