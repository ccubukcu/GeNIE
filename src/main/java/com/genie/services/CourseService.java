package com.genie.services;

import java.util.ArrayList;
import java.util.List;

import com.genie.enums.GradingCriteria;
import com.genie.model.Course;
import com.genie.model.GamificationSettings;
import com.genie.model.GradeCriteria;
import com.genie.model.SemesterCourse;
import com.genie.model.StudentAttendance;
import com.genie.model.StudentGrade;
import com.genie.model.StudentPoint;
import com.genie.model.User;
import com.genie.pojo.GradeWrapper;
import com.genie.pojo.StudentGradeRowWrapper;
import com.genie.utils.DaoUtil;
import com.genie.utils.ResourceUtil;

public class CourseService {

	public static List<SemesterCourse> getAllSemesterCoursesForInstructor() {
		String username = SessionService.getCurrentUser().getUsername();
		return DaoUtil.getCourseDAO().getAllSemesterCoursesForUser(username);
	}
	
	public static List<Course> getAllCoursesForInstructor() {
		String username = SessionService.getCurrentUser().getUsername();
		return DaoUtil.getCourseDAO().getAllCoursesForUser(username);
	}

	public static void updateCourse(Course selectedCourse) {
		DaoUtil.getCourseDAO().update(selectedCourse);
	}
	
	public static void saveCourse(Course selectedCourse) {
		String username = SessionService.getCurrentUser().getUsername();
		selectedCourse.setInstructor(username);
		DaoUtil.getCourseDAO().save(selectedCourse);
	}
	
	public static SemesterCourse getByIdWithEverything(Long id) {
		return DaoUtil.getSemesterCourseDAO().getByIdWithEverything(id);
	}
	
	public static SemesterCourse getById(Long id) {
		return DaoUtil.getSemesterCourseDAO().getById(id);
	}

	public static void deleteCourse(Course selectedCourse) {
		DaoUtil.getCourseDAO().delete(selectedCourse);
	}

	public static void deleteSemesterCourse(SemesterCourse selectedSemesterCourse) {
		DaoUtil.getSemesterCourseDAO().delete(selectedSemesterCourse);
	}

	public static void saveSemesterCourse(SemesterCourse selectedSemesterCourse) {
		DaoUtil.getSemesterCourseDAO().save(selectedSemesterCourse);
	}
	
	public static void updateSemesterCourse(SemesterCourse selectedSemesterCourse) {
		DaoUtil.getSemesterCourseDAO().update(selectedSemesterCourse);
	}

	public static void updateCriteria(GradeCriteria selectedCriteria) {
		DaoUtil.getSemesterCourseDAO().update(selectedCriteria);
	}

	public static void addCriteria(GradeCriteria selectedCriteria) {
		DaoUtil.getSemesterCourseDAO().save(selectedCriteria);
	}

	public static List<GradeCriteria> getCriteriaBySemesterCourse(Long id) {
		return DaoUtil.getSemesterCourseDAO().getGradeCriteriaBySemesterCourseId(id);
	}

	public static void deleteCriteria(GradeCriteria selectedCriteria) {
		DaoUtil.getSemesterCourseDAO().delete(selectedCriteria);
	}

	public static List<SemesterCourse> getAllForCourseList() {
		return DaoUtil.getSemesterCourseDAO().getAllForCourseList();
	}

	public static List<SemesterCourse> getAllCoursesForStudent() {
		String username = SessionService.getCurrentUser().getUsername();
		return DaoUtil.getSemesterCourseDAO().getAllCoursesForStudent(username);
	}

