package com.genie.model;

import java.sql.Blob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.genie.utils.DataFormatter;
import com.genie.utils.LoggableEntity;
import com.genie.utils.ResourceUtil;

/**
 * @author ccubukcu
 * 
 */
@Entity
@Audited
@Table(name = "document")
public class Document extends BaseEntity implements LoggableEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long documentId;

	@Column(name = "filename")
	private String filename;

	@Column(name = "filetype")
	private String filetype;

	@Column(name = "filesize")
	private Long filesize;

	@Column(name = "upload_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date uploadDate;

	@Column(name = "document_type")
	private String documentType;

	@Lob
	@Column(name = "file_data")
	private Blob fileData;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creation_user", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_document_user"))
	private User uploadUser;

	public Document() {
	}

	public long getDocumentId() {
		return this.documentId;
	}

	public void setDocumentId(long documentId) {
		this.documentId = documentId;
	}

	public Date getUploadDate() {
		return this.uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getUploadDateString() {
		if (uploadDate == null)
			return "";
		else
			return DataFormatter.formatDate(uploadDate);
	}

	public User getUploadUser() {
		return uploadUser;
	}

	public void setUploadUser(User uploadUser) {
		this.uploadUser = uploadUser;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public Long getFilesize() {
		return filesize;
	}

	public void setFilesize(Long filesize) {
		this.filesize = filesize;
	}
	
	public Blob getFileData() {
		return fileData;
	}

	public void setFileData(Blob fileData) {
		this.fileData = fileData;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}
	
	public String getFilesizeString() {
		String sizeSuffix = ResourceUtil.getLabel("general.labels.KB");
		Float sizeSorted = filesize.floatValue() / 1024;
		
		if(filesize > 1024 * 1024) {
			sizeSuffix = ResourceUtil.getLabel("general.labels.MB");

			sizeSorted = filesize.floatValue() / (1024*1024);
		}
		
		
		
		
		return String.format("%.2f", sizeSorted) + " " + sizeSuffix;
	}
}
