package com.genie.beans.admin;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.joda.time.DateTime;

import com.genie.beans.BaseBean;
import com.genie.model.SchoolYear;
import com.genie.model.Semester;
import com.genie.services.SchoolYearService;
import com.genie.services.SemesterService;
import com.genie.utils.DataFormatter;
import com.genie.utils.ResourceUtil;

/**
 * @author ccubukcu
 *
 */
@ManagedBean
@ViewScoped
public class SemesterManagementBean extends BaseBean {

	private static final long serialVersionUID = 1133406891053381763L;

	private List<Semester> semesters;
	private SchoolYear selectedYear;
	private List<SchoolYear> years;
	
	private Semester selectedSemester;
	private String startDate;
	private String endDate;
	private boolean updating;
	
	@PostConstruct
	public void initBean() {
		refreshSemesters();
	}
	
	public boolean isSemesterDisabled(int semester) {
		if(selectedYear == null || selectedSemester == null) {
			return true;
		} else if (updating && semester == selectedSemester.getSemesterOrder()) {
			return false;
		}
		else {
			return SchoolYearService.isSemesterCreated(selectedYear.getId(), semester);
		}
	}
	
	public void refreshSemesters() {
		semesters = SemesterService.getAllWithCourses();	
	}
	
	public void addOrUpdate() {
		if(validateSemester()) {
			try {
				selectedSemester.setStartDate(DataFormatter.stringToDate(startDate));
				selectedSemester.setEndDate(DataFormatter.stringToDate(endDate));
				selectedSemester.setSchoolYearId(selectedYear.getId());
				
				if(updating) {
					SemesterService.updateSemester(selectedSemester);
					updateSuccessful();
				} else {
					SemesterService.saveSemester(selectedSemester);
					saveSuccessful();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				saveOrUpdateFailed();
			}
			clearSelectedSemester();
			refreshSemesters();
		}
	}
	
	public void clearSelectedSemester () {
		selectedSemester = null;
		startDate = null;
		endDate = null;
	}
	
	public void delete() {
		if(getCoursesCount(selectedSemester) == 0 && selectedSemester != null && selectedSemester.getId() != null) {
			SemesterService.deleteSemester(selectedSemester);
			refreshSemesters();
			deleteSucessful();
		} else {
			operationFailedWithMessage("growl.semesterManagement.cannotDeleteWithCourses");
		}
	}
	
	public boolean validateSemester() {
		DateTime start = new DateTime(DataFormatter.stringToDate(startDate));
		DateTime end = new DateTime(DataFormatter.stringToDate(endDate));
		
		if(end.isBefore(start)) {
			return validationFailed("semesterManagementForm:endDate", "error.endDateMustBeLater");
		} if(selectedYear != null) {
			DateTime yearStart = new DateTime(selectedYear.getStartDate());
			DateTime yearEnd = new DateTime(selectedYear.getEndDate());
			
			if(start.isBefore(yearStart) && !start.isEqual(yearStart)) {
				return validationFailed("semesterManagementForm:startDate", "error.mustBeAfterYearStart");
			}
			
			if(end.isAfter(yearEnd) && !end.isEqual(yearEnd)) {
				return validationFailed("semesterManagementForm:endDate", "error.mustBeBeforeYearEnd");
			}
		} else {
			DateTime minDays = start.plusDays(30);
			
			if(end.isBefore(minDays)) {
				return validationFailed("semesterManagementForm:endDate", "error.mustBeAtLeast30Days");
			}
		}
		return true;
	}
	
	public String getYearDates() {
		if(selectedYear == null || selectedYear.getId() == null)
			return "-";
		else {
			return selectedYear.getStartDateString() + " - " + selectedYear.getEndDateString();
		}
	}
	
	public void refreshYears() {
		years = SchoolYearService.getAll();
	}
	
	public void prepareAddDialog() {
		refreshYears();
		selectedSemester = new Semester();
		updating = false;
		
		startDate = null;
		endDate = null;
	}
	
	public void prepareUpdateDialog() {
		refreshYears();
		if(!isButtonsDisabled())
			updating = true;
		else updating = false;
		
		startDate = selectedSemester.getStartDateString();
		endDate = selectedSemester.getEndDateString();
		
		selectedSemester = SemesterService.getByIdWithYear(selectedSemester.getId());
		
		selectedYear = selectedSemester.getSchoolYear();
	}
	
	public boolean isButtonsDisabled() {
		if(selectedSemester == null || (selectedSemester != null && selectedSemester.getId() == null)) {
			return true;
		}
		return false;
	}
	
	public boolean deleteButtonDisabled() {
		if(isButtonsDisabled()) {
			return true;
		} else if (getCoursesCount(selectedSemester) > 0 ) {
			return true;
		}
		return false;
	}
	
	public int getCoursesCount(Semester semester) {
		if(semester.getSemesterCourses() == null) return 0;
		else return semester.getSemesterCourses().size();
	}
	
	public String getDialogHeader() {
		return isUpdating() ? ResourceUtil.getLabel("semesterManagement.label.updateHeader") : ResourceUtil.getLabel("semesterManagement.label.addHeader");
	}
	
	public String getDialogButtonLabel() {
		return isUpdating() ? ResourceUtil.getLabel("general.labels.edit") : ResourceUtil.getLabel("general.labels.add");
	}
	
	public String getDialogButtonClass() {
		String baseClass = "submit-input grad-btn ln-tr";
		if(updating) {
			baseClass += " edit-button";
		} else {
			baseClass += " add-button";
		}
		
		return baseClass;
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public boolean isUpdating() {
		return updating;
	}

	public void setUpdating(boolean updating) {
		this.updating = updating;
	}

	public SchoolYear getSelectedYear() {
		return selectedYear;
	}

	public void setSelectedYear(SchoolYear selectedYear) {
		this.selectedYear = selectedYear;
	}

	public List<SchoolYear> getYears() {
		return years;
	}

	public void setYears(List<SchoolYear> years) {
		this.years = years;
	}
}
