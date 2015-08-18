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
 * @author ccubukcu
 *
 */

@Entity
@Audited
@Table(name = "achievement_progress_items")
public class AchievementProgressItem extends BaseEntity implements LoggableEntity {

	private static final long serialVersionUID = 4011647151056332632L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	@Column(name = "achievement_progress_id")
	private Long achievementProgressId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "achievement_progress_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_pitems_progress"))
	private AchievementProgress achievemenProgress;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "grade_criteria_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_pitems_criteria"))
	private GradeCriteria gradeCriteria;
	
	@Column(name = "grade_criteria_id")	
	private Long gradeCriteriaId;

	@Column(name = "assignment_id")
	private Long assignmentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assignment_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_pitems_assignments"))
	private Assignment assignment;
	
	private int attendanceWeek;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAchievementProgressId() {
		return achievementProgressId;
	}

	public void setAchievementProgressId(Long achievementProgressId) {
		this.achievementProgressId = achievementProgressId;
	}

	public AchievementProgress getAchievemenProgress() {
		return achievemenProgress;
	}

	public void setAchievemenProgress(AchievementProgress achievemenProgress) {
		this.achievemenProgress = achievemenProgress;
	}

	public GradeCriteria getGradeCriteria() {
		return gradeCriteria;
	}

	public void setGradeCriteria(GradeCriteria gradeCriteria) {
		this.gradeCriteria = gradeCriteria;
	}

	public Long getGradeCriteriaId() {
		return gradeCriteriaId;
	}

	public void setGradeCriteriaId(Long gradeCriteriaId) {
		this.gradeCriteriaId = gradeCriteriaId;
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

	public int getAttendanceWeek() {
		return attendanceWeek;
	}

	public void setAttendanceWeek(int attendanceWeek) {
		this.attendanceWeek = attendanceWeek;
	}
	
}
