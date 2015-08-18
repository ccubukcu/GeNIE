package com.genie.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;

import com.genie.dao.UserDAO;
import com.genie.model.AbstractBaseEntity;
import com.genie.model.Authority;
import com.genie.model.StudentGrade;
import com.genie.model.User;
import com.genie.security.Role;

/**
 * @author ccubukcu
 * 
 */
@SuppressWarnings("unchecked")
public class UserDAOImpl extends BaseDAOImpl implements UserDAO {
	
	private static final long serialVersionUID = 2584454286785478438L;
	
	private static final Logger logger = Logger.getLogger(UserDAOImpl.class);
	
	@Override
	public User getById(long id) {
		return entityManager.find(User.class, id);
	}

	@Override
	public User getByUsername(String username) {
		User u = null;

		try {
			Query q = entityManager.createQuery("select u from User u WHERE u.username = :username AND u.active = 1");
			q.setParameter("username", username);
			u = (User) q.getSingleResult();
			
		} catch (NoResultException nre){
			return null;
		} catch (Exception e) {
			if(logger.isDebugEnabled())
				logger.debug("listing error", e);
			e.printStackTrace();
		}
		return u;
	}
	
	@Override
	public User getByToken(String token) {
		User u = null;

		try {
			Query q = entityManager.createQuery("select u from User u WHERE u.passToken = :token AND u.active = 1");
			q.setParameter("token", token);
			u = (User) q.getSingleResult();
			
		} catch (NoResultException nre){
			return null;
		} catch (Exception e) {
			if(logger.isDebugEnabled())
				logger.debug("listing error", e);
			e.printStackTrace();
		}
		return u;
	}
	
	@Override
	public User getByUsernameAndPassword(String username, String password) {
		User u = null;

		try {
			Query q = entityManager.createQuery("select u from User u WHERE u.username = :username AND u.password = :password AND u.active = 1");
			q.setParameter("username", username);
			q.setParameter("password", password);
			u = (User) q.getSingleResult();
			
		} catch (NoResultException nre){
			return null;
		} catch (Exception e) {
			if(logger.isDebugEnabled())
				logger.debug("listing error", e);
			e.printStackTrace();
		}
		return u;
	}
	
	@Override
	public User getByEmail(String email) {
		User u = null;

		try {
			Query q = entityManager.createQuery("select u from User u WHERE u.email = :email AND u.active = 1");
			q.setParameter("email", email);
			u = (User) q.getSingleResult();
			
		} catch (NoResultException nre){
			return null;
		} catch (Exception e) {
			if(logger.isDebugEnabled())
				logger.debug("listing error", e);
			e.printStackTrace();
		}
		return u;
	}

	@Override
	public boolean delete(User user) {
		user = entityManager.getReference(User.class, user.getUsername());
		if (user == null)
			return false;
		user.setActive(false);
		update(user);
		return true;
	}

	@Override
	public boolean save(AbstractBaseEntity entity) {
		return super.save(entity);
	}

	@Override
	public boolean update(AbstractBaseEntity entity) {
		return super.update(entity);
	}
	
	@Override
	public User getByIdWithAuthority(long id) {
		User u = getById(id);
		if (u != null) {
			Hibernate.initialize(u.getAuthorities());
		}
		return u;
	}
	
	@Override
	public User getByUsernameWithAuthority(String username) {
		User u = getByUsername(username);
		if (u != null) {
			Hibernate.initialize(u.getAuthorities());
		}
		return u;
	}

	@Override
	public boolean saveUserWithAuthority(User user) {
		try {
			save(user);
			if (user.getAuthorities() != null) {
				for (Authority aut : user.getAuthorities()) {
					save(aut);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean updateUserWithAuthority(User user) {
		try {
			update(user);
			if (user.getAuthorities() != null) {
				for (Authority aut : user.getAuthorities()) {
					update(aut);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public List<User> getAll() {
		List<User> users = null;

		try {
			Query q = entityManager.createQuery("select u from User u WHERE u.active = 1");
			users = q.getResultList();
			
		} catch (Exception e) {
			if(logger.isDebugEnabled())
				logger.debug("listing error", e);
			e.printStackTrace();
		}
		return users;
	}

	@Override
	public List<User> getAllWithAuthorities() {
		List<User> users = null;

		try {
			Query q = entityManager.createQuery("select u from User u WHERE u.active = 1");
			users = q.getResultList();
			for (User user : users) {
				Hibernate.initialize(user.getAuthorities());
			}
		} catch (Exception e) {
			if(logger.isDebugEnabled())
				logger.debug("listing error", e);
			e.printStackTrace();
		}
		return users;
	}

	@Override
	public List<User> getAllStudentsByCourseForGrading(Long courseId, Long semesterId) {
		List<User> users = null;

		try {
			Query q = entityManager.createQuery("select a.user from Authority a "
					+ "WHERE a.active = 1 AND a.authority = :authority "
					+ "AND a.courseId = :courseId and a.semesterId = :semesterId");
			
			q.setParameter("authority", Role.ROLE_STUDENT.toString());
			q.setParameter("courseId", courseId);
			q.setParameter("semesterId", semesterId);
			
			users = q.getResultList();
			for (User user : users) {
				Hibernate.initialize(user.getAttendances());
				Hibernate.initialize(user.getGrades());
				
				if(user.getGrades() != null) {
					for (StudentGrade grade : user.getGrades()) {
						Hibernate.initialize(grade.getGradeCriteria());
						Hibernate.initialize(grade.getAssignment());
					}
				}
			}
		} catch (Exception e) {
			if(logger.isDebugEnabled())
				logger.debug("listing error", e);
			e.printStackTrace();
		}
		return users;
	}

	@Override
	public User getByUsernameWithEverything(String username) {
		User user = getByUsername(username);
		if(user != null) {
			Hibernate.initialize(user.getAttendances());
			Hibernate.initialize(user.getGrades());
			
			if(user.getGrades() != null) {
				for (StudentGrade grade : user.getGrades()) {
					Hibernate.initialize(grade.getGradeCriteria());
					Hibernate.initialize(grade.getAssignment());
				}
			}
		}
		return user;
	}

	@Override
	public List<User> getAllBySemesterCourseExceptUser(Long courseId, Long semesterId, String username) {
		List<User> users = null;

		try {
			Query q = entityManager.createQuery("select a.user from Authority a "
					+ "WHERE a.active = 1 AND a.authority = :authority AND a.username != :username "
					+ "AND a.courseId = :courseId and a.semesterId = :semesterId");
			
			q.setParameter("authority", Role.ROLE_STUDENT.toString());
			q.setParameter("courseId", courseId);
			q.setParameter("semesterId", semesterId);
			q.setParameter("username", username);
			
			users = q.getResultList();
		} catch (Exception e) {
			if(logger.isDebugEnabled())
				logger.debug("listing error", e);
			e.printStackTrace();
		}
		return users;
	}
}
