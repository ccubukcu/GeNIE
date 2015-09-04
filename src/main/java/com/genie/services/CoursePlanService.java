package com.genie.services;

import java.util.ArrayList;
import java.util.List;

import com.genie.enums.GradingCriteria;
import com.genie.model.Assignment;
import com.genie.model.AssignmentSubmission;
import com.genie.model.CourseMaterial;
import com.genie.model.CoursePlan;
import com.genie.model.GradeCriteria;
import com.genie.model.StudentAttendance;
import com.genie.model.StudentGrade;
import com.genie.model.User;
import com.genie.pojo.AssignmentGradeWrapper;
import com.genie.pojo.AttendanceDetailsWrapper;
import com.genie.pojo.AttendanceWrapper;
import com.genie.pojo.GradeWrapper;
import com.genie.utils.DaoUtil;

public class CoursePlanService {
	
	public static void addPlan(CoursePlan plan) {
		DaoUtil.getCoursePlanDAO().save(plan);
	}
	
	public static void updatePlan(CoursePlan plan) {
		DaoUtil.getCoursePlanDAO().update(plan);
	}
	
	public static void deletePlan(CoursePlan plan) {
		DaoUtil.getCoursePlanDAO().delete(plan);
	}

	public static CoursePlan getByIdWithSemesterAndCourse(Long id) {
		return DaoUtil.getCoursePlanDAO().getByIdWithSemesterAndCourse(id);
	}

	public static List<CoursePlan> getAllPlansBySemesterCourseId(Long id) {
		return DaoUtil.getCoursePlanDAO().getAllPlansBySemesterCourseId(id);
	}

	public static void saveCourseMaterial(CourseMaterial cm) {
		DaoUtil.getCourseMaterialDAO().save(cm);
	}

	public static List<CoursePlan> getAllPlansBySemesterCourseIdWithMaterials(Long id) {
		return DaoUtil.getCoursePlanDAO().getAllPlansBySemesterCourseIdWithMaterials(id);
	}

	public static List<CoursePlan> getAllPlansBySemesterCourseIdWithMaterialDocuments(Long id) {
		return DaoUtil.getCoursePlanDAO().getAllPlansBySemesterCourseIdWithMaterialDocuments(id);
	}

	public static List<CourseMaterial> getMaterialsForPlanWithDocuments(Long id) {
		return DaoUtil.getCourseMaterialDAO().getAllByPlanIdWithDocuments(id);
	}

	public static void deleteMaterial(CourseMaterial cm) {
		DaoUtil.getCourseMaterialDAO().delete(cm);
	}

	public static void deleteAssignment(Assignment selectedAssignment) {
		DaoUtil.getAssignmentDAO().delete(selectedAssignment);
	}

	public static List<Assignment> getAssignmentsForSemesterCourse(Long courseId) {
		return DaoUtil.getAssignmentDAO().getAllBySemesterCourseId(courseId);
	}

	public static void updateAssignment(Assignment selectedAssignment) {
		DaoUtil.getAssignmentDAO().update(selectedAssignment);
	}

	public static void addAssignment(Assignment selectedAssignment) {
		DaoUtil.getAssignmentDAO().save(selectedAssignment);
	}
	
	public static List<GradeCriteria> getGradeCriteriaByTypeAndSemesterCourse(GradingCriteria type, Long semesterCourseId) {
		return DaoUtil.getCoursePlanDAO().getGradeCriteriaByTypeAndSemesterCourse(type.getIndex(), semesterCourseId);
	}

	public static List<Assignment> getAllAssignmentsBySemesterCourse(Long id) {
		return DaoUtil.getCoursePlanDAO().getAllAssignmentsBySemesterCourse(id);
	}

	public static int getWeekCountInCourseSemester(Long id) {
		List<CoursePlan> plans = DaoUtil.getCoursePlanDAO().getAllPlansBySemesterCourseId(id);
		
		int total = 0;
		if(plans != null) {
			for (CoursePlan cp : plans) {
				total += cp.getPlanLength();
			}
		}
		
		return total;
	}

