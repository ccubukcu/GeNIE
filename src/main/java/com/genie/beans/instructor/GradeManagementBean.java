package com.genie.beans.instructor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.genie.beans.BaseBean;
import com.genie.enums.GradingCriteria;
import com.genie.model.Achievement;
import com.genie.model.Assignment;
import com.genie.model.AssignmentSubmission;
import com.genie.model.Document;
import com.genie.model.GradeCriteria;
import com.genie.model.SemesterCourse;
import com.genie.model.StudentAttendance;
import com.genie.model.StudentGrade;
import com.genie.model.User;
import com.genie.pojo.GradeColumn;
import com.genie.pojo.GradeWrapper;
import com.genie.pojo.StudentGradeRowWrapper;
import com.genie.pojo.StudentRowWrapper;
import com.genie.services.CoursePlanService;
import com.genie.services.CourseService;
import com.genie.services.GamificationService;
import com.genie.services.UserService;
import com.genie.utils.JsfUtil;
import com.genie.utils.ResourceUtil;

@ManagedBean
@ViewScoped
public class GradeManagementBean extends BaseBean {
	private static final long serialVersionUID = -9058734913279122003L;

	private List<SemesterCourse> courses;
	private SemesterCourse selectedCourse;

	private List<Assignment> assignments;
	private Assignment selectedAssignment;
	private List<AssignmentSubmission> assignmentSubmissions;
	private List<StudentRowWrapper> assignmentStudents;
	
	private List<GradeCriteria> exams;
	private GradeCriteria selectedExam;
	private List<StudentRowWrapper> examStudents;

	private List<GradeCriteria> otherGrades;
	private GradeCriteria selectedOtherGrade;
	private List<StudentRowWrapper> otherGradeStudents;

	private List<SelectItem> weeks;
	private Integer selectedWeek;
	private List<StudentRowWrapper> attendanceStudents;

	private List<StudentGradeRowWrapper> gradeStudents;
	private List<GradeColumn> otherGradeColumns;
	private List<GradeColumn> examGradeColumns;
	
	private List<User> students;
	private int selectedTab;

	private boolean courseSelected;
	private boolean examSelected;
	private boolean assignmentSelected;
	private boolean weekSelected;
	private boolean otherGradeSelected;
	
	@PostConstruct
	public void initBean() {
		refreshCourses();
		selectedTab = 0;
		
		courseSelected = false;
		examSelected = false;
		assignmentSelected = false;
		weekSelected = false;
		otherGradeSelected = false;
	}
	
	public void onCourseSelect() {
		courseSelected = true;
		
		examSelected = false;
		assignmentSelected = false;
		weekSelected = false;
		otherGradeSelected = false;
		
		selectedAssignment = null;
		selectedExam = null;
		selectedOtherGrade = null;
		selectedWeek = null;
		
		refreshStudents();
		refreshAssignments();
		refreshExams();
		refreshOtherGrades();
		refreshWeeks();
		
		refreshStudentFinalGrades();
	}
	
	public void refreshStudentFinalGrades() {
		List<GradeCriteria> criteria = CoursePlanService.getGradeCriteriaBySemesterCourse(selectedCourse.getId());
		
		gradeStudents = CourseService.calculateStudentGrades(students, criteria, selectedCourse.getId(), true, false);

		examGradeColumns = new ArrayList<GradeColumn>();
		if(gradeStudents != null && gradeStudents.size() > 0) {
			int i = 0;
			if(gradeStudents.get(0).getExamGrades() != null) {
				for (GradeWrapper gw : gradeStudents.get(0).getExamGrades()) {
					examGradeColumns.add(new GradeColumn(gw.getLabel(), i));
					i++;
				}
			}
		}
		
		otherGradeColumns = new ArrayList<GradeColumn>();
		if(gradeStudents != null && gradeStudents.size() > 0) {
			int i = 0;
			if(gradeStudents.get(0).getOtherGrades() != null) {
				for (GradeWrapper gw : gradeStudents.get(0).getOtherGrades()) {
					otherGradeColumns.add(new GradeColumn(gw.getLabel(), i));
					i++;
				}
			}
		}
	}
	
	public void refreshStudents() {
		students = UserService.getAllStudentsByCourseSemesterForGrading(selectedCourse.getCourseId(), selectedCourse.getSemesterId());
	}
	
