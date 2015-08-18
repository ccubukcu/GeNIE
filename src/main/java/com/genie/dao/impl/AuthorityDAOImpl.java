package com.genie.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Hibernate;

import com.genie.dao.AuthorityDAO;
import com.genie.model.Authority;

/**
 * @author ccubukcu
 * 
 */
@SuppressWarnings("unchecked")
public class AuthorityDAOImpl extends BaseDAOImpl implements AuthorityDAO {

	private static final long serialVersionUID = 5907210349143251930L;

	@Override
	public List<Authority> getAll() {
		List<Authority> authorities = null;

		try {
			authorities = entityManager
					.createQuery("select a from Authority a where a.active = 1").getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return authorities;
	}

	@Override
	public Authority getById(long id) {
		return entityManager.find(Authority.class, id);
	}

	@Override
	public boolean delete(Authority item) {
		item = entityManager.getReference(Authority.class, item.getId());
		if (item == null)
			return false;
		item.setActive(false);
		update(item);
		return true;
	}

	@Override
	public List<Authority> getAuthoritiesForUserWithCourseAndSemester(String username) {
		List<Authority> authorities = null;

		try {
			Query q = entityManager
					.createQuery("select a from Authority a where a.active = 1 AND a.username = :username");
			q.setParameter("username", username);
			authorities = q.getResultList();
			
			for (Authority authority : authorities) {
				Hibernate.initialize(authority.getCourse());
				Hibernate.initialize(authority.getSemester());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return authorities;
	}

	@Override
	public void deleteBySemesterCourseAndStudent(Long courseId, Long semesterId, String username) {
		try {
			Query q = entityManager
					.createQuery("delete Authority a where a.semesterId = :semId AND a.courseId = :crsId AND a.username = :username");
			q.setParameter("username", username);
			q.setParameter("semId", semesterId);
			q.setParameter("crsId", courseId);
			
			q.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
