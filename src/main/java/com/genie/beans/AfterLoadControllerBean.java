package com.genie.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

		List<User> instructors = new ArrayList<User>();
		for(int insNr = 1; insNr<26; insNr++) {
			User instr = createUser("instructor" + insNr, "instructor" + insNr, Role.ROLE_INSTRUCTOR.toString());
			userDAO.saveUserWithAuthority(instr);
			instructors.add(instr);
		}
		
		List<User> students = new ArrayList<User>();
		for(int stNr = 1; stNr<26; stNr++) {
			User stu = createUser("student" + stNr, "student" + stNr, Role.ROLE_STUDENT.toString());
			userDAO.saveUserWithAuthority(stu);
			students.add(stu);
		}
		
		//
		// Create School Year
		//
		SchoolYear yr1 = new SchoolYear();
		yr1.setYearIdentifier("Year of 2014/2015");
		yr1.setStartDate(stringToDate("18/09/2014"));
		yr1.setEndDate(stringToDate("21/08/2015"));
		yr1.setNotes("This is the year of 2014/2015 which starts on 18/09/2014 and ends on 21/08/2015");
		yearDAO.save(yr1);
		
		SchoolYear yr = new SchoolYear();
		yr.setYearIdentifier("Year of 2015/2016");
		yr.setStartDate(stringToDate("14/10/2015"));
		yr.setEndDate(stringToDate("24/09/2016"));
		yr1.setNotes("This is the year of 2015/2016 which starts on 14/10/2015 and ends on 24/09/2016");
		yearDAO.save(yr);
		
		//
		// Create 4 semesters
		//
		List<Semester> semesters = new ArrayList<Semester>();
		Semester sm1 = new Semester();
		sm1.setSchoolYearId(yr.getId());
		sm1.setSemesterIdentifier("Semester 1 of 14/15");
		sm1.setSemesterOrder(1);
		sm1.setStartDate(stringToDate("18/09/2014"));
		sm1.setEndDate(stringToDate("21/12/2014"));
		sm1.setNotes("Semester 1 of year 2014/2015 which starts on 18/09/2014 and ends on 21/12/2014");
		semesterDAO.save(sm1);
		semesters.add(sm1);
		
		Semester sm2 = new Semester();
		sm2.setSchoolYearId(yr.getId());
		sm2.setSemesterIdentifier("Semester 2 of 14/15");
		sm2.setSemesterOrder(2);
		sm2.setStartDate(stringToDate("12/01/2015"));
		sm2.setEndDate(stringToDate("05/06/2015"));
		sm2.setNotes("Semester 2 of year 2014/2015 which starts on 12/01/2015 and ends on 05/06/2015");
		semesterDAO.save(sm2);
		semesters.add(sm2);
		
		Semester sm21 = new Semester();
		sm21.setSchoolYearId(yr1.getId());
		sm21.setSemesterIdentifier("Semester 1 of 15/16");
		sm21.setSemesterOrder(1);
		sm21.setStartDate(stringToDate("14/10/2015"));
		sm21.setEndDate(stringToDate("29/01/2016"));
		sm21.setNotes("Semester 1 of year 2015/2016 which starts on 14/10/2015 and ends on 29/01/2016");
		semesterDAO.save(sm21);
		semesters.add(sm21);
		
		Semester sm22 = new Semester();
		sm22.setSchoolYearId(yr1.getId());
		sm22.setSemesterIdentifier("Semester 2 of 15/16");
		sm22.setSemesterOrder(2);
		sm22.setStartDate(stringToDate("19/02/2016"));
		sm22.setEndDate(stringToDate("30/06/2016"));
		sm22.setNotes("Semester 2 of year 2015/2016 which starts on 19/02/2016 and ends on 30/06/2016");
		semesterDAO.save(sm22);
		semesters.add(sm22);
		
		//
		// Create 2 courses and related authorities
		//
		
		List<SemesterCourse> courses = new ArrayList<SemesterCourse>();
		for(int insNr = 0; insNr < instructors.size(); insNr++) {
			User user = instructors.get(insNr);
			int rand = new Random().nextInt(2)+1;
			
			for(int p = 0; p < rand; p++) {
				String courseCode = "C" + Integer.toString(insNr) + "0" + Integer.toString(p+1);
				Course c1 = new Course();
				c1.setCourseIdentifier(courseCode);
				c1.setCourseName("Course by " + user.getUsername());
				c1.setInstructor(user.getUsername());
				c1.setShortDescription("Short Description of the course " + courseCode + ". Enrollment code: enroll");
				c1.setLongDescription("This is the long description for " + courseCode + " - " + c1.getCourseName());
				courseDAO.save(c1);
				
				Authority ac1 = new Authority();
				ac1.setUsername(user.getUsername());
				ac1.setAuthority(Role.ROLE_INSTRUCTOR.toString());
				ac1.setCourseId(c1.getId());
				authorityDAO.save(ac1);
				
				SemesterCourse sc1 = new SemesterCourse();
				Semester sem = semesters.get(new Random().nextInt(semesters.size()));
				sc1.setSemesterId(sem.getId());
				sc1.setSemester(sem);
				sc1.setCourseId(c1.getId());
				sc1.setEnrollmentKey("enroll");
				semesterCourseDAO.save(sc1);
				
				courses.add(sc1);
			}
		}
		
		//
		// Create grading criteria
		//
		for (SemesterCourse sc : courses) {
			createGradeCriteria(sc.getId());
		}
		
		//
		// Create 15 week course plan
		//
		for (SemesterCourse sc : courses) {
			List<Long> cpIdList = createCoursePlan(sc.getId());
			
			createAssignments(cpIdList, sc.getSemester());
			Collections.shuffle(students, new Random(System.nanoTime()));
			
			int studentCount = new Random().nextInt(7)+5;
			List<User> subList = students.subList(0, studentCount);
			for (User stu : subList) {
				Authority ac = new Authority();
				ac.setUsername(stu.getUsername());
				ac.setAuthority(Role.ROLE_STUDENT.toString());
				ac.setCourseId(sc.getCourseId());
				ac.setSemesterId(sc.getSemesterId());
				authorityDAO.save(ac);
			}
		}
	}
	
	public void createGradeCriteria(long scid) {
		int totalWeight = 100;
		GradeCriteria gc1 = new GradeCriteria();
		gc1.setName("Midterm 1");
		gc1.setGradingCriteria(GradingCriteria.EXAM.getIndex());
		gc1.setWeight(new Random().nextInt(10)+5);
		gc1.setSemesterCourseId(scid);
		semesterCourseDAO.save(gc1);
		totalWeight -= gc1.getWeight();
		
		GradeCriteria gc2 = new GradeCriteria();
		gc2.setName("Midterm 2");
		gc2.setGradingCriteria(GradingCriteria.EXAM.getIndex());
		gc2.setWeight(new Random().nextInt(10)+5);
		gc2.setSemesterCourseId(scid);
		semesterCourseDAO.save(gc2);
		totalWeight -= gc2.getWeight();

		GradeCriteria gc3 = new GradeCriteria();
		gc3.setName("Final");
		gc3.setGradingCriteria(GradingCriteria.EXAM.getIndex());
		gc3.setWeight(new Random().nextInt(15)+15);
		gc3.setSemesterCourseId(scid);
		semesterCourseDAO.save(gc3);
		totalWeight -= gc3.getWeight();

		GradeCriteria gc4 = new GradeCriteria();
		gc4.setName("Attendance");
		gc4.setGradingCriteria(GradingCriteria.ATTENDANCE.getIndex());
		gc4.setWeight(new Random().nextInt(totalWeight/3));
		gc4.setSemesterCourseId(scid);
		semesterCourseDAO.save(gc4);
		totalWeight -= gc4.getWeight();
		
		GradeCriteria gc5 = new GradeCriteria();
		gc5.setName("Attendance");
		gc5.setGradingCriteria(GradingCriteria.GAMIFICATION.getIndex());
		gc5.setWeight(new Random().nextInt(totalWeight/3));
		gc5.setSemesterCourseId(scid);
		semesterCourseDAO.save(gc5);
		totalWeight -= gc5.getWeight();
		
		GradeCriteria gc6 = new GradeCriteria();
		gc6.setName("Participation");
		gc6.setGradingCriteria(GradingCriteria.OTHER.getIndex());
		gc6.setWeight(new Random().nextInt(totalWeight/2));
		gc6.setSemesterCourseId(scid);
		semesterCourseDAO.save(gc6);
		totalWeight -= gc6.getWeight();

		GradeCriteria gc7 = new GradeCriteria();
		gc7.setName("Assignments");
		gc7.setGradingCriteria(GradingCriteria.ASSIGNMENT.getIndex());
		gc7.setWeight(totalWeight);
		gc7.setSemesterCourseId(scid);
		semesterCourseDAO.save(gc7);
	}
	
	public List<Long> createCoursePlan(long scid) {
		List<Long> idList = new ArrayList<Long>();
		
		int weekLength = new Random().nextInt(10) + 10;
		int totalLength = 0;
		int i = 1;
		while(totalLength < weekLength) {
			int currentLength = new Random().nextInt(5) + 1;
			
			if(currentLength + totalLength > weekLength) {
				currentLength = weekLength - totalLength;
			}
			
			CoursePlan cp1 = new CoursePlan();
			cp1.setCoursePlanTitle("Course Plan Section " + i);
			cp1.setSemesterCourseId(scid);
			cp1.setPlanOrder(1);
			cp1.setPlanLength(3);
			cp1.setDescription("This is the details of the section " + i + " of the course plan");
			coursePlanDAO.save(cp1);
			idList.add(cp1.getId());
			
			totalLength += currentLength;
			i++;
		}
		
		return idList;
	}
	
	public void createAssignments(List<Long> idList, Semester sem) {
		DateTime start = new DateTime(sem.getStartDate());
		
		int k = 1;
		int asgnCount = new Random().nextInt(10) + 1;
		int individualWeight = 100 / asgnCount;
		
		for (int i = 0; i < asgnCount; i++) {
			Assignment asgn = new Assignment();
			asgn.setPlanId(idList.get(new Random().nextInt(idList.size())));
			asgn.setName("Assignment " + Integer.toString(k));
			asgn.setWeight((long)individualWeight);
			asgn.setDescription("Assignment Description, a very detailed one " + Integer.toString(k));
			asgn.setDueDate(start.toDate());
			assignmentDAO.save(asgn);
			k++;
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
