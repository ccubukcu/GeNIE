package com.genie.beans.admin;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.joda.time.DateTime;

import com.genie.beans.BaseBean;
import com.genie.model.SchoolYear;
import com.genie.services.SchoolYearService;
import com.genie.utils.DataFormatter;
import com.genie.utils.ResourceUtil;

/**
 * @author ccubukcu
 *
 */
@ManagedBean
@ViewScoped
public class YearManagementBean extends BaseBean {
	
	private static final long serialVersionUID = -6772247089602980052L;

	private List<SchoolYear> years;
	
	private SchoolYear selectedYear;
	private String startDate;
	private String endDate;
	private boolean updating;
	
	@PostConstruct
	public void initBean() {
		refreshYears();
	}
	
	public void refreshYears() {
		years = SchoolYearService.getAllWithSemesters();
	}
	
	public void addOrUpdate() {
		if(validateYear()) {
			try {
				selectedYear.setStartDate(DataFormatter.stringToDate(startDate));
				selectedYear.setEndDate(DataFormatter.stringToDate(endDate));
				
				if(updating) {
					SchoolYearService.updateSchoolYear(selectedYear);
					updateSuccessful();
				} else {
					SchoolYearService.saveSchoolYear(selectedYear);
					saveSuccessful();
				}
			} catch (Exception e) {
				e.printStackTrace();
				saveOrUpdateFailed();
			}
			
			clearSelectedYear();
			refreshYears();
		}
	}
	
	public void clearSelectedYear () {
		selectedYear = null;
		startDate = null;
		endDate = null;
	}
	
	public void delete() {
		if(getSemesterCount(selectedYear) == 0 && selectedYear != null && selectedYear.getId() != null) {
			SchoolYearService.deleteSchoolYear(selectedYear);
			refreshYears();
			deleteSucessful();
		} else {
			operationFailedWithMessage("growl.yearManagement.cannotDeleteWithSemesters");
		}
	}
	
	public boolean validateYear() {
		DateTime start = new DateTime(DataFormatter.stringToDate(startDate));
		DateTime end = new DateTime(DataFormatter.stringToDate(endDate));
		
		if(end.isBefore(start)) {
			return validationFailed("yearManagementForm:endDate", "error.endDateMustBeLater");
		} else {
			DateTime minDays = start.plusDays(30);
			
			if(end.isBefore(minDays)) {
				return validationFailed("yearManagementForm:endDate", "error.mustBeAtLeast30Days");
			}
		}
		return true;
	}
	
	public void prepareAddDialog() {
		selectedYear = new SchoolYear();
		updating = false;
	}
	
	public void prepareUpdateDialog() {
		if(!isButtonsDisabled())
			updating = true;
		else updating = false;
		
		startDate = selectedYear.getStartDateString();
		endDate = selectedYear.getEndDateString();
	}
	
	public boolean isButtonsDisabled() {
		if(selectedYear == null || (selectedYear != null && selectedYear.getId() == null)) {
			return true;
		}
		return false;
	}
	
	public boolean deleteButtonDisabled() {
		if(isButtonsDisabled()) {
			return true;
		} else if (getSemesterCount(selectedYear) > 0 ) {
			return true;
		}
		return false;
	}
	
	public int getSemesterCount(SchoolYear year) {
		if(year.getSemesters() == null) return 0;
		else return year.getSemesters().size();
	}
	
	public String getDialogHeader() {
		return isUpdating() ? ResourceUtil.getLabel("yearManagement.label.updateHeader") : ResourceUtil.getLabel("yearManagement.label.addHeader");
	}
	
	public String getDialogButtonLabel() {
		return ResourceUtil.getButtonLabel(isUpdating());
	}
	
	public String getDialogButtonClass() {
		return ResourceUtil.getButtonClass(isUpdating());
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

	public boolean isUpdating() {
		return updating;
	}

	public void setUpdating(boolean updating) {
		this.updating = updating;
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
}
