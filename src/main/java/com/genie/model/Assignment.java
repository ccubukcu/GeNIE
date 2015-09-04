package com.genie.model;

import java.util.Date;

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

import com.genie.utils.DataFormatter;
import com.genie.utils.LoggableEntity;

/**
 * @author ccubukcu
 *
 */

@Entity
@Audited
@Table(name = "assignments")
public class Assignment extends BaseEntity implements LoggableEntity {
	private static final long serialVersionUID = -5621665724587954202L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "description", length = 6000)
	private String description;

	@Column(name = "due_date")
	private Date dueDate;

	@Column(name = "plan_id")
	private Long planId;
	
	@Column(name = "weight")
	private Long weight;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "plan_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_assignment_week"))
	private CoursePlan plan;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getDueDateString() {
		if (dueDate == null)
			return "";
		else
			return DataFormatter.formatDate(dueDate);
	}

	public Long getPlanId() {
		return planId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
	}

	public CoursePlan getPlan() {
		return plan;
	}

	public void setPlan(CoursePlan plan) {
		this.plan = plan;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Assignment other = (Assignment) obj;
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
