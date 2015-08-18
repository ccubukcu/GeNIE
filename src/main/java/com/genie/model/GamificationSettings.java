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
@Table(name = "gamification_settings")
public class GamificationSettings extends BaseEntity implements LoggableEntity {
	private static final long serialVersionUID = 2745221411981883744L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	@Column(name = "semester_course_id")
	private Long semesterCourseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "semester_course_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_gamification_semestercourse"))
	private SemesterCourse semesterCourse;

	@Column(name = "gamification_enabled")
	private boolean gamificationEnabled;

	@Column(name = "leaderboards_enabled")
	private boolean leaderboardsEnabled;

	@Column(name = "badges_enabled")
	private boolean badgesEnabled;

	@Column(name = "achievements_enabled")
	private boolean achievementsEnabled;

	@Column(name = "points_name")
	private String pointsName;
	
	@Column(name = "max_convertable_points")
	private int maxConvertablePoints;

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

	public String getPointsName() {
		return pointsName;
	}

	public void setPointsName(String pointsName) {
		this.pointsName = pointsName;
	}

	public int getMaxConvertablePoints() {
		return maxConvertablePoints;
	}

	public void setMaxConvertablePoints(int maxConvertablePoints) {
		this.maxConvertablePoints = maxConvertablePoints;
	}
}
