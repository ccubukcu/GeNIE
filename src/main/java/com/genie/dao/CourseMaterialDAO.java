package com.genie.dao;

import java.util.List;

import com.genie.model.AbstractBaseEntity;
import com.genie.model.CourseMaterial;

/**
 * @author ccubukcu
 *
 */
public interface CourseMaterialDAO {
	public List<CourseMaterial> getAll();
	public CourseMaterial getById(long id);

	public boolean save(AbstractBaseEntity entity);
	public boolean update(AbstractBaseEntity entity);
	public boolean delete(CourseMaterial u);
	
	public List<CourseMaterial> getAllByPlanIdWithDocuments(Long id);
}