	public static List<StudentGradeRowWrapper> calculateStudentGrades(List<User> students, List<GradeCriteria> criteria, Long semesterCourseId, boolean weighted, boolean typeString) {
		List<StudentGradeRowWrapper> gradedStudents = new ArrayList<StudentGradeRowWrapper>();
		
		int weekCount = CoursePlanService.getWeekCountInCourseSemester(semesterCourseId);
		GamificationSettings generalSettings = GamificationService.getGeneralSettingsForSemesterCourse(semesterCourseId);
		
		int maxPoints = 0;
		if(generalSettings != null) {
			maxPoints = generalSettings.getMaxConvertablePoints();
		}
		
		if(students != null) {
			for (User stu : students) {
				float totalGrade = 0f;
				StudentGradeRowWrapper sgrw = new StudentGradeRowWrapper();
				
				for (GradeCriteria gc : criteria) {

					float weight = (weighted ? gc.getWeight() : 100.0f);
					
					if(gc.getGradingCriteria() == GradingCriteria.ATTENDANCE.getIndex()) {
						float attendanceGrade = weight * getAttendanceRatio(stu, weekCount);
						sgrw.setAttendanceGrade(createGradeWrapper(attendanceGrade, gc.getName()));
						totalGrade += attendanceGrade;
					}
					
					if(gc.getGradingCriteria() == GradingCriteria.GAMIFICATION.getIndex()) {
						float gamificationGrade = weight * getGamificationRatio(stu, maxPoints, semesterCourseId);
						sgrw.setGamificationGrade(createGradeWrapper(gamificationGrade, gc.getName()));
						totalGrade += gamificationGrade;
					}
					
					if(gc.getGradingCriteria() == GradingCriteria.ASSIGNMENT.getIndex()) {
						float assignmentGrade = weight * getAssignmentRatio(stu) / 100;
						sgrw.setFinalAssignmentGrades(createGradeWrapper(assignmentGrade, gc.getName()));
						totalGrade += assignmentGrade;
					}
					
					if(gc.getGradingCriteria() == GradingCriteria.EXAM.getIndex()) {
						float examGrade = 0f;
						if(stu.getGrades() != null) {
							for (StudentGrade sg : stu.getGrades()) {
								if(sg.getGradeCriteria() != null 
										&& sg.getGradeCriteria().getGradingCriteria() == GradingCriteria.EXAM.getIndex()
										&& gc.getId().equals(sg.getGradeCriteriaId())) {
									examGrade = sg.getGrade() * weight / 100;
								}
							}
						}
						sgrw.addToExamGrades(createGradeWrapper(examGrade, gc.getName()));
						totalGrade += examGrade;
					}
					
					if(gc.getGradingCriteria() == GradingCriteria.OTHER.getIndex()) {
						float otherGrade = 0f;
						if(stu.getGrades() != null) {
							for (StudentGrade sg : stu.getGrades()) {
								if(sg.getGradeCriteria() != null 
										&& sg.getGradeCriteria().getGradingCriteria() == GradingCriteria.OTHER.getIndex()
										&& gc.getId().equals(sg.getGradeCriteriaId())) {
									otherGrade = sg.getGrade() * weight / 100;
									totalGrade += otherGrade;
								}
							}
						}
						sgrw.addToOtherGrades(createGradeWrapper(otherGrade, gc.getName()));
					}
				}
				
				if(typeString)
					sgrw.setType(weighted ? ResourceUtil.getLabel("courseDetails.label.weightedGrade") : ResourceUtil.getLabel("courseDetails.label.rawGrade"));
				
				sgrw.setStudent(stu);
				sgrw.setFinalGrade(String.format("%.3f", totalGrade));
				gradedStudents.add(sgrw);
			}
		}
		
		return gradedStudents;
	}
	
	private static float getAssignmentRatio(User stu) {
		float assignmentGrade = 0f;
		
		if(stu.getGrades() != null) {
			for (StudentGrade sg : stu.getGrades()) {
				if(sg.getAssignment() != null && sg.getGrade() != null && sg.getAssignment().getWeight() != null) {
					assignmentGrade += sg.getGrade() * sg.getAssignment().getWeight();
					
				}
			}
		}
		
		return assignmentGrade / 100;
	}
	
	private static float getAttendanceRatio(User stu, int totalWeeks) {
		if(totalWeeks == 0)
			return 0;
		
		int totalAttended = 0;
		if(stu.getAttendances() != null)
			for (StudentAttendance sa : stu.getAttendances()) {
				if(sa.getAttended() == true)
					totalAttended++;
			}
		
		return (float)totalAttended / (float)totalWeeks;
	}
	
	private static float getGamificationRatio(User stu, int maxPoints, Long semesterCourseId) {
		float studentPoints = 0f;
		List<StudentPoint> points = GamificationService.getAllPointsForStudent(stu.getUsername(), semesterCourseId);
		
		if(points != null) {
			for (StudentPoint sp : points) {
				if(sp.getPoints() != null)
					studentPoints += sp.getPoints();
			}
		}
		
		if(studentPoints > maxPoints)
			return 1.0f;
		
		if(maxPoints == 0)
			maxPoints = 1;
		
		return studentPoints / maxPoints;
	}
	
	private static GradeWrapper createGradeWrapper(float grade, String label) {
		String gradeStr = String.format("%.3f", grade);
		return new GradeWrapper(gradeStr, label);
	}
}
