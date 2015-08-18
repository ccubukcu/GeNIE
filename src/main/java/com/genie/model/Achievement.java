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

import com.genie.enums.AchievementType;
import com.genie.enums.ComparisonType;
import com.genie.enums.SampleImageFolder;
import com.genie.utils.DataFormatter;
import com.genie.utils.ResourceUtil;

/**
 * @author ccubukcu
 *
 */

@Entity
@Audited
@Table(name = "achievements")
public class Achievement extends BaseEntity implements LoggableEntity {
	
	private static final long serialVersionUID = 7455550311224077162L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	@Column(name = "semester_course_id")
	private Long semesterCourseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "semester_course_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_achievements_semestercourse"))
	private SemesterCourse semesterCourse;
	
	@Column(name = "image")
	@Lob
	private Blob image;

	@Column(name = "resource_image_name")
	private String resourceImageName;
	
	@Column(name = "resource_image_folder")
	private Integer resourceImageFolder;

	@Column(name = "description", length = 1250)
	private String description;
	
	@Column(name = "name", length = 250)
	private String name;
	
	@Column(name = "using_uploaded_image")
	private boolean usingUploadedImage;
	
	// Score, attendance, early submission etc.
	@Column(name = "achievement_type")
	private Integer achievementType;
	
	@Column(name = "target_grading_criteria")
	private Integer targetGradingCriteria;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "target_grade_criteria_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_achievements_criterias"))
	private GradeCriteria targetGradeCriteria;
	
	@Column(name = "target_grade_criteria_id")	
	private Long targetGradeCriteriaId;

	// 90 points, 5 days earlier etc.
	@Column(name = "threshold_value")
	private Integer thresholdValue;
	
	// Number of times to achieve that. 90 points 3 times, 5 days earlier once etc.
	@Column(name = "target_count")
	private Integer targetCount;
	
	// Earlier, later, smaller, equal etc.
	@Column(name = "comparison_type")
	private Integer comparisonType;
	
	@Column(name = "point_reward")
	private Long pointReward;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "badge_reward_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_achievements_badges"))
	private Badge badgeReward;
	
	@Column(name = "badge_reward_id")	
	private Long badgeRewardId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reward_grade_criteria_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_achievements_criterias"))
	private GradeCriteria rewardGradeCriteria;
	
	@Column(name = "reward_grade_criteria_id")	
	private Long rewardGradeCriteriaId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reward_assignment_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name="fk_achievements_assignments"))
	private Assignment rewardAssignment;
	
	@Column(name = "reward_assignment_id")	
	private Long rewardAssignmentId;

	@Column(name = "grade_reward")
	private Integer gradeReward;

	@Column(name = "week_reward")
	private Integer weekReward;

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

	public Integer getResourceImageFolder() {
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

	public Integer getAchievementType() {
		return achievementType;
	}

	public void setAchievementType(Integer achievementType) {
		this.achievementType = achievementType;
	}

	public Integer getThresholdValue() {
		return thresholdValue;
	}

	public void setThresholdValue(Integer thresholdValue) {
		this.thresholdValue = thresholdValue;
	}

	public Integer getTargetCount() {
		return targetCount;
	}

	public void setTargetCount(Integer targetCount) {
		this.targetCount = targetCount;
	}

	public Integer getComparisonType() {
		return comparisonType;
	}

	public void setComparisonType(Integer comparisonType) {
		this.comparisonType = comparisonType;
	}

	public Long getPointReward() {
		return pointReward;
	}

	public void setPointReward(Long pointReward) {
		this.pointReward = pointReward;
	}

	public Badge getBadgeReward() {
		return badgeReward;
	}

	public void setBadgeReward(Badge badgeReward) {
		this.badgeReward = badgeReward;
	}

	public Long getBadgeRewardId() {
		return badgeRewardId;
	}

	public void setBadgeRewardId(Long badgeRewardId) {
		this.badgeRewardId = badgeRewardId;
	}
	
	public GradeCriteria getTargetGradeCriteria() {
		return targetGradeCriteria;
	}

	public void setTargetGradeCriteria(GradeCriteria targetGradeCriteria) {
		this.targetGradeCriteria = targetGradeCriteria;
	}

	public Long getTargetGradeCriteriaId() {
		return targetGradeCriteriaId;
	}

	public void setTargetGradeCriteriaId(Long targetGradeCriteriaId) {
		this.targetGradeCriteriaId = targetGradeCriteriaId;
	}

	public GradeCriteria getRewardGradeCriteria() {
		return rewardGradeCriteria;
	}

	public void setRewardGradeCriteria(GradeCriteria rewardGradeCriteria) {
		this.rewardGradeCriteria = rewardGradeCriteria;
	}

	public Long getRewardGradeCriteriaId() {
		return rewardGradeCriteriaId;
	}

	public void setRewardGradeCriteriaId(Long rewardGradeCriteriaId) {
		this.rewardGradeCriteriaId = rewardGradeCriteriaId;
	}

	public Integer getGradeReward() {
		return gradeReward;
	}

	public void setGradeReward(Integer gradeReward) {
		this.gradeReward = gradeReward;
	}
	
	public Integer getTargetGradingCriteria() {
		return targetGradingCriteria;
	}

	public void setTargetGradingCriteria(Integer targetGradingCriteria) {
		this.targetGradingCriteria = targetGradingCriteria;
	}

	public Assignment getRewardAssignment() {
		return rewardAssignment;
	}

	public void setRewardAssignment(Assignment rewardAssignment) {
		this.rewardAssignment = rewardAssignment;
	}

	public Long getRewardAssignmentId() {
		return rewardAssignmentId;
	}

	public void setRewardAssignmentId(Long rewardAssignmentId) {
		this.rewardAssignmentId = rewardAssignmentId;
	}

	public Integer getWeekReward() {
		return weekReward;
	}

	public void setWeekReward(Integer weekReward) {
		this.weekReward = weekReward;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAchievementTypeString() {
		if(achievementType == null) {
			return "";
		} else {
			return AchievementType.getLabel(achievementType);
		}
		
	}
	
	public String getComparisonTypeString() {
		if(comparisonType == null) {
			return "";
		} else {
			return ComparisonType.getLabel(comparisonType);
		}
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
        final Achievement other = (Achievement) obj;
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
