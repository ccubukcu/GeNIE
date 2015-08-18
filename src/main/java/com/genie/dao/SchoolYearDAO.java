package com.genie.dao;

import java.util.List;

import com.genie.model.AbstractBaseEntity;
import com.genie.model.SchoolYear;

/**
 * @author ccubukcu
 *
 */
public interface SchoolYearDAO {
	public List<SchoolYear> getAll();
	public List<SchoolYear> getAllWithSemesters();
	public SchoolYear getById(long id);

	public boolean save(AbstractBaseEntity entity);
	public boolean update(AbstractBaseEntity entity);
	public boolean delete(SchoolYear u);
}
