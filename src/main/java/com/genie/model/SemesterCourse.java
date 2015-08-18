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

@Entity
@Audited
@Table(name = "semester_courses")
public class SemesterCourse extends BaseEntity implements LoggableEntity {
	private static final long serialVersionUID = -4216545952756537643L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "semester_id")
	private Long semesterId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "semester_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_xref_semesters"))
	private Semester semester;

	@Column(name = "course_id")
	private Long courseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_xref_courses"))
	private Course course;

	@OneToMany(mappedBy = "semesterCourse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Where(clause="active=1")
	private List<GradeCriteria> gradeCriteria;
	
	@Column(name = "enrollment_key")
	private String enrollmentKey;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<GradeCriteria> getGradeCriteria() {
		return gradeCriteria;
	}

	public void setGradeCriteria(List<GradeCriteria> gradeCriteria) {
		this.gradeCriteria = gradeCriteria;
	}

	public String getEnrollmentKey() {
		return enrollmentKey;
	}

	public void setEnrollmentKey(String enrollmentKey) {
		this.enrollmentKey = enrollmentKey;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SemesterCourse other = (SemesterCourse) obj;
        if(id == other.id) {
        	return true;
        }
        if(id == null || other.id == null) {
        	return false;
        }
        if(id.intValue() > 0 && other.id.intValue() > 0) {
        	if(!id.equals(other.id)) {
            	return false;
            }
        }
        
        return true;
	}
}
