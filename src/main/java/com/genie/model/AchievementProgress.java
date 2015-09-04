package com.genie.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import com.genie.utils.LoggableEntity;

/**
 * @author ccubukcu
 *
 */

@Entity
@Audited
@Table(name = "achievement_progress")
public class AchievementProgress extends BaseEntity implements LoggableEntity {
	
	private static final long serialVersionUID = -5270783963489640878L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	@Column(name = "achievement_id")
	private Long achievementId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "achievement_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_progress_achievement"))
	private Achievement achievement;
	
	@Column(name = "username")
	private String username;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "username", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_progress_user"))
	private User user;

	@Column(name = "current_value")
	private Integer currentValue;

	@Column(name = "is_complete")
	private boolean isComplete;

	@OneToMany(mappedBy = "achievemenProgress", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Where(clause="active=1")
	private List<AchievementProgressItem> progressItems;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAchievementId() {
		return achievementId;
	}

	public void setAchievementId(Long achievementId) {
		this.achievementId = achievementId;
	}

	public Achievement getAchievement() {
		return achievement;
	}

	public void setAchievement(Achievement achievement) {
		this.achievement = achievement;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(Integer currentValue) {
		this.currentValue = currentValue;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public List<AchievementProgressItem> getProgressItems() {
		return progressItems;
	}

	public void setProgressItems(List<AchievementProgressItem> progressItems) {
		this.progressItems = progressItems;
	}
}
