package com.genie.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Hibernate;

import com.genie.dao.CourseDAO;
import com.genie.model.Course;
import com.genie.model.SemesterCourse;

/**
 * @author ccubukcu
 *
 */
@SuppressWarnings("unchecked")
public class CourseDAOImpl extends BaseDAOImpl implements CourseDAO {

	private static final long serialVersionUID = -6286335370472896670L;

	@Override
	public List<Course> getAll() {
		List<Course> items = null;

		try {
			items = entityManager
					.createQuery("select a from Course a where a.active = 1").getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	@Override
	public Course getById(long id) {
		return entityManager.find(Course.class, id);
	}

	@Override
	public boolean delete(Course item) {
		item = entityManager.getReference(Course.class, item.getId());
		if (item == null)
			return false;
		item.setActive(false);
		update(item);
		return true;
	}

	@Override
	public List<SemesterCourse> getAllSemesterCoursesForUser(String username) {
		List<SemesterCourse> items = null;

		try {
			Query q = entityManager
					.createQuery("select a from SemesterCourse a where a.active = 1 AND a.course.instructor = :username");
			
			q.setParameter("username", username);
			items = q.getResultList();
			
			for (SemesterCourse sc : items) {
				Hibernate.initialize(sc.getCourse());
				Hibernate.initialize(sc.getSemester());
				Hibernate.initialize(sc.getSemester().getSchoolYear());
				Hibernate.initialize(sc.getGradeCriteria());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	@Override
	public List<Course> getAllCoursesForUser(String username) {
		List<Course> items = null;

		try {
			Query q = entityManager
					.createQuery("select a from Course a where a.active = 1 AND a.instructor = :username");
			
			q.setParameter("username", username);
			items = q.getResultList();
			
			for (Course c : items) {
				Hibernate.initialize(c.getSemesterCourses());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}
}
