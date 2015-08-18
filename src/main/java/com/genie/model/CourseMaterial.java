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
@Table(name = "course_materials")
public class CourseMaterial extends BaseEntity implements LoggableEntity {
	private static final long serialVersionUID = 7688876452996551378L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "course_plan_id")
	private Long coursePlanId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_plan_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_materials_plans"))
	private CoursePlan coursePlan;

	@Column(name = "notes", length = 255)
	private String notes;

	@Column(name = "document_id")
	private Long documentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "document_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_material_document"))
	private Document document;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Long getCoursePlanId() {
		return coursePlanId;
	}

	public void setCoursePlanId(Long coursePlanId) {
		this.coursePlanId = coursePlanId;
	}

	public CoursePlan getCoursePlan() {
		return coursePlan;
	}

	public void setCoursePlan(CoursePlan coursePlan) {
		this.coursePlan = coursePlan;
	}
}
