package com.genie.model;

import java.util.Date;
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

import com.genie.utils.DataFormatter;

/**
 * @author ccubukcu
 *
 */

@Entity
@Audited
@Table(name = "semesters")
public class Semester extends BaseEntity implements LoggableEntity {
	private static final long serialVersionUID = -7952990665551734033L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "semester_identifier", length = 127)
	private String semesterIdentifier;
	
	@Column(name = "semester_order")
	private Integer semesterOrder;
	
	@Column(name = "school_year_id")
	private Long schoolYearId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "school_year_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_semester_year"))
	private SchoolYear schoolYear;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "end_date")
	private Date endDate;

	@Column(name = "notes", length = 1250)
	private String notes;
	
	@OneToMany(mappedBy = "semester", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Where(clause="active=1")
	private List<SemesterCourse> semesterCourses;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSemesterIdentifier() {
		return semesterIdentifier;
	}

	public void setSemesterIdentifier(String semesterIdentifier) {
		this.semesterIdentifier = semesterIdentifier;
	}

	public SchoolYear getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(SchoolYear schoolYear) {
		this.schoolYear = schoolYear;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getStartDateString() {
		if (startDate == null)
			return "";
		else
			return DataFormatter.formatDate(startDate);
	}

	public String getEndDateString() {
		if (endDate == null)
			return "";
		else
			return DataFormatter.formatDate(endDate);
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public List<SemesterCourse> getSemesterCourses() {
		return semesterCourses;
	}

	public void setSemesterCourses(List<SemesterCourse> semesterCourses) {
		this.semesterCourses = semesterCourses;
	}

	public Integer getSemesterOrder() {
		return semesterOrder;
	}

	public void setSemesterOrder(Integer semesterOrder) {
		this.semesterOrder = semesterOrder;
	}

	public Long getSchoolYearId() {
		return schoolYearId;
	}

	public void setSchoolYearId(Long schoolYearId) {
		this.schoolYearId = schoolYearId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Semester other = (Semester) obj;
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
