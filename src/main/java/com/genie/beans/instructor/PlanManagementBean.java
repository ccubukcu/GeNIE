package com.genie.beans.instructor;

import java.sql.Blob;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.genie.beans.BaseBean;
import com.genie.model.Assignment;
import com.genie.model.CourseMaterial;
import com.genie.model.CoursePlan;
import com.genie.model.Document;
import com.genie.model.SemesterCourse;
import com.genie.services.CoursePlanService;
import com.genie.services.CourseService;
import com.genie.services.DocumentService;
import com.genie.utils.DataFormatter;
import com.genie.utils.ResourceUtil;

/**
 * @author ccubukcu
 *
 */
@ManagedBean
@ViewScoped
public class PlanManagementBean extends BaseBean {
	private static final long serialVersionUID = 5227607783640655482L;

	private List<CoursePlan> plans;
	private CoursePlan selectedPlan;

	private List<SemesterCourse> courses;
	private SemesterCourse selectedCourse;
	private CoursePlan selectedFilterPlan;
	
	private List<CourseMaterial> materials;
	private CourseMaterial selectedMaterial;
	
	private List<Assignment> assignments;
	private Assignment selectedAssignment;
	private String dueDate;
	
	private boolean updating;
	private boolean updatingAssignment;
	private boolean courseSelected;
	
	@PostConstruct
	public void initBean() {
		refreshCourses();
		
		courseSelected = false;
	}
	
	public void clearSelectedPlan () {
		selectedPlan = null;
	}
	
	public void clearSelectedAssignment() {
		selectedAssignment = null;
	}
	
	public void refreshCourses() {
		courses = CourseService.getAllSemesterCoursesForInstructor();
	}
	
	public void refreshPlans() {
		if(selectedCourse != null)
			plans = CoursePlanService.getAllPlansBySemesterCourseIdWithMaterials(selectedCourse.getId());	
	}
	
	public void refreshAssignments() {
		assignments = CoursePlanService.getAssignmentsForSemesterCourse(selectedCourse.getId());
	}
	
	public void addOrUpdate() {
		try {
			selectedPlan.setSemesterCourseId(selectedCourse.getId());
			
			if(updating) {
				CoursePlanService.updatePlan(selectedPlan);
				updateSuccessful();
			} else {
				CoursePlanService.addPlan(selectedPlan);
				saveSuccessful();
			}
		} catch (Exception e) {
			e.printStackTrace();
			saveOrUpdateFailed();
		}
		clearSelectedPlan();
		refreshPlans();
	}
	
	public void delete() {
		if(selectedPlan != null && selectedPlan.getId() != null) {
			CoursePlanService.deletePlan(selectedPlan);
			clearSelectedPlan();
			refreshPlans();
			deleteSucessful();
		} else {
			operationFailed();
		}
	}
	
	public void handleFileUpload(FileUploadEvent event) {
		try {
			UploadedFile file = event.getFile();
			Document doc = new Document();
			String filename = file.getFileName();
			
			doc.setDocumentType(file.getContentType());
			doc.setFilename(filename.substring(0, filename.lastIndexOf('.')));
			doc.setFiletype(filename.substring(filename.lastIndexOf('.'), filename.length()));
			doc.setFilesize(file.getSize());
			doc.setUploadDate(new Date());
			
			byte[] data = IOUtils.toByteArray(file.getInputstream());
			Blob fileBlob = new SerialBlob(data);
			doc.setFileData(fileBlob);
			
			DocumentService.saveDocument(doc);
			
			CourseMaterial cm = new CourseMaterial();
			cm.setDocumentId(doc.getDocumentId());
			cm.setCoursePlanId(selectedPlan.getId());
			
			CoursePlanService.saveCourseMaterial(cm);
			
			prepareDocumentDialog();
		} catch (Exception e) {
			saveOrUpdateFailed();
		}
	}
	
	public void deleteCourseMaterial(CourseMaterial cm) {
		if(selectedCourse != null && selectedCourse.getId() != null) {
			DocumentService.delete(cm.getDocument());
			CoursePlanService.deleteMaterial(cm);
			prepareDocumentDialog();
			deleteSucessful();
		} else {
			operationFailed();
		}
	}
	

	public void addOrUpdateAssignment() {
		if(validate()) {
			try {
				selectedAssignment.setDueDate(DataFormatter.stringToDate(dueDate));
				selectedAssignment.setPlanId(selectedFilterPlan.getId());
				
				if(updatingAssignment) {
					CoursePlanService.updateAssignment(selectedAssignment);
					updateSuccessful();
				} else {
					CoursePlanService.addAssignment(selectedAssignment);
					saveSuccessful();
				}
			} catch (Exception e) {
				e.printStackTrace();
				saveOrUpdateFailed();
			}
			clearSelectedAssignment();
			refreshAssignments();
		}
	}
	
	public boolean validate() {
		int totalWeight = 0;
		
		if(assignments != null) {
			for (Assignment asgn : assignments) {
				totalWeight += asgn.getWeight() == null ? 0 : asgn.getWeight();
			}
		}
		
		if(totalWeight + selectedAssignment.getWeight() > 100) {
			return validationFailed("assignmentManagementForm:asgnWeight", "error.totalAssignmentWeightOver100");
		}
		
		return true;
	}
	
