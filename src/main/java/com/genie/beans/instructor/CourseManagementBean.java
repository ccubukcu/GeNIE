package com.genie.beans.instructor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import com.genie.beans.BaseBean;
import com.genie.enums.GradingCriteria;
import com.genie.model.Course;
import com.genie.model.GradeCriteria;
import com.genie.model.SchoolYear;
import com.genie.model.Semester;
import com.genie.model.SemesterCourse;
import com.genie.services.CourseService;
import com.genie.services.SchoolYearService;
import com.genie.services.SemesterService;
import com.genie.utils.ResourceUtil;

/**
 * @author ccubukcu
 *
 */
@ManagedBean
@ViewScoped
public class CourseManagementBean extends BaseBean {
	private static final long serialVersionUID = 5117086999722337943L;

	private List<SemesterCourse> semesterCourses;
	private SemesterCourse selectedSemesterCourse;
	private List<Course> courses;
	private Course selectedCourse;
	
	private List<Semester> semesters;
	private Semester selectedSemester;
	private List<SchoolYear> years;
	private SchoolYear selectedYear;
	
	private Course selectedDialogCourse;
	private List<GradeCriteria> gradeCriteria;
	private GradeCriteria selectedCriteria;
	private List<SelectItem> gradingCriteriaItems;
	
	private boolean updatingCourse;
	private boolean updatingSemesterCourse;
	private boolean updatingCriteria;
	
	private boolean criteriaSelected;
	private boolean yearSelected;
	private boolean semesterSelected;
	private boolean dialogCourseSelected;
	private boolean gradingCriteriaSelected;
	
	@PostConstruct
	public void initBean() {
		refreshCourses();
		refreshSemesterCourses();
		refreshYears();
		
		gradingCriteriaItems = GradingCriteria.getAsSelectItems();
		selectedCriteria = new GradeCriteria();
	}
	
	public void refreshCourses() {
		courses = CourseService.getAllCoursesForInstructor();
	}
	
	public void refreshSemesterCourses() {
		semesterCourses = CourseService.getAllSemesterCoursesForInstructor();
	}
	
	public void refreshYears() {
		years = SchoolYearService.getAll();
	}
	
	public void refreshSemesters() {
		if(selectedYear != null) {
			semesters = SemesterService.getByYearId(selectedYear.getId());
		}
	}
	
	public void onYearSelected() {
		yearSelected = true;
		refreshSemesters();
	}
	
	public void onSemesterSelected() {
		semesterSelected = true;
	}
	
	public void onDialogCourseSelected() {
		dialogCourseSelected = true;
	}
	
	public void clearSelectedCourse() {
		selectedCourse = null;
	}
	
	public void clearSelectedSemesterCourse(){
		selectedSemesterCourse = null;
	}
	
	public void prepareAddCourseDialog() {
		selectedCourse = new Course();
		
		updatingCourse = false;
	}
	
	public void prepareUpdateCourseDialog() {
		updatingCourse = true;
	}
	
	public void addOrUpdateCourse() {
		try {
			if(updatingCourse) {
				CourseService.updateCourse(selectedCourse);
				updateSuccessful();
			} else {
				CourseService.saveCourse(selectedCourse);
				saveSuccessful();
			}
		} catch (Exception e) {
			e.printStackTrace();
			saveOrUpdateFailed();
		}
		
		refreshCourses();
		clearSelectedCourse();
		refreshSemesterCourses();
	}
	
	public void deleteCourse() {
		if(validateDelete()) {
			if(selectedCourse != null && selectedCourse.getId() != null) {
				CourseService.deleteCourse(selectedCourse);
				refreshCourses();
				deleteSucessful();
			} else {
				operationFailed();
			}
			
			refreshCourses();
			clearSelectedCourse();
		} else {
			operationFailedWithMessage("growl.unpermitted_operation");
		}
	}
	
