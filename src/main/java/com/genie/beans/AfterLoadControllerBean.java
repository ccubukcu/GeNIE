package com.genie.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.genie.dao.AssignmentDAO;
import com.genie.dao.AuthorityDAO;
import com.genie.dao.CourseDAO;
import com.genie.dao.CoursePlanDAO;
import com.genie.dao.SchoolYearDAO;
import com.genie.dao.SemesterCourseDAO;
import com.genie.dao.SemesterDAO;
import com.genie.dao.UserDAO;
import com.genie.enums.GradingCriteria;
import com.genie.model.Assignment;
import com.genie.model.Authority;
import com.genie.model.Course;
import com.genie.model.CoursePlan;
import com.genie.model.GradeCriteria;
import com.genie.model.SchoolYear;
import com.genie.model.Semester;
import com.genie.model.SemesterCourse;
import com.genie.model.User;
import com.genie.security.Role;
import com.genie.utils.DataFormatter;

/**
 * @author ccubukcu
 *
 */
public class AfterLoadControllerBean extends SpringBeanAutowiringSupport implements ApplicationListener<ContextRefreshedEvent>{

	@Autowired
	private UserDAO userDAO;
	@Autowired
	private SchoolYearDAO yearDAO;
	@Autowired
	private SemesterDAO semesterDAO;
	@Autowired
	private CourseDAO courseDAO;
	@Autowired
	private SemesterCourseDAO semesterCourseDAO;
	@Autowired
	private AuthorityDAO authorityDAO;
	@Autowired
	private CoursePlanDAO coursePlanDAO;
	@Autowired
	private AssignmentDAO assignmentDAO;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		checkAdminUser();
		
		User ins = userDAO.getByUsername("instructor");
		
