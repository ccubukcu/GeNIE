package com.genie.dao;


import com.genie.model.AbstractBaseEntity;
import com.genie.model.SystemProperties;

/**
 * @author ccubukcu
 *
 */
public interface SystemPropertiesDAO {
	public SystemProperties getById(Integer id);
	public boolean save(AbstractBaseEntity entity);
}
