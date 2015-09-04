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
@Table(name = "leaderboard_settings")
public class LeaderboardSettings extends BaseEntity implements LoggableEntity {
	private static final long serialVersionUID = -5232625888157615869L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	@Column(name = "semester_course_id")
	private Long semesterCourseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "semester_course_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_leaderboards_semestercourse"))
	private SemesterCourse semesterCourse;

	@Column(name = "visibility")
	private int visibility;

	@Column(name = "is_anonymous")
	private boolean isAnonymous;
	
	@Column(name = "top_students")
	private int topStudents;

	@Column(name = "bottom_students")
	private int bottomStudents;

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

	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

	public boolean isAnonymous() {
		return isAnonymous;
	}

	public void setAnonymous(boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

	public int getTopStudents() {
		return topStudents;
	}

	public void setTopStudents(int topStudents) {
		this.topStudents = topStudents;
	}

	public int getBottomStudents() {
		return bottomStudents;
	}

	public void setBottomStudents(int bottomStudents) {
		this.bottomStudents = bottomStudents;
	}

}
