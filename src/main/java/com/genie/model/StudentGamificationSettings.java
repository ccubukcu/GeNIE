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
@Table(name = "student_gamification_settings")
public class StudentGamificationSettings extends BaseEntity implements LoggableEntity {
	private static final long serialVersionUID = -9154165446313031195L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "student_name")
	private String studentName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_name", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_stu_gsettings_user"))
	private User student;

	@Column(name = "semester_course_id")
	private Long semesterCourseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "semester_course_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_stu_gsettings_semcourse"))
	private SemesterCourse semesterCourse;
	
	@Column(name = "gamification_enabled")
	private boolean gamificationEnabled;

	@Column(name = "leaderboards_enabled")
	private boolean leaderboardsEnabled;

	@Column(name = "badges_enabled")
	private boolean badgesEnabled;

	@Column(name = "achievements_enabled")
	private boolean achievementsEnabled;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public boolean isGamificationEnabled() {
		return gamificationEnabled;
	}

	public void setGamificationEnabled(boolean gamificationEnabled) {
		this.gamificationEnabled = gamificationEnabled;
	}

	public boolean isLeaderboardsEnabled() {
		return leaderboardsEnabled;
	}

	public void setLeaderboardsEnabled(boolean leaderboardsEnabled) {
		this.leaderboardsEnabled = leaderboardsEnabled;
	}

	public boolean isBadgesEnabled() {
		return badgesEnabled;
	}

	public void setBadgesEnabled(boolean badgesEnabled) {
		this.badgesEnabled = badgesEnabled;
	}

	public boolean isAchievementsEnabled() {
		return achievementsEnabled;
	}

	public void setAchievementsEnabled(boolean achievementsEnabled) {
		this.achievementsEnabled = achievementsEnabled;
	}
}