	public boolean validateDelete() {
		if(selectedCourse != null && selectedCourse.getSemesterCourses() != null 
				&& selectedCourse.getSemesterCourses() != null && selectedCourse.getSemesterCourses().size() > 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean isCourseButtonsDisabled() {
		if(selectedCourse == null || (selectedCourse != null && selectedCourse.getId() == null)) {
			return true;
		}
		return false;
	}
	
	public boolean isCourseDeleteDisabled() {
		if(isCourseButtonsDisabled()) {
			return true;
		} else if(selectedCourse.getSemesterCourses() != null && selectedCourse.getSemesterCourses().size() > 0) {
			return true;
		}
		return false;
	}
	
	public void prepareAddSemesterCourseDialog() {
		selectedYear = null;
		selectedSemester = null;
		selectedDialogCourse = null;
		
		selectedSemesterCourse = new SemesterCourse();
		gradeCriteria = new ArrayList<GradeCriteria>();
		selectedCriteria = new GradeCriteria();
		
		updatingSemesterCourse = false;
		dialogCourseSelected = false;
		yearSelected = false;
		semesterSelected = false;
	}
	
	public void prepareUpdateSemesterCourseDialog() {
		selectedCriteria = new GradeCriteria();
		
		selectedYear = selectedSemesterCourse.getSemester().getSchoolYear();
		selectedSemester = selectedSemesterCourse.getSemester();
		selectedDialogCourse = selectedSemesterCourse.getCourse();
		
		gradeCriteria = selectedSemesterCourse.getGradeCriteria();
		
		updatingSemesterCourse = true;
		dialogCourseSelected = true;
		yearSelected = true;
		semesterSelected = true;
	}
	
	public void addOrUpdateSemesterCourse() {
		try {
			if(updatingSemesterCourse) {
				CourseService.updateSemesterCourse(selectedSemesterCourse);
				updateSuccessful();
			} else {
				selectedSemesterCourse.setGradeCriteria(gradeCriteria);
				selectedSemesterCourse.setCourseId(selectedDialogCourse.getId());
				selectedSemesterCourse.setSemesterId(selectedSemester.getId());
				
				CourseService.saveSemesterCourse(selectedSemesterCourse);
				saveSuccessful();
			}
			
			refreshSemesterCourses();
		} catch (Exception e) {
			e.printStackTrace();
			saveOrUpdateFailed();
		}
	}
	
	public void deleteSemesterCourse() {
		if(selectedSemesterCourse != null && selectedSemesterCourse.getId() != null) {
			CourseService.deleteSemesterCourse(selectedSemesterCourse);
			refreshCourses();
			deleteSucessful();
		} else {
			operationFailed();
		}
		
		refreshSemesterCourses();
		clearSelectedSemesterCourse();
	}
	
	public boolean isSemesterCourseButtonsDisabled() {
		if(selectedSemesterCourse == null || (selectedSemesterCourse != null && selectedSemesterCourse.getId() == null)) {
			return true;
		}
		return false;
	}
	
	public void prepareCriteriaDialog() {
		refreshCriteria();
		onCriteriaUnselected();
	}
	
	public void addOrUpdateCriteria() {
		if(validateCriteria()) {
			try {
				if(updatingCriteria) {
					CourseService.updateCriteria(selectedCriteria);
					updateSuccessful();
				} else {
					selectedCriteria.setSemesterCourseId(selectedSemesterCourse.getId());
					CourseService.addCriteria(selectedCriteria);
					saveSuccessful();
				}
				
				onCriteriaUnselected();
				refreshCriteria();
			} catch (Exception e) {
				e.printStackTrace();
				saveOrUpdateFailed();
			}
		}
	}
	
	public boolean validateCriteria() {
		if(selectedCriteria == null || selectedSemesterCourse == null) {
			return validationFailed("gradeManagementForm:gc-item-selection", "error.erroOccurred");
		}
		
		int total = 0;
		boolean assignmentFound = false;
		boolean attendanceFound = false;
		boolean gamificationFound = false;
		for (GradeCriteria gc : selectedSemesterCourse.getGradeCriteria()) {
			total += gc.getWeight() == null ? 0 : gc.getWeight();
			
			if(gc.getGradingCriteria() == GradingCriteria.ASSIGNMENT.getIndex()) {
				assignmentFound = true;
			} else if (gc.getGradingCriteria() == GradingCriteria.ATTENDANCE.getIndex()) {
				attendanceFound = true;
			} else if (gc.getGradingCriteria() == GradingCriteria.GAMIFICATION.getIndex()) {
				gamificationFound = true;
			}
		}
		
		if(assignmentFound) {
			return validationFailed("gradeManagementForm:gc-item-selection", "error.courseManagement.assignmentCriteriaFound");
		}
		
		if(attendanceFound) {
			return validationFailed("gradeManagementForm:gc-item-selection", "error.courseManagement.attendanceCriteriaFound");
		}
		
		if(gamificationFound) {
			return validationFailed("gradeManagementForm:gc-item-selection", "error.courseManagement.gamificationCriteriaFound");
		}
		
		total += selectedCriteria.getWeight() == null ? 0 : selectedCriteria.getWeight();
		
		if(total > 100) {
			return validationFailed("gradeManagementForm:critweight", "error.weightOver100");
		}
		
		return true;
	}
	
	public void deleteCriteria() {
		try {
			CourseService.deleteCriteria(selectedCriteria);
			refreshCriteria();
			onCriteriaUnselected();
			deleteSucessful();
		} catch (Exception e) {
			e.printStackTrace();
			operationFailed();
		}
	}
	
	public void refreshCriteria() {
		gradeCriteria = CourseService.getCriteriaBySemesterCourse(selectedSemesterCourse.getId());
		refreshSemesterCourses();
	}
	
	public String getGradingCriteriaName(Integer gradingCriteria) {
		String name = "";
		if(gradingCriteria != null) {
			name = GradingCriteria.getLabel(gradingCriteria);
		}
		return name;
	}
	
	public void onCriteriaSelected() {
		if(selectedCriteria != null) {
			criteriaSelected = true;
			updatingCriteria = true;
		}
	}
	
	public void onGradingCriteriaSelected() {
		gradingCriteriaSelected = true;
	}
	
	public void onCriteriaUnselected() {
		criteriaSelected = false;
		updatingCriteria = false;
		
		selectedCriteria = new GradeCriteria();

		gradingCriteriaSelected = false;
	}
	
	public boolean isCriteriaButtonsRendered() {
		if(selectedCriteria == null || selectedCriteria.getId() == null) {
			return true;
		}
		return false;
	}
	
	public List<String> stringifyGradeCriteria(SemesterCourse sc) {
		List<String> gcStrings = new ArrayList<String>();
		
		if(sc != null && sc.getGradeCriteria() != null) {
			for (GradeCriteria gc : sc.getGradeCriteria()) {
				if(gc.getActive()) {
					String gcStr = gc.getName() + "(" + GradingCriteria.getLabel(gc.getGradingCriteria()) + ")" + ": " + Integer.toString(gc.getWeight()) + "%";
					gcStrings.add(gcStr);
				}
			}
		}
		return gcStrings;
	}
	
	public String getYearDates() {
		if(selectedYear == null || selectedYear.getId() == null)
			return "-";
		else {
			return selectedYear.getStartDateString() + " - " + selectedYear.getEndDateString();
		}
	}
	
	public String getSemesterDates() {
		if(selectedSemester == null || selectedSemester.getId() == null)
			return "-";
		else {
			return selectedSemester.getStartDateString() + " - " + selectedSemester.getEndDateString();
		}
	}
	
	public String getDialogHeader() {
		return updatingCourse ? ResourceUtil.getLabel("courseManagement.label.updateHeader") : ResourceUtil.getLabel("courseManagement.label.addHeader");
	}
	
	public String getDialogButtonLabel() {
		return ResourceUtil.getButtonLabel(updatingCourse);
	}
	
	public String getDialogButtonClass() {
		return ResourceUtil.getButtonClass(updatingCourse);
	}
	
	public String getSemCourseDialogHeader() {
		return updatingSemesterCourse ? ResourceUtil.getLabel("courseManagement.label.updateHeaderSemCourse") : ResourceUtil.getLabel("courseManagement.label.addHeaderSemCourse");
	}
	
	public String getSemCourseDialogButtonLabel() {
		return ResourceUtil.getButtonLabel(updatingSemesterCourse);
	}
	
	public String getSemCourseDialogButtonClass() {
		return ResourceUtil.getButtonClass(updatingSemesterCourse);
	}

	public List<SemesterCourse> getSemesterCourses() {
		return semesterCourses;
	}

	public void setSemesterCourses(List<SemesterCourse> semesterCourses) {
		this.semesterCourses = semesterCourses;
	}

	public SemesterCourse getSelectedSemesterCourse() {
		return selectedSemesterCourse;
	}

	public void setSelectedSemesterCourse(SemesterCourse selectedSemesterCourse) {
		this.selectedSemesterCourse = selectedSemesterCourse;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public Course getSelectedCourse() {
		return selectedCourse;
	}

	public void setSelectedCourse(Course selectedCourse) {
		this.selectedCourse = selectedCourse;
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

	public Course getSelectedDialogCourse() {
		return selectedDialogCourse;
	}

	public void setSelectedDialogCourse(Course selectedDialogCourse) {
		this.selectedDialogCourse = selectedDialogCourse;
	}

	public List<GradeCriteria> getGradeCriteria() {
		return gradeCriteria;
	}

	public void setGradeCriteria(List<GradeCriteria> gradeCriteria) {
		this.gradeCriteria = gradeCriteria;
	}

	public GradeCriteria getSelectedCriteria() {
		return selectedCriteria;
	}

	public void setSelectedCriteria(GradeCriteria selectedCriteria) {
		this.selectedCriteria = selectedCriteria;
	}

	public boolean isUpdatingCourse() {
		return updatingCourse;
	}

	public void setUpdatingCourse(boolean updatingCourse) {
		this.updatingCourse = updatingCourse;
	}

	public boolean isUpdatingSemesterCourse() {
		return updatingSemesterCourse;
	}

	public void setUpdatingSemesterCourse(boolean updatingSemesterCourse) {
		this.updatingSemesterCourse = updatingSemesterCourse;
	}

	public boolean isYearSelected() {
		return yearSelected;
	}

	public void setYearSelected(boolean yearSelected) {
		this.yearSelected = yearSelected;
	}

	public boolean isSemesterSelected() {
		return semesterSelected;
	}

	public void setSemesterSelected(boolean semesterSelected) {
		this.semesterSelected = semesterSelected;
	}

	public boolean isDialogCourseSelected() {
		return dialogCourseSelected;
	}

	public void setDialogCourseSelected(boolean dialogCourseSelected) {
		this.dialogCourseSelected = dialogCourseSelected;
	}

	public List<SelectItem> getGradingCriteriaItems() {
		return gradingCriteriaItems;
	}

	public void setGradingCriteriaItems(List<SelectItem> gradingCriteriaItems) {
		this.gradingCriteriaItems = gradingCriteriaItems;
	}

	public boolean isUpdatingCriteria() {
		return updatingCriteria;
	}

	public void setUpdatingCriteria(boolean updatingCriteria) {
		this.updatingCriteria = updatingCriteria;
	}

	public boolean isCriteriaSelected() {
		return criteriaSelected;
	}

	public void setCriteriaSelected(boolean criteriaSelected) {
		this.criteriaSelected = criteriaSelected;
	}

	public boolean isGradingCriteriaSelected() {
		return gradingCriteriaSelected;
	}

	public void setGradingCriteriaSelected(boolean gradingCriteriaSelected) {
		this.gradingCriteriaSelected = gradingCriteriaSelected;
	}
}
