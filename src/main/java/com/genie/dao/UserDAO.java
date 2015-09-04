package com.genie.dao;

import java.util.List;

import com.genie.model.AbstractBaseEntity;
import com.genie.model.User;

/**
 * @author ccubukcu
 *
 */
public interface UserDAO {
	public User getById(long id);
	public User getByUsername(String username);
	public User getByEmail(String email);
	public User getByUsernameAndPassword(String username, String password);
	public User getByToken(String token);
	
	public boolean save(AbstractBaseEntity entity);
	public boolean update(AbstractBaseEntity entity);
	public boolean delete(User u);
	public boolean saveUserWithAuthority(User user);
	public boolean updateUserWithAuthority(User user);
	public User getByIdWithAuthority(long id);
	public User getByUsernameWithAuthority(String authority);
	public List<User> getAll();
	public List<User> getAllWithAuthorities();
	public List<User> getAllStudentsByCourseForGrading(Long courseId, Long semesterId);
	public User getByUsernameWithEverything(String username);
	public List<User> getAllBySemesterCourseExceptUser(Long courseId, Long semesterId, String username);
}
