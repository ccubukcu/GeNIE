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

/**
 * 
 * @author ccubukcu
 */
@Entity
@Table(name = "attendances")
public class Attendance extends BaseEntity implements LoggableEntity {
	
	private static final long serialVersionUID = 6308718935289224453L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "week_id")
	private Long weekId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "week_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_attendance_semester"))
	private CoursePlan week;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "username", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_attendance_user"))
	private User user;

	@Column(name = "username")
	private String username;

	@Column(name = "course_id")
	private Long courseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_attendance_course"))
	private Course course;
	
	@Column(name = "status")
	private int status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getWeekId() {
		return weekId;
	}

	public void setWeekId(Long weekId) {
		this.weekId = weekId;
	}

	public CoursePlan getWeek() {
		return week;
	}

	public void setWeek(CoursePlan week) {
		this.week = week;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
	
}
