package com.genie.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Hibernate;

import com.genie.dao.SemesterCourseDAO;
import com.genie.model.GradeCriteria;
import com.genie.model.SemesterCourse;
import com.genie.security.Role;

/**
 * @author ccubukcu
 *
 */
@SuppressWarnings("unchecked")
public class SemesterCourseDAOImpl extends BaseDAOImpl implements SemesterCourseDAO {

	private static final long serialVersionUID = 1394581342467000627L;

	@Override
	public List<SemesterCourse> getAll() {
		List<SemesterCourse> items = null;

		try {
			items = entityManager
					.createQuery("select a from SemesterCourse a WHERE a.active = 1").getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	@Override
	public SemesterCourse getById(long id) {
		return entityManager.find(SemesterCourse.class, id);
	}

	@Override
	public boolean delete(SemesterCourse item) {
		item = entityManager.getReference(SemesterCourse.class, item.getId());
		if (item == null)
			return false;
		item.setActive(false);
		update(item);
		return true;
	}
	
	@Override
	public List<SemesterCourse> getBySemesterId(Long id) {
		List<SemesterCourse> items = null;

		try {
			Query q = entityManager
					.createQuery("select a from SemesterCourse a WHERE a.active = 1 AND a.semesterId = :semID");
			q.setParameter("semID", id);
			items = q.getResultList();
			
			for (SemesterCourse sc : items) {
				Hibernate.initialize(sc.getSemester());
				Hibernate.initialize(sc.getCourse());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	@Override
	public List<GradeCriteria> getGradeCriteriaBySemesterCourseId(Long id) {
		List<GradeCriteria> items = null;

		try {
			Query q = entityManager
					.createQuery("select a from GradeCriteria a WHERE a.active = 1 AND a.semesterCourseId = :id");
			q.setParameter("id", id);
			items = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}
	
	@Override
	public boolean delete(GradeCriteria item) {
		item = entityManager.getReference(GradeCriteria.class, item.getId());
		if (item == null)
			return false;
		item.setActive(false);
		update(item);
		return true;
	}

	@Override
	public SemesterCourse getByIdWithEverything(Long id) {
		SemesterCourse sc = getById(id);
		Hibernate.initialize(sc.getSemester());
		Hibernate.initialize(sc.getCourse());
		Hibernate.initialize(sc.getSemester().getSchoolYear());
		return sc;
	}

	@Override
	public List<SemesterCourse> getAllForCourseList() {
		List<SemesterCourse> items = null;

		try {
			Query q = entityManager
					.createQuery("select a from SemesterCourse a WHERE a.active = 1");
			items = q.getResultList();
			
			for (SemesterCourse sc : items) {
				Hibernate.initialize(sc.getSemester());
				Hibernate.initialize(sc.getCourse());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	@Override
	public List<SemesterCourse> getAllCoursesForStudent(String username) {
		List<SemesterCourse> items = null;

		try {
			Query q = entityManager
					.createQuery("select sc from SemesterCourse sc, Authority a WHERE a.active = 1"
							+ " AND sc.active = 1 AND a.semesterId = sc.semesterId AND a.courseId = sc.courseId"
							+ " AND a.username = :username AND a.authority = :authority");
			q.setParameter("username", username);
			q.setParameter("authority", Role.ROLE_STUDENT.toString());
			items = q.getResultList();
			
			for (SemesterCourse sc : items) {
				Hibernate.initialize(sc.getSemester());
				Hibernate.initialize(sc.getSemester().getSchoolYear());
				Hibernate.initialize(sc.getCourse());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}
}