	public void onExamSelect() {
		examSelected = true;
		examStudents = getWrappedStudentsForCriteria(selectedExam.getId());
	}
	
	public void onAssignmentSelect() {
		assignmentSelected = true;
		assignmentSubmissions = CoursePlanService.getSubmissionsByAssignmentId(selectedAssignment.getId());
		assignmentStudents = new ArrayList<StudentRowWrapper>();
		
		if(students != null && selectedCourse != null) {
			for (User stu : students) {
				StudentRowWrapper stuRow = new StudentRowWrapper();
				stuRow.setStudent(stu);
				boolean found = false;
				for (StudentGrade grade : stu.getGrades()) {
					if(grade.getSemesterCourseId().equals(selectedCourse.getId()) &&
							grade.getAssignmentId() == selectedAssignment.getId()) {
						stuRow.setGrade(grade);
						found = true;
					}
				}
				
				for (AssignmentSubmission as : assignmentSubmissions) {
					if(as.getStudentName().equals(stu.getUsername())) {
						stuRow.setSubmission(as);
					}
				}
				
				if(!found) {
					StudentGrade newGrade = new StudentGrade();
					newGrade.setGrade(0f);
					stuRow.setGrade(newGrade);
				}
				
				assignmentStudents.add(stuRow);
			}
		}
	}
	
	public void onOtherGradeSelect() {
		otherGradeSelected = true;
		otherGradeStudents = getWrappedStudentsForCriteria(selectedOtherGrade.getId());
	}
	
	public void onWeekSelect() {
		weekSelected = true;
		
		attendanceStudents = new ArrayList<StudentRowWrapper>();
		if(students != null && selectedCourse != null) {
			for (User stu : students) {
				StudentRowWrapper stuRow = new StudentRowWrapper();
				stuRow.setStudent(stu);
				boolean found = false;
				for (StudentAttendance attendance : stu.getAttendances()) {
					if(attendance.getSemesterCourseId().equals(selectedCourse.getId()) && 
							attendance.getWeek() == selectedWeek) {
						stuRow.setAttendance(attendance);
						found = true;
					}
				}
				
				if(!found) {
					StudentAttendance newAtt = new StudentAttendance();
					newAtt.setAttended(false);;
					stuRow.setAttendance(newAtt);
				}
				
				attendanceStudents.add(stuRow);
			}
		}
	}
	
	public List<StudentRowWrapper> getWrappedStudentsForCriteria(long criteriaId) {
		List<StudentRowWrapper> wrappedStudents = new ArrayList<StudentRowWrapper>();
		
		if(students != null && selectedCourse != null) {
			for (User stu : students) {
				StudentRowWrapper stuRow = new StudentRowWrapper();
				stuRow.setStudent(stu);
				boolean found = false;
				for (StudentGrade grade : stu.getGrades()) {
					if(grade.getSemesterCourseId().equals(selectedCourse.getId()) && grade.getGradeCriteriaId() != null &&
							grade.getGradeCriteriaId() == criteriaId) {
						stuRow.setGrade(grade);
						found = true;
					}
				}
				
				if(!found) {
					StudentGrade newGrade = new StudentGrade();
					newGrade.setGrade(0f);
					stuRow.setGrade(newGrade);
				}
				
				wrappedStudents.add(stuRow);
			}
		}
		
		return wrappedStudents;
	}
	
	public void updateExamGrades() {
		try {
			List<Achievement> achievements = GamificationService.getGradeAchievements(selectedCourse.getId());
			for (StudentRowWrapper srw : examStudents) {
				if(srw.getGrade().getId() != null) {
					CoursePlanService.updateGrade(srw.getGrade());
				} else {
					StudentGrade grade = srw.getGrade();
					grade.setSemesterCourseId(selectedCourse.getId());
					grade.setStudentName(srw.getStudent().getUsername());
					grade.setGradeCriteriaId(selectedExam.getId());
					
					CoursePlanService.addGrade(srw.getGrade());
				}
				
				GamificationService.recordGradeProgress(achievements, srw.getGrade(), selectedExam.getId());
			}
			
			saveSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			saveOrUpdateFailed();
		}
		
		refreshStudents();
		onExamSelect();
	}
	
