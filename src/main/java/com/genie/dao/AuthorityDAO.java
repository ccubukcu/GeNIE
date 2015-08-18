package com.genie.dao;

import java.util.List;

import com.genie.model.AbstractBaseEntity;
import com.genie.model.Authority;

/**
 * @author ccubukcu
 *
 */
public interface AuthorityDAO {
	public List<Authority> getAll();
	public Authority getById(long id);

	public boolean save(AbstractBaseEntity entity);
	public boolean update(AbstractBaseEntity entity);
	public boolean delete(Authority u);
	public List<Authority> getAuthoritiesForUserWithCourseAndSemester(String username);
	public void deleteBySemesterCourseAndStudent(Long courseId, Long semesterId, String username);
}
