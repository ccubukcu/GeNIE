package com.genie.model;

import java.sql.Blob;

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

import org.hibernate.envers.Audited;

import com.genie.enums.SampleImageFolder;
import com.genie.utils.DataFormatter;
import com.genie.utils.ResourceUtil;

/**
 * 
 * @author ccubukcu
 */
@Audited
@Entity
@Table(name = "badges")
public class Badge extends BaseEntity implements LoggableEntity {
	
	private static final long serialVersionUID = 8939049666743610411L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	@Column(name = "semester_course_id")
	private Long semesterCourseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "semester_course_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_badge_semestercourse"))
	private SemesterCourse semesterCourse;
	
	@Column(name = "image")
	@Lob
	private Blob image;

	@Column(name = "resource_image_name")
	private String resourceImageName;
	
	@Column(name = "resource_image_folder")
	private Integer resourceImageFolder;

	@Column(name = "description", length = 600)
	private String description;
	
	@Column(name = "name", length = 250)
	private String name;
	
	@Column(name = "using_uploaded_image")
	private boolean usingUploadedImage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Blob getImage() {
		return image;
	}

	public void setImage(Blob image) {
		this.image = image;
	}
	
	public String getResourceImageName() {
		return resourceImageName;
	}

	public void setResourceImageName(String resourceImageName) {
		this.resourceImageName = resourceImageName;
	}

	public int getResourceImageFolder() {
		return resourceImageFolder;
	}

	public void setResourceImageFolder(Integer resourceImageFolder) {
		this.resourceImageFolder = resourceImageFolder;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isUsingUploadedImage() {
		return usingUploadedImage;
	}

	public void setUsingUploadedImage(boolean usingUploadedImage) {
		this.usingUploadedImage = usingUploadedImage;
	}
	
	public String getEncodedImage() {
		if(usingUploadedImage) {
			if(image != null)
				return DataFormatter.getBase64EncodedImage(image);
			else return "";
		} else {
			if(resourceImageFolder != null && resourceImageName != null) {
				String path = SampleImageFolder.getSampleImageFolder(resourceImageFolder).getResourceFolderPath() + "/" + resourceImageName;
				return DataFormatter.getBase64EncodedImage(ResourceUtil.getFileFromResources(path));
			} else return "";
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Badge other = (Badge) obj;
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
