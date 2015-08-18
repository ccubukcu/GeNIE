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
 * 
 * @author ccubukcu
 */
@Audited
@Entity
@Table(name = "assignment_submissions")
public class AssignmentSubmission extends BaseEntity implements LoggableEntity {
	
	private static final long serialVersionUID = 3024856729461367009L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "assignment_id")
	private Long assignmentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assignment_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_asgnsubmissions_assignments"))
	private Assignment assignment;
	
	@Column(name = "student_name")
	private String studentName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_name", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_asgnsubmissions_users"))
	private User student;
	
	@Column(name = "document_id")
	private Long documentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "document_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_asgnsubmissions_documents"))
	private Document document;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(Long assignmentId) {
		this.assignmentId = assignmentId;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
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
}