	public void deleteAssignment() {
		if(selectedAssignment != null && selectedAssignment.getId() != null) {
			CoursePlanService.deleteAssignment(selectedAssignment);
			clearSelectedAssignment();
			refreshAssignments();
			deleteSucessful();
		} else {
			operationFailed();
		}
	}
	
	public void onCourseSelect() {
		refreshPlans();
		refreshAssignments();
		courseSelected = true;
	}
	
	public void prepareAddDialog() {
		selectedPlan = new CoursePlan();
		selectedPlan.setPlanOrder(1);
		selectedPlan.setPlanLength(0);
		
		updating = false;
	}
	
	public void prepareUpdateDialog() {
		if(!isButtonsDisabled())
			updating = true;
		else updating = false;
	}
	
	public void prepareAddAssignmentDialog() {
		selectedAssignment = new Assignment();
		
		dueDate = null;
		selectedFilterPlan = null;
		updatingAssignment = false;
	}
	
	public void prepareUpdateAssignmentDialog() {
		updatingAssignment = true;
		
		dueDate = selectedAssignment.getDueDateString();
		selectedFilterPlan = selectedAssignment.getPlan();
	}
	
	public void prepareDocumentDialog() {
		materials = CoursePlanService.getMaterialsForPlanWithDocuments(selectedPlan.getId());
	}
	
	public boolean isAddButtonDisabled() {
		if(courseSelected == false) {
			return true;
		}
		return false;
	}
	
	public boolean isButtonsDisabled() {
		if(isAddButtonDisabled()) {
			return true;
		} else if(selectedPlan == null || (selectedPlan != null && selectedPlan.getId() == null)) {
			return true;
		}
		return false;
	}
	
	public boolean isAssignmentButtonsDisabled() {
		if(isAddButtonDisabled()) {
			return true;
		} else if(selectedAssignment == null || (selectedAssignment != null && selectedAssignment.getId() == null)) {
			return true;
		}
		return false;
	}
	
	public boolean deleteButtonDisabled() {
		if(isButtonsDisabled()) {
			return true;
		}
		return false;
	}
	
	public String getDialogHeader() {
		return isUpdating() ? ResourceUtil.getLabel("planManagement.label.updateHeader") : ResourceUtil.getLabel("planManagement.label.addHeader");
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
	
	public String getAssignmentDialogHeader() {
		return updatingAssignment ? ResourceUtil.getLabel("planManagement.label.updateAssignmentHeader") : ResourceUtil.getLabel("planManagement.label.addAssignmentHeader");
	}
	
	public String getAssignmentDialogButtonLabel() {
		return updatingAssignment ? ResourceUtil.getLabel("general.labels.edit") : ResourceUtil.getLabel("general.labels.add");
	}
	
	public String getAssignmentDialogButtonClass() {
		String baseClass = "submit-input grad-btn ln-tr";
		if(updatingAssignment) {
			baseClass += " edit-button";
		} else {
			baseClass += " add-button";
		}
		
		return baseClass;
	}
	
	public List<CoursePlan> getSemesterPlans() {
		return plans;
	}

	public void setSemesterPlans(List<CoursePlan> plans) {
		this.plans = plans;
	}

	public CoursePlan getSelectedPlan() {
		return selectedPlan;
	}

	public void setSelectedPlan(CoursePlan selectedPlan) {
		this.selectedPlan = selectedPlan;
	}

	public boolean isUpdating() {
		return updating;
	}

	public void setUpdating(boolean updating) {
		this.updating = updating;
	}

	public List<CoursePlan> getPlans() {
		return plans;
	}

	public void setPlans(List<CoursePlan> plans) {
		this.plans = plans;
	}

	public List<SemesterCourse> getCourses() {
		return courses;
	}

	public void setCourses(List<SemesterCourse> courses) {
		this.courses = courses;
	}

	public SemesterCourse getSelectedCourse() {
		return selectedCourse;
	}

	public void setSelectedCourse(SemesterCourse selectedCourse) {
		this.selectedCourse = selectedCourse;
	}

	public boolean isCourseSelected() {
		return courseSelected;
	}

	public void setCourseSelected(boolean courseSelected) {
		this.courseSelected = courseSelected;
	}

	public List<CourseMaterial> getMaterials() {
		return materials;
	}

	public void setMaterials(List<CourseMaterial> materials) {
		this.materials = materials;
	}

	public CourseMaterial getSelectedMaterial() {
		return selectedMaterial;
	}

	public void setSelectedMaterial(CourseMaterial selectedMaterial) {
		this.selectedMaterial = selectedMaterial;
	}

	public CoursePlan getSelectedFilterPlan() {
		return selectedFilterPlan;
	}

	public void setSelectedFilterPlan(CoursePlan selectedFilterPlan) {
		this.selectedFilterPlan = selectedFilterPlan;
	}

	public List<Assignment> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<Assignment> assignments) {
		this.assignments = assignments;
	}

	public Assignment getSelectedAssignment() {
		return selectedAssignment;
	}

	public void setSelectedAssignment(Assignment selectedAssignment) {
		this.selectedAssignment = selectedAssignment;
	}

	public boolean isUpdatingAssignment() {
		return updatingAssignment;
	}

	public void setUpdatingAssignment(boolean updatingAssignment) {
		this.updatingAssignment = updatingAssignment;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
}