	public void updateAssignmentGrades() {
		try {
			List<Achievement> achievements = GamificationService.getGradeAchievements(selectedCourse.getId());
			for (StudentRowWrapper srw : assignmentStudents) {
				if(srw.getGrade().getId() != null) {
					CoursePlanService.updateGrade(srw.getGrade());
				} else {
					StudentGrade grade = srw.getGrade();
					grade.setSemesterCourseId(selectedCourse.getId());
					grade.setStudentName(srw.getStudent().getUsername());
					grade.setAssignmentId(selectedAssignment.getId());
					
					CoursePlanService.addGrade(srw.getGrade());
				}
				
				GamificationService.recordGradeProgress(achievements, srw.getGrade(), selectedAssignment.getId());
			}
			
			saveSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			saveOrUpdateFailed();
		}

		refreshStudents();
		onAssignmentSelect();
	}
	
	public void updateAttendances() {
		try {
			List<Achievement> achievements = GamificationService.getAttendanceAchievements(selectedCourse.getId());
			int weekCount = CoursePlanService.getWeekCountInCourseSemester(selectedCourse.getId());
			for (StudentRowWrapper srw : attendanceStudents) {
				if(srw.getAttendance().getId() != null) {
					CoursePlanService.updateAttendance(srw.getAttendance());
				} else {
					StudentAttendance att = srw.getAttendance();
					
					att.setSemesterCourseId(selectedCourse.getId());
					att.setStudentName(srw.getStudent().getUsername());
					att.setWeek(selectedWeek);
					
					CoursePlanService.addAttendance(srw.getAttendance());
				}
				
				if(srw.getAttendance().getAttended()) {
					GamificationService.recordAttendanceProgress(achievements,
						srw.getStudent().getUsername(), selectedWeek, weekCount);
				}
			}
			
			saveSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			saveOrUpdateFailed();
		}

		refreshStudents();
		onWeekSelect();
	}
	
	public void updateOtherGrades() {
		try {
			List<Achievement> achievements = GamificationService.getGradeAchievements(selectedCourse.getId());
			for (StudentRowWrapper srw : otherGradeStudents) {
				if(srw.getGrade().getId() != null) {
					CoursePlanService.updateGrade(srw.getGrade());
				} else {
					StudentGrade grade = srw.getGrade();
					grade.setSemesterCourseId(selectedCourse.getId());
					grade.setStudentName(srw.getStudent().getUsername());
					grade.setGradeCriteriaId(selectedOtherGrade.getId());
					
					
					CoursePlanService.addGrade(srw.getGrade());
				}
				
				GamificationService.recordGradeProgress(achievements, srw.getGrade(), selectedOtherGrade.getId());
			}
			
			saveSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			saveOrUpdateFailed();
		}

		refreshStudents();
		onOtherGradeSelect();
	}
	
	public StreamedContent downloadAssignmentSubmission(String username) {
		StreamedContent file = null;
		if(username != null && selectedAssignment != null) {
			AssignmentSubmission submission = CoursePlanService.getAssignmentSubmissionForDownload(username, selectedAssignment.getId());
			
			if(submission != null && submission.getDocument() != null) {
				Document doc = submission.getDocument();
				String docType = doc.getDocumentType();
				String filename = doc.getFilename() + doc.getFiletype();
				
				try {
					byte[] fileBytes = doc.getFileData().getBytes(1, (int)doc.getFileData().length());
					InputStream is = new ByteArrayInputStream(fileBytes);
					file = new DefaultStreamedContent(is, docType, filename);
				} catch (SQLException e) {
					JsfUtil.addErrorMessage("growl.assignment.noSubmittedFileForDownloading");
					e.printStackTrace();
				}
				
			}
		}
		
		if(file == null) {
			JsfUtil.addErrorMessage("growl.assignment.noSubmittedFileForDownloading");
		}
		
		return file;
	}

	public void refreshCourses() {
		courses = CourseService.getAllSemesterCoursesForInstructor();
	}
	
	public void refreshAssignments() {
		assignments = CoursePlanService.getAllAssignmentsBySemesterCourse(selectedCourse.getId());
	}
	
	public void refreshExams() {
		exams = CoursePlanService.getGradeCriteriaByTypeAndSemesterCourse(GradingCriteria.EXAM, selectedCourse.getId());
	}
	
	public void refreshOtherGrades() {
		otherGrades = CoursePlanService.getGradeCriteriaByTypeAndSemesterCourse(GradingCriteria.OTHER, selectedCourse.getId());
	}
	
