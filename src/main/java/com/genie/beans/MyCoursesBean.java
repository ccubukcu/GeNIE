package com.genie.beans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.genie.model.SemesterCourse;
import com.genie.model.StudentGamificationSettings;
import com.genie.services.CourseService;
import com.genie.services.GamificationService;
import com.genie.services.SessionService;
import com.genie.services.UserService;
import com.genie.utils.JsfUtil;
import com.genie.utils.ResourceUtil;

@ManagedBean
@ViewScoped
public class MyCoursesBean extends BaseBean {
	private static final long serialVersionUID = -1753043022566286545L;

	private List<SemesterCourse> semCourses;
	private SemesterCourse selectedCourse;
	
	private StudentGamificationSettings settings;
	
	@PostConstruct
	public void initBean() {
		SessionService.refreshCurrentUser();
		
		refreshCourses();
	}
	
	public void refreshCourses() {
		semCourses = CourseService.getAllCoursesForStudent();
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

	public List<SemesterCourse> getSemCourses() {
		return semCourses;
	}

	public void setSemCourses(List<SemesterCourse> semCourses) {
		this.semCourses = semCourses;
	}

	public SemesterCourse getSelectedCourse() {
		return selectedCourse;
	}

	public void setSelectedCourse(SemesterCourse selectedCourse) {
		this.selectedCourse = selectedCourse;
	}

	public StudentGamificationSettings getSettings() {
		return settings;
	}

	public void setSettings(StudentGamificationSettings settings) {
		this.settings = settings;
	}
}
