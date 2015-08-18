package com.genie.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * 
 * @author ccubukcu
 */
@Audited
@Entity
@Table(name = "student_grades")
public class StudentGrade extends BaseEntity implements LoggableEntity {
	
	private static final long serialVersionUID = -381994423541451035L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "semester_course_id")
	private Long semesterCourseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "semester_course_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_grade_semesters_courses"))
	private SemesterCourse semesterCourse;
	
	@Column(name = "grade_criteria_id")
	private Long gradeCriteriaId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "grade_criteria_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_grades_criteria"))
	private GradeCriteria gradeCriteria;
	
	@Column(name = "assignment_id")
	private Long assignmentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assignment_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_grades_assignments"))
	private Assignment assignment;
	
	@Column(name = "student_name")
	private String studentName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_name", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_grades_users"))
	private User student;
	
	@Column(name = "grade")
	private Float grade;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSemesterCourseId() {
		return semesterCourseId;
	}

	public void setSemesterCourseId(Long semesterCourseId) {
		this.semesterCourseId = semesterCourseId;
	}

	public SemesterCourse getSemesterCourse() {
		return semesterCourse;
	}

	public void setSemesterCourse(SemesterCourse semesterCourse) {
		this.semesterCourse = semesterCourse;
	}

	public Long getGradeCriteriaId() {
		return gradeCriteriaId;
	}

	public void setGradeCriteriaId(Long gradeCriteriaId) {
		this.gradeCriteriaId = gradeCriteriaId;
	}

	public GradeCriteria getGradeCriteria() {
		return gradeCriteria;
	}

	public void setGradeCriteria(GradeCriteria gradeCriteria) {
		this.gradeCriteria = gradeCriteria;
	}

	public Float getGrade() {
		return grade;
	}

	public void setGrade(Float grade) {
		this.grade = grade;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public Long getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(Long assignmentId) {
		this.assignmentId = assignmentId;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}
}
