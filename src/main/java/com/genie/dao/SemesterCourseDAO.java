package com.genie.dao;

import java.util.List;

import com.genie.model.AbstractBaseEntity;
import com.genie.model.GradeCriteria;
import com.genie.model.SemesterCourse;

/**
 * @author ccubukcu
 *
 */
public interface SemesterCourseDAO {
	public List<SemesterCourse> getAll();
	public SemesterCourse getById(long id);

	public boolean save(AbstractBaseEntity entity);
	public boolean update(AbstractBaseEntity entity);
	public boolean delete(SemesterCourse u);
	public List<SemesterCourse> getBySemesterId(Long id);
	public List<GradeCriteria> getGradeCriteriaBySemesterCourseId(Long id);
	public boolean delete(GradeCriteria selectedCriteria);
	public SemesterCourse getByIdWithEverything(Long id);
	public List<SemesterCourse> getAllForCourseList();
	public List<SemesterCourse> getAllCoursesForStudent(String username);
}
