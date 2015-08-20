package com.genie.beans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.genie.model.Authority;
import com.genie.model.SchoolYear;
import com.genie.model.Semester;
import com.genie.model.SemesterCourse;
import com.genie.model.StudentGamificationSettings;
import com.genie.model.User;
import com.genie.security.Role;
import com.genie.services.GamificationService;
import com.genie.services.SchoolYearService;
import com.genie.services.SemesterService;
import com.genie.services.SessionService;
import com.genie.services.UserService;
import com.genie.utils.JsfUtil;
import com.genie.utils.ResourceUtil;

/**
 * @author ccubukcu
 * */
@ManagedBean
@ViewScoped
public class CourseListBean extends BaseBean {
	private static final long serialVersionUID = -614747380683470671L;

	private List<SemesterCourse> semCourses;
	private SemesterCourse selectedCourse;
	
	private List<SchoolYear> years;
	private SchoolYear selectedYear;
	
	private List<Semester> semesters;
	private Semester selectedSemester;
	
	private StudentGamificationSettings settings;
	
	private String enrollmentKey;
	
	@PostConstruct
	public void initBean() {
		SessionService.refreshCurrentUser();
		
		refreshYears();
	}
	
	public void refreshYears() {
		years = SchoolYearService.getAllWithSemesters();
		
		if(years != null && years.size() > 0) {
			selectedYear = years.get(0);
			onYearSelect();
		}
	}
	
	public void onYearSelect() {
		if(selectedYear != null) {
			semesters = selectedYear.getSemesters();
			
			if(semesters != null && semesters.size() > 0) {
				selectedSemester = semesters.get(0);
				
				onSemesterSelect();
			}
		}
	}
	
	public void onSemesterSelect() {
		semCourses = SemesterService.getCoursesBySemesterId(selectedSemester.getId());
	}
	
	public boolean isUserEnrolled(SemesterCourse sc) {
		if(SessionService.getCurrentUser() != null) {
			List<Authority> userAuth = SessionService.getCurrentUser().getAuthorities();
			
			if(userAuth != null) {
				for (Authority authority : userAuth) {
					if(authority.getSemesterId() != null && sc.getSemesterId() != null 
							&& authority.getCourseId() != null && sc.getCourseId() != null
							&& authority.getSemesterId().equals(sc.getSemesterId())
							&& authority.getCourseId().equals(sc.getCourseId())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void prepareGamificationSettings(SemesterCourse sc) {
		selectedCourse = sc;
		settings = GamificationService.getUserSettings(selectedCourse.getId());
		
		if(settings == null) {
			settings = new StudentGamificationSettings();
		}
	}
	
	public void addOrUpdateGamificationSettings() {
		try {
			settings.setSemesterCourseId(selectedCourse.getId());
			settings.setStudentName(SessionService.getUsername());
			
			if(settings.getId() != null) {
				GamificationService.updateUserSettings(settings);
				updateSuccessful();
			} else {
				GamificationService.saveUserSettings(settings);
				saveSuccessful();
			}
		} catch (Exception e) {
			e.printStackTrace();
			saveOrUpdateFailed();
		}
	}
	
	public void unenrollFromCourse() {
		if(selectedCourse != null) {
			try {
				UserService.deleteAuthorityBySemesterCourse(selectedCourse);
				SessionService.refreshCurrentUser();
				operationSuccessfulWithMessage("growl.courselist.unenrollSuccessful");
			} catch (Exception e) {
				e.printStackTrace();
				operationFailed();
			}
		}
	}
	
	public void enrollInCourse() {
		if(selectedCourse != null && validateKey()) {
			try {
				Authority enrollment = new Authority();
				enrollment.setAuthority(Role.ROLE_STUDENT.toString());
				enrollment.setSemesterId(selectedCourse.getSemesterId());
				enrollment.setCourseId(selectedCourse.getCourseId());
				
				User currentUser = SessionService.getCurrentUser();
				enrollment.setUsername(currentUser.getUsername());
				currentUser.getAuthorities().add(enrollment);
				
				UserService.saveAuthority(enrollment);
				operationSuccessfulWithMessage("growl.courselist.enrollSuccessful");
			} catch (Exception e) {
				e.printStackTrace();
				operationFailed();
			}
		}
	}
	
	public boolean validateKey() {
		if(!selectedCourse.getEnrollmentKey().equals(enrollmentKey)) {
			return validationFailed("enrollForm:enrollmentKey", "error.courses.invalidEnrollmentKey");
		}
		return true;
	}
	
	public void goToCourse(SemesterCourse sc) {
		JsfUtil.redirect("course-details?scid=" + sc.getId());
	}
	
	public String getUnenrollMessage() {
		String label = "";
		if(selectedCourse != null)
			label = selectedCourse.getCourse().getCourseIdentifier() + " - " + selectedCourse.getCourse().getCourseName();
		
		return ResourceUtil.getLabel("courseList.labels.unenrollNotice", label);
	}
	
	public SemesterCourse getSelectedCourse() {
		return selectedCourse;
	}
	
	public void setSelectedCourse(SemesterCourse sc) {
		selectedCourse = sc;
	}
	
	public List<SemesterCourse> getSemCourses() {
		return semCourses;
	}

	public void setSemCourses(List<SemesterCourse> semCourses) {
		this.semCourses = semCourses;
	}

	public List<SchoolYear> getYears() {
		return years;
	}

	public void setYears(List<SchoolYear> years) {
		this.years = years;
	}

	public SchoolYear getSelectedYear() {
		return selectedYear;
	}

	public void setSelectedYear(SchoolYear selectedYear) {
		this.selectedYear = selectedYear;
	}

	public List<Semester> getSemesters() {
		return semesters;
	}

	public void setSemesters(List<Semester> semesters) {
		this.semesters = semesters;
	}

	public Semester getSelectedSemester() {
		return selectedSemester;
	}

	public void setSelectedSemester(Semester selectedSemester) {
		this.selectedSemester = selectedSemester;
	}

	public String getEnrollmentKey() {
		return enrollmentKey;
	}

	public void setEnrollmentKey(String enrollmentKey) {
		this.enrollmentKey = enrollmentKey;
	}

	public StudentGamificationSettings getSettings() {
		return settings;
	}

	public void setSettings(StudentGamificationSettings settings) {
		this.settings = settings;
	}
}
