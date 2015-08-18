package com.genie.dao;

import java.util.List;

import com.genie.model.AbstractBaseEntity;
import com.genie.model.Document;

/**
 * @author ccubukcu
 *
 */
public interface DocumentDAO {
	public List<Document> getAll();
	public Document getById(long id);

	public boolean save(AbstractBaseEntity entity);
	public boolean update(AbstractBaseEntity entity);
	public boolean delete(Document u);
}
