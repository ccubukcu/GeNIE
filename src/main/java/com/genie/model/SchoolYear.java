package com.genie.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@Table(name = "school_years")
public class SchoolYear extends BaseEntity implements LoggableEntity {
	private static final long serialVersionUID = -1183211332705230386L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@Column(name = "year_identifier", length = 127)
	private String yearIdentifier;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "end_date")
	private Date endDate;

	@Column(name = "notes", length = 1250)
	private String notes;
	
	@OneToMany(mappedBy = "schoolYear", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Where(clause="active=1")
	private List<Semester> semesters;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getYearIdentifier() {
		return yearIdentifier;
	}

	public void setYearIdentifier(String yearIdentifier) {
		this.yearIdentifier = yearIdentifier;
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
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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

	public List<Semester> getSemesters() {
		return semesters;
	}

	public void setSemesters(List<Semester> semesters) {
		this.semesters = semesters;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SchoolYear other = (SchoolYear) obj;
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