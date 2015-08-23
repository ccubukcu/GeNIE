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
 * @author ccubukcu
 *
 */

@Entity
@Audited
@Table(name = "student_badges")
public class StudentBadge extends BaseEntity implements LoggableEntity {
	
	private static final long serialVersionUID = 7726978351830869134L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	@Column(name = "username")
	private String username;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "username", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_sbadge_user"))
	private User user;
	
	@Column(name = "badge_id")
	private Long badgeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "badge_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_sbadge_badge"))
	private Badge badge;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getBadgeId() {
		return badgeId;
	}

	public void setBadgeId(Long badgeId) {
		this.badgeId = badgeId;
	}

	public Badge getBadge() {
		return badge;
	}

	public void setBadge(Badge badge) {
		this.badge = badge;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