	public void refreshWeeks() {
		weeks = new ArrayList<SelectItem>();
		
		int weekCount = CoursePlanService.getWeekCountInCourseSemester(selectedCourse.getId());
		
		for(int i = 1; i < weekCount + 1; i++) {
			String prefix = ResourceUtil.getLabel("week.label.name");
			String label = prefix + " " + Integer.toString(i);
			
			SelectItem weekItem = new SelectItem(i, label);
			weeks.add(weekItem);
		}
	}
	
	public void onTabChange(TabChangeEvent event){
		TabView tabView = (TabView) event.getComponent();
        selectedTab = tabView.getChildren().indexOf(event.getTab());
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

	public List<GradeCriteria> getExams() {
		return exams;
	}

	public void setExams(List<GradeCriteria> exams) {
		this.exams = exams;
	}

	public GradeCriteria getSelectedExam() {
		return selectedExam;
	}

	public void setSelectedExam(GradeCriteria selectedExam) {
		this.selectedExam = selectedExam;
	}

	public List<GradeCriteria> getOtherGrades() {
		return otherGrades;
	}

	public void setOtherGrades(List<GradeCriteria> otherGrades) {
		this.otherGrades = otherGrades;
	}

	public GradeCriteria getSelectedOtherGrade() {
		return selectedOtherGrade;
	}

	public void setSelectedOtherGrade(GradeCriteria selectedOtherGrade) {
		this.selectedOtherGrade = selectedOtherGrade;
	}

	public List<SelectItem> getWeeks() {
		return weeks;
	}

	public void setWeeks(List<SelectItem> weeks) {
		this.weeks = weeks;
	}

	public Integer getSelectedWeek() {
		return selectedWeek;
	}

	public void setSelectedWeek(Integer selectedWeek) {
		this.selectedWeek = selectedWeek;
	}

	public boolean isExamSelected() {
		return examSelected;
	}

	public void setExamSelected(boolean examSelected) {
		this.examSelected = examSelected;
	}

	public boolean isAssignmentSelected() {
		return assignmentSelected;
	}

	public void setAssignmentSelected(boolean assignmentSelected) {
		this.assignmentSelected = assignmentSelected;
	}

	public boolean isWeekSelected() {
		return weekSelected;
	}

	public void setWeekSelected(boolean weekSelected) {
		this.weekSelected = weekSelected;
	}

	public boolean isOtherGradeSelected() {
		return otherGradeSelected;
	}

	public void setOtherGradeSelected(boolean otherGradeSelected) {
		this.otherGradeSelected = otherGradeSelected;
	}

	public List<StudentRowWrapper> getAssignmentStudents() {
		return assignmentStudents;
	}

	public void setAssignmentStudents(List<StudentRowWrapper> assignmentStudents) {
		this.assignmentStudents = assignmentStudents;
	}

	public List<StudentRowWrapper> getExamStudents() {
		return examStudents;
	}

	public void setExamStudents(List<StudentRowWrapper> examStudents) {
		this.examStudents = examStudents;
	}

	public List<StudentRowWrapper> getOtherGradeStudents() {
		return otherGradeStudents;
	}

	public void setOtherGradeStudents(List<StudentRowWrapper> otherGradeStudents) {
		this.otherGradeStudents = otherGradeStudents;
	}

	public List<StudentRowWrapper> getAttendanceStudents() {
		return attendanceStudents;
	}

	public void setAttendanceStudents(List<StudentRowWrapper> attendanceStudents) {
		this.attendanceStudents = attendanceStudents;
	}

	public List<User> getStudents() {
		return students;
	}

	public void setStudents(List<User> students) {
		this.students = students;
	}

	public int getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(int selectedTab) {
		this.selectedTab = selectedTab;
	}

	public List<StudentGradeRowWrapper> getGradeStudents() {
		return gradeStudents;
	}

	public void setGradeStudents(List<StudentGradeRowWrapper> gradeStudents) {
		this.gradeStudents = gradeStudents;
	}

	public List<GradeColumn> getOtherGradeColumns() {
		return otherGradeColumns;
	}

	public void setOtherGradeColumns(List<GradeColumn> otherGradeColumns) {
		this.otherGradeColumns = otherGradeColumns;
	}

	public List<GradeColumn> getExamGradeColumns() {
		return examGradeColumns;
	}

	public void setExamGradeColumns(List<GradeColumn> examGradeColumns) {
		this.examGradeColumns = examGradeColumns;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
