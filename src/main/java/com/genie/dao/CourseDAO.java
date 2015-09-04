package com.genie.dao;

import java.util.List;

import com.genie.model.AbstractBaseEntity;
import com.genie.model.Course;
import com.genie.model.SemesterCourse;

/**
 * @author ccubukcu
 *
 */
public interface CourseDAO {
	public List<Course> getAll();
	public Course getById(long id);

	public boolean save(AbstractBaseEntity entity);
	public boolean update(AbstractBaseEntity entity);
	public boolean delete(Course u);
	public List<SemesterCourse> getAllSemesterCoursesForUser(String username);
	public List<Course> getAllCoursesForUser(String username);
}
