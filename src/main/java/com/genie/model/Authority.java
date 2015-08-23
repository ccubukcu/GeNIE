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
@Table(name = "authorities")
public class Authority extends BaseEntity implements LoggableEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "authority", length = 100)
	private String authority;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "username", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_authority_user"))
	private User user;

	@Column(name = "username")
	private String username;

	@Column(name = "semester_id", nullable=true)
	private Long semesterId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "semester_id", insertable = false, updatable = false, nullable=true, foreignKey = @ForeignKey(name="fk_authority_semester"))
	private Semester semester;

	@Column(name = "course_id", nullable=true)
	private Long courseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", insertable = false, updatable = false, nullable=true, foreignKey = @ForeignKey(name="fk_authority_course"))
	private Course course;
	
	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public Long getId() {
		return id;
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

	public Long getSemesterId() {
		return semesterId;
	}

	public void setSemesterId(Long semesterId) {
		this.semesterId = semesterId;
	}

	public Semester getSemester() {
		return semester;
	}

	public void setSemester(Semester semester) {
		this.semester = semester;
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

	public void setId(Long id) {
		this.id = id;
	}

}
