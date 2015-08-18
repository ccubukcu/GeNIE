package com.genie.dao.impl;

import com.genie.dao.SystemPropertiesDAO;
import com.genie.model.AbstractBaseEntity;
import com.genie.model.SystemProperties;

/**
 * @author ccubukcu
 * 
 */
public class SystemPropertiesDAOImpl extends BaseDAOImpl implements SystemPropertiesDAO {

	private static final long serialVersionUID = 3947571152047456971L;
	
	@Override
	public SystemProperties getById(Integer id) {
		return  entityManager.find(SystemProperties.class, id);
	}
	
	@Override
	public boolean save(AbstractBaseEntity entity) {
		return super.save(entity);
	}
	
}