		if(ins == null) {
			createTestUsers();
		}
	}
	
	public void checkAdminUser() {
		User admin = userDAO.getByUsername("admin");
		
		if(admin == null) {
			admin = createUser("admin", "admin", Role.ROLE_ADMINISTRATOR.toString());
			userDAO.saveUserWithAuthority(admin);
		}
	}
	
	public void createTestUsers() {
		//
		// Create instructors and demonstrators
		//
		User i = createUser("i", "i", Role.ROLE_INSTRUCTOR.toString());
		userDAO.saveUserWithAuthority(i);
		
		User instructor = createUser("instructor", "instructor", Role.ROLE_INSTRUCTOR.toString());
		userDAO.saveUserWithAuthority(instructor);
		
		User d = createUser("d", "d", Role.ROLE_DEMONSTRATOR.toString());
		userDAO.saveUserWithAuthority(d);
		
		User demo = createUser("demonstrator", "demonstrator", Role.ROLE_DEMONSTRATOR.toString());
		userDAO.saveUserWithAuthority(demo);
		
		//
		// Create School Year
		//
		SchoolYear yr = new SchoolYear();
		yr.setYearIdentifier("Regular Year 1");
		yr.setStartDate(stringToDate("10/09/2015"));
		yr.setEndDate(stringToDate("21/08/2016"));
		yr.setNotes("Regular Year Notes for testing purposes.");
		yearDAO.save(yr);
		
		//
		// Create 2 semesters
		//
		Semester sm1 = new Semester();
		sm1.setSchoolYearId(yr.getId());
		sm1.setSemesterIdentifier("Regular Semester 1");
		sm1.setSemesterOrder(1);
		sm1.setStartDate(stringToDate("10/09/2015"));
		sm1.setEndDate(stringToDate("21/12/2015"));
		sm1.setNotes("Regular Semester 1 Notes for testing purposes.");
		semesterDAO.save(sm1);
		
		Semester sm2 = new Semester();
		sm2.setSchoolYearId(yr.getId());
		sm2.setSemesterIdentifier("Regular Semester 2");
		sm2.setSemesterOrder(2);
		sm2.setStartDate(stringToDate("21/12/2015"));
		sm2.setEndDate(stringToDate("05/06/2016"));
		sm2.setNotes("Regular Semester 2 Notes for testing purposes.");
		semesterDAO.save(sm2);
		
		//
		// Create 2 courses and related authorities
		//
		Course c1 = new Course();
		c1.setCourseIdentifier("RC101");
		c1.setCourseName("Regular Course Sem 1");
		c1.setInstructor("i");
		c1.setShortDescription("Short Desc RC101 for testing purposes.");
		c1.setLongDescription("Long Description for RC101 - Regular Course for Semester 1 for testing purposes");
		courseDAO.save(c1);
		
		Course c2 = new Course();
		c2.setCourseIdentifier("RC201");
		c2.setCourseName("Regular Course Sem 2");
		c2.setInstructor("i");
		c2.setShortDescription("Short Desc RC202 for testing purposes.");
		c2.setLongDescription("Long Description for RC201 - Regular Course for Semester 2 for testing purposes");
		courseDAO.save(c2);

		Authority ac1 = new Authority();
		ac1.setUsername("i");
		ac1.setAuthority(Role.ROLE_INSTRUCTOR.toString());
		ac1.setCourseId(c1.getId());
		authorityDAO.save(ac1);
		
		Authority ac2 = new Authority();
		ac2.setUsername("i");
		ac2.setAuthority(Role.ROLE_INSTRUCTOR.toString());
		ac2.setCourseId(c2.getId());
		authorityDAO.save(ac2);
		
		//
		// Create 2 semester courses
		//
		SemesterCourse sc1 = new SemesterCourse();
		sc1.setSemesterId(sm1.getId());
		sc1.setCourseId(c1.getId());
		sc1.setEnrollmentKey("sc1enroll");
		semesterCourseDAO.save(sc1);
		
		SemesterCourse sc2 = new SemesterCourse();
		sc2.setSemesterId(sm2.getId());
		sc2.setCourseId(c2.getId());
		sc2.setEnrollmentKey("sc2enroll");
		semesterCourseDAO.save(sc2);

		//
		// Create grading criteria
		//
		createGradeCriteria(sc1.getId());
		createGradeCriteria(sc2.getId());
		
		//
		// Create 15 week course plan
		//
		List<Long> cpIdListSc1 = createCoursePlan(sc1.getId());
		List<Long> cpIdListSc2 = createCoursePlan(sc2.getId());
		
		//
		// Create assignments for each plan
		//
		createAssignments(cpIdListSc1);
		createAssignments(cpIdListSc2);
		
		for (int j = 0; j < 10; j++) {
			Long courseId = sc1.getCourseId();
			Long semesterId = sc1.getSemesterId();
			
			if(j > 6) {
				courseId = sc2.getCourseId();
				semesterId = sc2.getSemesterId();
			}
			
			User student = createUser(Integer.toString(j), Integer.toString(j), Role.ROLE_STUDENT.toString(), courseId, semesterId);
			
			userDAO.saveUserWithAuthority(student);
		}
	}
	
	public void createGradeCriteria(long scid) {
		String suffix = " For Semester Course " + Long.toString(scid);
		GradeCriteria gc1 = new GradeCriteria();
		gc1.setName("Midterm 1" + suffix);
		gc1.setGradingCriteria(GradingCriteria.EXAM.getIndex());
		gc1.setWeight(15);
		gc1.setSemesterCourseId(scid);
		semesterCourseDAO.save(gc1);

		GradeCriteria gc2 = new GradeCriteria();
		gc2.setName("Midterm 2" + suffix);
		gc2.setGradingCriteria(GradingCriteria.EXAM.getIndex());
		gc2.setWeight(15);
		gc2.setSemesterCourseId(scid);
		semesterCourseDAO.save(gc2);

		GradeCriteria gc3 = new GradeCriteria();
		gc3.setName("Final" + suffix);
		gc3.setGradingCriteria(GradingCriteria.EXAM.getIndex());
		gc3.setWeight(30);
		gc3.setSemesterCourseId(scid);
		semesterCourseDAO.save(gc3);

		GradeCriteria gc4 = new GradeCriteria();
		gc4.setName("Attendance" + suffix);
		gc4.setGradingCriteria(GradingCriteria.ATTENDANCE.getIndex());
		gc4.setWeight(10);
		gc4.setSemesterCourseId(scid);
		semesterCourseDAO.save(gc4);

		GradeCriteria gc5 = new GradeCriteria();
		gc5.setName("Assignments" + suffix);
		gc5.setGradingCriteria(GradingCriteria.ASSIGNMENT.getIndex());
		gc5.setWeight(30);
		gc5.setSemesterCourseId(scid);
		semesterCourseDAO.save(gc5);
	}
	
	public List<Long> createCoursePlan(long scid) {
		List<Long> idList = new ArrayList<Long>();
		
		String suffix = " For Semester Course " + Long.toString(scid);
		CoursePlan cp1 = new CoursePlan();
		cp1.setCoursePlanTitle("Regular Plan Section 1" + suffix);
		cp1.setSemesterCourseId(scid);
		cp1.setPlanOrder(1);
		cp1.setPlanLength(3);
		cp1.setDescription("Regular Plan Section 1 Description" + suffix);
		coursePlanDAO.save(cp1);
		idList.add(cp1.getId());
		
		CoursePlan cp2 = new CoursePlan();
		cp2.setCoursePlanTitle("Regular Plan Section 2" + suffix);
		cp2.setSemesterCourseId(scid);
		cp2.setPlanOrder(2);
		cp2.setPlanLength(2);
		cp2.setDescription("Regular Plan Section 2 Description" + suffix);
		coursePlanDAO.save(cp2);
		idList.add(cp2.getId());

		CoursePlan cp3 = new CoursePlan();
		cp3.setCoursePlanTitle("Regular Plan Section 3" + suffix);
		cp3.setSemesterCourseId(scid);
		cp3.setPlanOrder(3);
		cp3.setPlanLength(4);
		cp3.setDescription("Regular Plan Section 3 Description" + suffix);
		coursePlanDAO.save(cp3);
		idList.add(cp3.getId());

		CoursePlan cp4 = new CoursePlan();
		cp4.setCoursePlanTitle("Regular Plan Section 4" + suffix);
		cp4.setSemesterCourseId(scid);
		cp4.setPlanOrder(4);
		cp4.setPlanLength(1);
		cp4.setDescription("Regular Plan Section 4 Description" + suffix);
		coursePlanDAO.save(cp4);
		idList.add(cp4.getId());

		CoursePlan cp5 = new CoursePlan();
		cp5.setCoursePlanTitle("Regular Plan Section 5" + suffix);
		cp5.setSemesterCourseId(scid);
		cp5.setPlanOrder(5);
		cp5.setPlanLength(5);
		cp5.setDescription("Regular Plan Section 5 Description" + suffix);
		coursePlanDAO.save(cp5);
		idList.add(cp5.getId());
		
		return idList;
	}
	
	public void createAssignments(List<Long> idList) {
		DateTime now = DateTime.now();
		for (int i = 0; i<idList.size(); i++) {
			Assignment asgn = new Assignment();
			asgn.setPlanId(idList.get(i));
			String suffix = " " + Integer.toString(i) + " on " + idList.get(i).toString();
			
			asgn.setName("Assignment" + suffix);
			asgn.setDescription("Assignment Description, a very detailed one" + suffix);
			asgn.setDueDate(now.toDate());
			assignmentDAO.save(asgn);
			now = now.plusDays(15);
		}
	}
	
	public Date stringToDate(String date) {
		return DataFormatter.stringToDate(date);
	}
	
	public User createUser(String username, String password, String authority) {
		User newUser = new User();
		newUser.setPassword(DataFormatter.md5String(password));
		newUser.setUsername(username);
		newUser.setFullName(username);
		newUser.setActive(true);
		
		List<Authority> authList = new ArrayList<Authority>();
		Authority auth = new Authority();
		auth.setUsername(newUser.getUsername());
		auth.setAuthority(authority);
		
		authList.add(auth);
		newUser.setAuthorities(authList);
		
		return newUser;
	}
	
	public User createUser(String username, String password, String authority, Long courseId, Long semesterId) {
		User newUser = new User();
		newUser.setPassword(DataFormatter.md5String(password));
		newUser.setUsername(username);
		newUser.setFullName(username);
		newUser.setActive(true);
		
		List<Authority> authList = new ArrayList<Authority>();
		Authority auth = new Authority();
		auth.setUsername(newUser.getUsername());
		auth.setAuthority(authority);
		auth.setCourseId(courseId);
		auth.setSemesterId(semesterId);
		
		authList.add(auth);
		newUser.setAuthorities(authList);
		
		return newUser;
	}
}
