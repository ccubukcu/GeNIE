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
@Table(name = "course_plans")
public class CoursePlan extends BaseEntity implements LoggableEntity {
	private static final long serialVersionUID = 7622477932599857881L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "course_plan_title", length = 255)
	private String coursePlanTitle;

	@Column(name = "semester_course_id")
	private Long semesterCourseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "semester_course_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_plan_semester_course"))
	private SemesterCourse semesterCourse;

	@Column(name = "plan_order")
	private Integer planOrder;

	@Column(name = "plan_length")
	private Integer planLength;
	
	@Column(name = "description", length=1250)
	private String description;
	
	@OneToMany(mappedBy = "coursePlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Where(clause="active=1")
	private List<CourseMaterial> materials;

	@OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Where(clause="active=1")
	private List<Assignment> assignments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCoursePlanTitle() {
		return coursePlanTitle;
	}

	public void setCoursePlanTitle(String coursePlanTitle) {
		this.coursePlanTitle = coursePlanTitle;
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

	public Integer getPlanOrder() {
		return planOrder;
	}

	public void setPlanOrder(Integer planOrder) {
		this.planOrder = planOrder;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<CourseMaterial> getMaterials() {
		return materials;
	}

	public void setMaterials(List<CourseMaterial> materials) {
		this.materials = materials;
	}

	public Integer getPlanLength() {
		return planLength;
	}

	public void setPlanLength(Integer planLength) {
		this.planLength = planLength;
	}

	public List<Assignment> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<Assignment> assignments) {
		this.assignments = assignments;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CoursePlan other = (CoursePlan) obj;
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
