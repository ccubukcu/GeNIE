package com.genie.services;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.genie.model.Authority;
import com.genie.model.SemesterCourse;
import com.genie.model.User;
import com.genie.scheduling.jobs.SingularMailSenderJob;
import com.genie.utils.DaoUtil;
import com.genie.utils.DataFormatter;
import com.genie.utils.JsfUtil;
import com.genie.utils.ResourceUtil;

/**
 * @author ccubukcu
 * 
 */
public class UserService implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public static void authenticateUser(String username, String password) {
		try {
	        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
	        token.setDetails(new WebAuthenticationDetails(JsfUtil.getHttpServletRequest()));
	        Authentication authentication = DaoUtil.getAuthenticationManager().authenticate(token);
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        
	        SessionService.setCurrentUser(getUserByUsername(username));
	        SessionService.getSessionBean().setUserAuth(SecurityContextHolder.getContext().getAuthentication());
	        HttpSession session = JsfUtil.getHttpServletRequest().getSession(true);
	        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
	    } catch (Exception e) {
	        SecurityContextHolder.getContext().setAuthentication(null);
	        e.printStackTrace();
	    }
	}
	
	public static boolean saveUserWithAuthority(User user) {
		
		User userTemp = DaoUtil.getUserDAO().getByUsername(user.getUsername());
		
		if (userTemp == null || userTemp.equals(null) || !userTemp.getUsername().equals(user.getUsername())) {
			user.setActive(true);
			return DaoUtil.getUserDAO().saveUserWithAuthority(user);
		} else { 
			user.setActive(true);
			return DaoUtil.getUserDAO().updateUserWithAuthority(user);
		}
	}
	
	public static boolean updateUser(User u) {
		return DaoUtil.getUserDAO().update(u);
	}
	
	/**
	 * Returns true if username and password is correct.
	 * */
	public static boolean checkAuthentication(User newUser) {
		return DaoUtil.getUserDAO().getByUsernameAndPassword(newUser.getUsername(), newUser.getPassword()) != null;
	}
	
	/**
	 * Returns true if username and password is correct.
	 * */
	public static boolean checkAuthentication(String username, String password) {
		return DaoUtil.getUserDAO().getByUsernameAndPassword(username, password) != null;
	}
	
	public static void sendPasswordReminder(String username) {
		User u = getUserByUsername(username);
		String token = DataFormatter.generateRandomToken();
		
		DateTime dt = DateTime.now();
		dt = dt.plusDays(1);
		
		u.setPassToken(token);
		u.setTokenValidUntil(dt.toDate());
		updateUser(u);
		
		String url = DataFormatter.getUrlForAppRootWithSeparator() + "pass_recovery?token=" + token; 
		
		String subject = ResourceUtil.getLabel("email.passreminder.subject");
		String body = ResourceUtil.getLabel("email.passreminder.body", url);
		
		String uniqueName = "ReminderJobFor" + username;
		SingularMailSenderJob.scheduleJob(uniqueName, subject, body, u.getEmail(), null, null);
	}
	
	public static User getUserFromToken(String token) {
		return DaoUtil.getUserDAO().getByToken(token);
	}
	
	public static boolean updateUserWithAuthority(User user) {
		return DaoUtil.getUserDAO().updateUserWithAuthority(user);
	}

	public static boolean deleteUser(User user) {
		return DaoUtil.getUserDAO().delete(user);
	}
	
	public static User getUserByUsername(String username){
		return DaoUtil.getUserDAO().getByUsername(username);
	}
	
	public static User getUserByEmail(String email){
		return DaoUtil.getUserDAO().getByEmail(email);
	}

	public static List<User> getAllUsers() {
		return DaoUtil.getUserDAO().getAll();
	}
	
	public static boolean updateAuthority(Authority auth) {
		return DaoUtil.getAuthorityDAO().update(auth);
	}
	
	public static boolean saveAuthority(Authority newAuth) {
		return DaoUtil.getAuthorityDAO().save(newAuth);
	}

	public static List<User> getAllUsersWithAuthorities() {
		return DaoUtil.getUserDAO().getAllWithAuthorities();
	}

	public static void deleteAuthority(Authority selectedAuthority) {
		DaoUtil.getAuthorityDAO().delete(selectedAuthority);
	}

	public static List<Authority> getAuthoritiesForUserWithCourseAndSemester(String username) {
		return DaoUtil.getAuthorityDAO().getAuthoritiesForUserWithCourseAndSemester(username);
	}

	public static List<User> getAllStudentsByCourseSemesterForGrading(Long courseId, Long semesterId) {
		return DaoUtil.getUserDAO().getAllStudentsByCourseForGrading(courseId, semesterId);
	}

	public static void deleteAuthorityBySemesterCourse(SemesterCourse selectedCourse) {
		DaoUtil.getAuthorityDAO().deleteBySemesterCourseAndStudent(selectedCourse.getCourseId(), selectedCourse.getSemesterId(), SessionService.getUsername());
	}

	public static User getByUsernameWithEverything(String username) {
		return DaoUtil.getUserDAO().getByUsernameWithEverything(username);
	}

	public static List<User> getAllBySemesterCourseExceptUser(SemesterCourse semesterCourse) {
		return DaoUtil.getUserDAO().getAllBySemesterCourseExceptUser(semesterCourse.getCourseId(), semesterCourse.getSemesterId(), SessionService.getUsername());
	}
}
