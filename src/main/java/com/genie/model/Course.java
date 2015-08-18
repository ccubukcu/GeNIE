package com.genie.model;

import java.util.List;

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

/**
 * @author ccubukcu
 *
 */

@Entity
@Audited
@Table(name = "courses")
public class Course extends BaseEntity implements LoggableEntity {
	private static final long serialVersionUID = 5145335285652517668L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "course_identifier", length = 255)
	private String courseIdentifier;

	@Column(name = "course_name", length = 255)
	private String courseName;

	@Column(name = "short_description", length = 600)
	private String shortDescription;

	@Column(name = "long_description", length = 5500)
	private String longDescription;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "instructor", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_courses_users"))
	private User instructorUser;

	@Column(name = "instructor")
	private String instructor;
	
	@OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
	@Where(clause="active=1")
	private List<SemesterCourse> semesterCourses;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCourseIdentifier() {
		return courseIdentifier;
	}

	public void setCourseIdentifier(String courseIdentifier) {
		this.courseIdentifier = courseIdentifier;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	
	public User getInstructorUser() {
		return instructorUser;
	}

	public void setInstructorUser(User instructorUser) {
		this.instructorUser = instructorUser;
	}

	public String getInstructor() {
		return instructor;
	}

	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Course other = (Course) obj;
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

	public List<SemesterCourse> getSemesterCourses() {
		return semesterCourses;
	}

	public void setSemesterCourses(List<SemesterCourse> semesterCourses) {
		this.semesterCourses = semesterCourses;
	}
}