	public static void updateGrade(StudentGrade grade) {
		DaoUtil.getCoursePlanDAO().update(grade);
	}

	public static void addGrade(StudentGrade grade) {
		DaoUtil.getCoursePlanDAO().save(grade);
	}

	public static void updateAttendance(StudentAttendance attendance) {
		DaoUtil.getCoursePlanDAO().update(attendance);
	}

	public static void addAttendance(StudentAttendance attendance) {
		DaoUtil.getCoursePlanDAO().save(attendance);
	}

	public static AssignmentSubmission getAssignmentSubmissionForDownload(String username, Long assignmentId) {
		return DaoUtil.getAssignmentDAO().getSubmissionWithDocument(username, assignmentId);
	}

	public static Assignment getAssignmentById(long assignmentId) {
		return DaoUtil.getAssignmentDAO().getById(assignmentId);
	}

	public static AssignmentSubmission getAssignmentSubmissionForStudent(long assignmentId) {
		String username = SessionService.getCurrentUser().getUsername();
		return DaoUtil.getAssignmentDAO().getSubmissionWithDocument(username, assignmentId);
	}

	public static void saveAssignmentSubmission(AssignmentSubmission as) {
		DaoUtil.getAssignmentDAO().save(as);
	}

	public static void deleteSubmission(AssignmentSubmission submission) {
		DaoUtil.getAssignmentDAO().deleteSubmission(submission);
	}

	public static List<GradeCriteria> getGradeCriteriaBySemesterCourse(Long semesterCourseId) {
		return DaoUtil.getCoursePlanDAO().getGradeCriteriaBySemesterCourse(semesterCourseId);
	}

	public static List<AssignmentSubmission> getSubmissionsByAssignmentId(Long id) {
		return DaoUtil.getCoursePlanDAO().getAllSubmissionsByAssignmentId(id);
	}

	public static List<AttendanceDetailsWrapper> getAttendanceDetails(User currentStudent, Long semesterCourseId) {
		int weeks = getWeekCountInCourseSemester(semesterCourseId);
		
		List<AttendanceWrapper> attendances = new ArrayList<AttendanceWrapper>();
		
		for(int i=1; i <= weeks; i++) {
			attendances.add(new AttendanceWrapper(i));
		}
		
		for (StudentAttendance att : currentStudent.getAttendances()) {
			if(att.getSemesterCourseId().equals(semesterCourseId)) {
				attendances.get(att.getWeek()).setAttendance(att.getAttended());
			}
		}
		
		AttendanceDetailsWrapper adw = new AttendanceDetailsWrapper();
		adw.setU(currentStudent);;
		adw.setAttendances(attendances);
		
		List<AttendanceDetailsWrapper> adws = new ArrayList<AttendanceDetailsWrapper>();
		adws.add(adw);
		
		return adws;
	}

	public static List<AssignmentGradeWrapper> getAssignmentGrades(User currentStudent, Long semesterCourseId) {
		List<Assignment> assignments = getAllAssignmentsBySemesterCourse(semesterCourseId);
		
		List<GradeWrapper> gradeWrappers = new ArrayList<GradeWrapper>();
		for (Assignment assignment : assignments) {
			GradeWrapper gradeWrapper = new GradeWrapper();
			gradeWrapper.setLabel(assignment.getName());
			
			for (StudentGrade grade : currentStudent.getGrades()) {
				if(grade.getAssignmentId() != null && grade.getAssignmentId().equals(assignment.getId())) {
					gradeWrapper.setGrade(grade.getGrade().toString());
					break;
				} else {
					gradeWrapper.setGrade("-");
				}
			}
			
			gradeWrappers.add(gradeWrapper);
		}

		AssignmentGradeWrapper agw = new AssignmentGradeWrapper();
		agw.setU(currentStudent);;
		agw.setGrades(gradeWrappers);
		
		List<AssignmentGradeWrapper> assignmentWrappers = new ArrayList<AssignmentGradeWrapper>();
		assignmentWrappers.add(agw);
		
		return assignmentWrappers;
	}
}
