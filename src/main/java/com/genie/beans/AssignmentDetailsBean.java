package com.genie.beans;

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

import com.genie.model.Achievement;
import com.genie.model.Assignment;
import com.genie.model.AssignmentSubmission;
import com.genie.model.Document;
import com.genie.services.CoursePlanService;
import com.genie.services.DocumentService;
import com.genie.services.GamificationService;
import com.genie.services.SessionService;
import com.genie.utils.JsfUtil;

/**
 * @author ccubukcu
 *
 */
@ManagedBean
@ViewScoped
public class AssignmentDetailsBean extends BaseBean {
	private static final long serialVersionUID = 620255322003259334L;

	private static String assignmentIdStr;
	private long assignmentId;
	private Assignment assignment;
	
	private AssignmentSubmission submission;
	private AssignmentSubmission oldSubmission;
	
	@PostConstruct
	public void initBean() {
		if(assignmentIdStr != null) {
			try {
				assignmentId = Long.parseLong(assignmentIdStr);
				assignment = CoursePlanService.getAssignmentById(assignmentId);
				
				submission = CoursePlanService.getAssignmentSubmissionForStudent(assignmentId);
			} catch (Exception e) {
				e.printStackTrace();
				assignmentId = 0;
				assignment = null;
			}
		}
	}
	
	public boolean isSubmissionFound() {
		if(submission != null && submission.getId() > 0) {
			return true;
		}
		return false;
	}
	
	public static void loadUrlParameters(String asgnid) {
		assignmentIdStr = asgnid;
	}
	
	public void handleFileUpload(FileUploadEvent event) {
		try {
			if(assignment != null && assignment.getId() != null) {
				boolean isFirstSubmit = true;
				if(submission != null) {
					oldSubmission = submission;
					isFirstSubmit = false;
				}
				
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
				
				submission = new AssignmentSubmission();
				submission.setDocument(doc);
				submission.setDocumentId(doc.getDocumentId());
				submission.setAssignment(assignment);
				submission.setAssignmentId(assignmentId);
				submission.setStudentName(SessionService.getUsername());
				
				CoursePlanService.saveAssignmentSubmission(submission);
				
				if(oldSubmission != null) {
					DocumentService.delete(oldSubmission.getDocument());
					CoursePlanService.deleteSubmission(oldSubmission);
					oldSubmission = null;
				}
				
				if(isFirstSubmit) {
					List<Achievement> achievements = GamificationService.getSubmissionAchievements(assignment.getId());

					GamificationService.recordSubmissionProgress(achievements, submission);
				}
			} else {
				JsfUtil.addErrorMessage("growl.errorWhileSaving");
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("growl.errorWhileSaving");
		}
	}

	public static String getAssignmentIdStr() {
		return assignmentIdStr;
	}

	public static void setAssignmentIdStr(String assignmentIdStr) {
		AssignmentDetailsBean.assignmentIdStr = assignmentIdStr;
	}

	public long getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(long assignmentId) {
		this.assignmentId = assignmentId;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public AssignmentSubmission getSubmission() {
		return submission;
	}

	public void setSubmission(AssignmentSubmission submission) {
		this.submission = submission;
	}

	public AssignmentSubmission getOldSubmission() {
		return oldSubmission;
	}

	public void setOldSubmission(AssignmentSubmission oldSubmission) {
		this.oldSubmission = oldSubmission;
	}
}
