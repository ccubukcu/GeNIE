package com.genie.services;

import javax.faces.bean.ManagedProperty;

import com.genie.dao.impl.BaseDAOImpl;

/**
 * @author ccubukcu
 *
 */
public class BaseService {
	@ManagedProperty(name = "baseDao", value = "#{baseDao}")
	private transient BaseDAOImpl baseDao;

	public BaseDAOImpl getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDAOImpl baseDao) {
		this.baseDao = baseDao;
	}
}
