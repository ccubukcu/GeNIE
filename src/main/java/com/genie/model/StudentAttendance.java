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

import com.genie.utils.LoggableEntity;

/**
 * 
 * @author ccubukcu
 */
@Audited
@Entity
@Table(name = "student_attendance")
public class StudentAttendance extends BaseEntity implements LoggableEntity  {
	
	private static final long serialVersionUID = 7781799759805431062L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "semester_course_id")
	private Long semesterCourseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "semester_course_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_attendance_semcourses"))
	private SemesterCourse semesterCourse;
	
	@Column(name = "student_name")
	private String studentName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_name", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_attendance_users"))
	private User student;
	
	@Column(name = "attended")
	private Boolean attended;
	
	@Column(name = "attendance_week")
	private Integer week;

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

	public Boolean getAttended() {
		return attended;
	}

	public void setAttended(Boolean attended) {
		this.attended = attended;
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}
}
