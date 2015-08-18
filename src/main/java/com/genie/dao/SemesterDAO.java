package com.genie.dao;

import java.util.List;

import com.genie.model.AbstractBaseEntity;
import com.genie.model.Semester;

/**
 * @author ccubukcu
 *
 */
public interface SemesterDAO {
	public List<Semester> getAll();
	public List<Semester> getAllWithCourses();
	public Semester getById(long id);

	public boolean save(AbstractBaseEntity entity);
	public boolean update(AbstractBaseEntity entity);
	public boolean delete(Semester u);
	public List<Semester> getAllByYearId(long yearId);
	public Semester getByIdWithYear(Long id);
}
