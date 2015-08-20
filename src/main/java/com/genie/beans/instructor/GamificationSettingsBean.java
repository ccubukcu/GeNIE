package com.genie.beans.instructor;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.io.IOUtils;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;

import com.genie.beans.BaseBean;
import com.genie.enums.AchievementType;
import com.genie.enums.ComparisonType;
import com.genie.enums.GradingCriteria;
import com.genie.enums.LeaderboardVisibility;
import com.genie.enums.SampleImageFolder;
import com.genie.model.Achievement;
import com.genie.model.Assignment;
import com.genie.model.Badge;
import com.genie.model.GamificationSettings;
import com.genie.model.GradeCriteria;
import com.genie.model.LeaderboardSettings;
import com.genie.model.SemesterCourse;
import com.genie.pojo.ResourceImageWrapper;
import com.genie.services.CoursePlanService;
import com.genie.services.CourseService;
import com.genie.services.GamificationService;
import com.genie.utils.DataFormatter;
import com.genie.utils.PortalConstants;
import com.genie.utils.ResourceUtil;

/**
 * @author ccubukcu
 *
 */
@ManagedBean
@ViewScoped
public class GamificationSettingsBean extends BaseBean {
	private static final long serialVersionUID = -4383191321564157521L;

	private List<SemesterCourse> courses;
	private SemesterCourse selectedCourse;

	private GamificationSettings generalSettings;
	private LeaderboardSettings leaderboardSettings;

	private List<Badge> badges;
	private Badge selectedBadge;
	
	private List<Achievement> achievements;
	private Achievement selectedAchievement;
	private List<GradeCriteria> achievementGradeCriteria;
	private GradeCriteria selectedAchievementGradeCriteria;
	private List<GradeCriteria> allGradeCriteria;
	private GradeCriteria selectedRewardGradeCriteria;
	private Badge selectedRewardBadge;
	private List<Assignment> assignments;
	private Assignment selectedAssignmentReward;
	
	private int selectedTab;
	private Integer selectedWeek;
	private boolean courseSelected;
	private boolean updatingBadge;
	private boolean updatingAchievement;
	private boolean achievementTypeSelected;

	private List<SelectItem> visibilityItems;
	private List<SelectItem> resourceFolderItems;
	private List<SelectItem> achievementTypeItems;
	private List<SelectItem> comparisonTypeItems;
	private List<SelectItem> achievementGradingCriteriaItems;
	private List<SelectItem> weeks;

	private Blob uploadedImage;
	private int selectedResourceFolder;
	private ResourceImageWrapper selectedResourceImage;
	private List<ResourceImageWrapper> resourceImages;
	
	@PostConstruct
	public void initBean() {
		refreshCourses();
		selectedTab = 0;
		
		courseSelected = false;
		
		visibilityItems = LeaderboardVisibility.getAsSelectItems();
		resourceFolderItems = SampleImageFolder.getAsSelectItems();
		achievementTypeItems = AchievementType.getAsSelectItems();
		comparisonTypeItems = ComparisonType.getAsSelectItems();
		achievementGradingCriteriaItems = GradingCriteria.getAsSelectItemsForAchievements();
		
		prepareAddAchievementDialog();
		prepareAddBadgeDialog();
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~ Tab 1 (General) Operations ~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public void addOrUpdateGeneralSettings() {
		try {
			if(generalSettings.getId() != null) {
				GamificationService.updateGeneralSettings(generalSettings);
				updateSuccessful();
			} else {
				generalSettings.setSemesterCourseId(selectedCourse.getId());
				GamificationService.saveGeneralSettings(generalSettings);
				saveSuccessful();
			}
		} catch (Exception e) {
			e.printStackTrace();
			saveOrUpdateFailed();
		}
	}
	

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~ Tab 2 (Leaderboards) Operations ~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public void addOrUpdateLeaderboardSettings() {
		try {
			if(leaderboardSettings.getVisibility() == LeaderboardVisibility.ALL_VISIBLE.getIndex()) {
				leaderboardSettings.setBottomStudents(0);
				leaderboardSettings.setTopStudents(0);
			} else if (leaderboardSettings.getVisibility() == LeaderboardVisibility.TOP.getIndex()) {
				leaderboardSettings.setBottomStudents(0);
			}
			
			if(leaderboardSettings.getId() != null) {
				GamificationService.updateLeaderboardSettings(leaderboardSettings);
				updateSuccessful();
			} else {
				leaderboardSettings.setSemesterCourseId(selectedCourse.getId());
				GamificationService.saveLeaderboardSettings(leaderboardSettings);
				saveSuccessful();
			}
		} catch (Exception e) {
			e.printStackTrace();
			saveOrUpdateFailed();
		}
	}
	
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~ Tab 3 (Badges) Operations ~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public void addOrUpdateBadge() {
		try {
			boolean imageSelected = false;
			selectedBadge.setSemesterCourseId(selectedCourse.getId());
			
			if(selectedResourceImage != null) {
				selectedBadge.setResourceImageFolder(selectedResourceFolder);
				selectedBadge.setResourceImageName(selectedResourceImage.getFilename());
				imageSelected = true;
			}
			if(uploadedImage != null) {
				selectedBadge.setImage(uploadedImage);
				imageSelected = true;
			}
			
			if(!imageSelected) {
				validationFailed("badgeManagementForm:badgeImgSrc", "error.selectBadgeImage");
				return;
			}
			
			if(updatingBadge) {
				GamificationService.updateBadge(selectedBadge);
				updateSuccessful();
			} else {
				GamificationService.addBadge(selectedBadge);
				saveSuccessful();
			}
			
			selectedBadge = null;
			updatingBadge = false;
			refreshBadges();
		} catch (Exception e) {
			e.printStackTrace();
			saveOrUpdateFailed();
		}
	}
	
	public void deleteBadge() {
		try {
			GamificationService.deleteBadge(selectedBadge);
			refreshBadges();
			onBadgeUnselected();
			deleteSucessful();
		} catch (Exception e) {
			e.printStackTrace();
			operationFailed();
		}
	}
	
	public void handleBadgeImageUpload(FileUploadEvent event) {
		try {
			byte[] data = IOUtils.toByteArray(event.getFile().getInputstream());
			uploadedImage = new SerialBlob(data);
		} catch (Exception e) {
			saveOrUpdateFailed();
		}
	}
	
	public String getPreviewBadgeImage() {
		if(selectedBadge != null) {
			if(selectedBadge.isUsingUploadedImage()) {
				if(uploadedImage != null)
					return DataFormatter.getBase64EncodedImage(uploadedImage);
			} else {
				if(selectedResourceImage != null)
					return selectedResourceImage.getEncodedImage();
			}
		}
		return "";
	}
	
	public void onBadgeResourceFolderSelect() {
		if(selectedResourceFolder > 0) {
			resourceImages = SampleImageFolder.getImagesByIndex(selectedResourceFolder);
			
			if(selectedBadge != null && selectedBadge.getId() != null 
					&& selectedResourceFolder == selectedBadge.getResourceImageFolder() && resourceImages != null) {
				for (ResourceImageWrapper riw : resourceImages) {
					if(riw.getFilename().equals(selectedBadge.getResourceImageName())) {
						selectedResourceImage = riw;
					}
				}
			}
		}
	}
	
	public void prepareAddBadgeDialog() {
		selectedBadge = new Badge();
		selectedResourceImage = null;
		selectedResourceFolder = 0;
		resourceImages = null;
		uploadedImage = null;
		
		updatingBadge = false;
	}
	
	public void prepareUpdateBadgeDialog() {
		if(selectedBadge.isUsingUploadedImage()) {
			uploadedImage = selectedBadge.getImage();
		} else {
			selectedResourceFolder = selectedBadge.getResourceImageFolder();
			onBadgeResourceFolderSelect();
		}
		
		updatingBadge = true;
	}
	
	public void refreshBadges() {
		badges = GamificationService.getAllBadgesForSemesterCourse(selectedCourse.getId());
		selectedBadge = null;
		updatingBadge = false;
	}
	
	public void onBadgeSelected() {
		updatingBadge = true;
	}
	
	public void onBadgeUnselected() {
		updatingBadge = false;
	}
	
	public String getBadgeDialogHeader() {
		return updatingBadge ? ResourceUtil.getLabel("gamificationSettings.label.updateBadgeHeader") : ResourceUtil.getLabel("gamificationSettings.label.addBadgeHeader");
	}
	
	public String getBadgeDialogButtonLabel() {
		return updatingBadge ? ResourceUtil.getLabel("general.labels.edit") : ResourceUtil.getLabel("general.labels.add");
	}
	
	public String getBadgeDialogButtonClass() {
		String baseClass = "submit-input grad-btn ln-tr";
		if(updatingBadge) {
			baseClass += " edit-button";
		} else {
			baseClass += " add-button";
		}
		
		return baseClass;
	}
	
	public boolean isBadgeButtonsDisabled() {
		if(!courseSelected) {
			return true;
		} else if(selectedBadge == null || (selectedBadge != null && selectedBadge.getId() == null)) {
			return true;
		}
		return false;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~ Tab 4 (Achievements) Operations ~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public void addOrUpdateAchievement() {
		try {
			boolean imageSelected = false;
			selectedAchievement.setSemesterCourseId(selectedCourse.getId());
			
			if(selectedResourceImage != null) {
				selectedAchievement.setResourceImageFolder(selectedResourceFolder);
				selectedAchievement.setResourceImageName(selectedResourceImage.getFilename());
				imageSelected = true;
			}
			
			if(uploadedImage != null) {
				selectedAchievement.setImage(uploadedImage);
				imageSelected = true;
			}
			
			if(!imageSelected) {
				validationFailed("achievementManagementForm:achievementImgSrc", "error.selectAchievementImage");
				return;
			}
			
			selectedAchievement.setTargetGradeCriteriaId(selectedAchievementGradeCriteria == null ? null : selectedAchievementGradeCriteria.getId());
			selectedAchievement.setRewardGradeCriteriaId(selectedRewardGradeCriteria == null ? null : selectedRewardGradeCriteria.getId());
			selectedAchievement.setBadgeRewardId(selectedRewardBadge == null ? null : selectedRewardBadge.getId());
			selectedAchievement.setRewardAssignmentId(selectedAssignmentReward == null ? null : selectedAssignmentReward.getId());
			
			if(updatingAchievement) {
				GamificationService.updateAchievement(selectedAchievement);
				updateSuccessful();
			} else {
				GamificationService.addAchievement(selectedAchievement);
				saveSuccessful();
			}
			
			refreshAchievements();
		} catch (Exception e) {
			e.printStackTrace();
			saveOrUpdateFailed();
		}
	}
	
	public void deleteAchievement() {
		try {
			GamificationService.deleteAchievement(selectedAchievement);
			refreshAchievements();
			onAchievementUnselected();
			deleteSucessful();
		} catch (Exception e) {
			e.printStackTrace();
			operationFailed();
		}
	}
	
	public void handleAchievementImageUpload(FileUploadEvent event) {
		try {
			byte[] data = IOUtils.toByteArray(event.getFile().getInputstream());
			uploadedImage = new SerialBlob(data);
		} catch (Exception e) {
			saveOrUpdateFailed();
		}
	}
	
	public String getPreviewAchievementImage() {
		if(selectedAchievement != null) {
			if(selectedAchievement.isUsingUploadedImage()) {
				if(uploadedImage != null)
					return DataFormatter.getBase64EncodedImage(uploadedImage);
			} else {
				if(selectedResourceImage != null)
					return selectedResourceImage.getEncodedImage();
			}
		}
		return "";
	}
	
	public void onAchievementResourceFolderSelect() {
		if(selectedResourceFolder > 0) {
			resourceImages = SampleImageFolder.getImagesByIndex(selectedResourceFolder);
			
			if(selectedAchievement != null && selectedAchievement.getId() != null 
					&& selectedResourceFolder == selectedAchievement.getResourceImageFolder() && resourceImages != null) {
				for (ResourceImageWrapper riw : resourceImages) {
					if(riw.getFilename().equals(selectedAchievement.getResourceImageName())) {
						selectedResourceImage = riw;
					}
				}
			}
		}
	}
	
	public void prepareAddAchievementDialog() {
		selectedAchievement = new Achievement();
		selectedResourceImage = null;
		selectedResourceFolder = 0;
		resourceImages = null;
		uploadedImage = null;
		selectedAchievementGradeCriteria = null;
		selectedRewardGradeCriteria = null;
		
		updatingAchievement = false;
		achievementTypeSelected = false;

		if(selectedCourse != null) {
			refreshAchievementGradeCriteria();
			refreshBadges();
			refreshAssignments();
			refreshWeeks();
		}
	}
	
	public void prepareUpdateAchievementDialog() {
		if(selectedCourse != null) {
			if(selectedAchievement.isUsingUploadedImage()) {
				uploadedImage = selectedAchievement.getImage();
			} else {
				selectedResourceFolder = selectedAchievement.getResourceImageFolder();
				onAchievementResourceFolderSelect();
			}
			
			selectedAchievementGradeCriteria = selectedAchievement.getTargetGradeCriteria();
			onAchievementGradingCriteriaSelect();
			selectedRewardGradeCriteria = selectedAchievement.getRewardGradeCriteria();
			updatingAchievement = true;
	
			badges = GamificationService.getAllBadgesForSemesterCourse(selectedCourse.getId());
			selectedRewardBadge = selectedAchievement.getBadgeReward();
			onAchievementTypeSelect();
			
			refreshAchievementGradeCriteria();
			refreshBadges();
			refreshAssignments();
			refreshWeeks();
		}
	}
	
	public void refreshAchievements() {
		achievements = GamificationService.getAllAchievementsForSemesterCourseWithEverything(selectedCourse.getId());
		selectedAchievement = null;
		updatingAchievement = false;
	}
	
	public void onAchievementTypeSelect() {
		achievementTypeSelected = true;
	}
	
	public void onAchievementGradingCriteriaSelect() {
		refreshAchievementGradeCriteria();	
	}
	
	public void refreshAchievementGradeCriteria() {
		if(selectedCourse != null) {
			if(selectedAchievement != null && selectedAchievement.getTargetGradingCriteria() != null) {
			achievementGradeCriteria = CoursePlanService.getGradeCriteriaByTypeAndSemesterCourse(
					GradingCriteria.getGradingCriteria(selectedAchievement.getTargetGradingCriteria()), selectedCourse.getId());
			}
			
			allGradeCriteria = CoursePlanService.getGradeCriteriaBySemesterCourse(selectedCourse.getId());
		}
	}
	
	public void refreshAssignments() {
		assignments = CoursePlanService.getAllAssignmentsBySemesterCourse(selectedCourse.getId());
	}
	
	public void refreshWeeks() {
		weeks = new ArrayList<SelectItem>();
		
		int weekCount = CoursePlanService.getWeekCountInCourseSemester(selectedCourse.getId());
		
		for(int i = 1; i < weekCount + 1; i++) {
			String prefix = ResourceUtil.getLabel("week.label.name");
			String label = prefix + " " + Integer.toString(i);
			
			SelectItem weekItem = new SelectItem(i, label);
			weeks.add(weekItem);
		}
	}
	
	public boolean isCriteriaPanelRendered() {
		return achievementTypeSelected;
	}
	
	public boolean isThresholdRendered() {
		if(!isCriteriaPanelRendered())
			return false;
		if(selectedAchievement.getAchievementType() == null || selectedAchievement.getAchievementType() == AchievementType.ATTENDANCE_CONSECUTIVE.getIndex()
				|| selectedAchievement.getAchievementType() == AchievementType.ATTENDANCE_OVERALL.getIndex())
			return false;
		return true;
	}
	
	public boolean isComparisonTypeRendered() {
		if(!isThresholdRendered())
			return false;
		if(selectedAchievement.getAchievementType() == AchievementType.SUBMISSION_EARLY.getIndex())
			return false;
		return true;
	}
	
	public boolean isGradingCriteriaRendered() {
		if(!isCriteriaPanelRendered())
			return false;
		if(selectedAchievement.getAchievementType() != null && selectedAchievement.getAchievementType() != AchievementType.GRADE.getIndex())
			return false;
		return true;
	}
	
	public boolean isAttendanceRewardSelected() {
		return selectedRewardGradeCriteria != null && selectedRewardGradeCriteria.getGradingCriteria() == GradingCriteria.ATTENDANCE.getIndex();
	}
	
	public boolean isAssignmentRewardSelected() {
		return selectedRewardGradeCriteria != null && selectedRewardGradeCriteria.getGradingCriteria() == GradingCriteria.ASSIGNMENT.getIndex();
	}
	
	public boolean isGradeCriteriaDisabled() {
		if(!isGradingCriteriaRendered())
			return true;
		if(selectedAchievement.getTargetGradingCriteria() == null || selectedAchievement.getTargetGradingCriteria() < 0)
			return true;
		return false;
	}
	
	public void onAchievementSelected() {
		updatingAchievement = true;
	}
	
	public void onAchievementUnselected() {
		updatingAchievement = false;
	}
	
	public String getAchievementDialogHeader() {
		return updatingAchievement ? ResourceUtil.getLabel("gamificationSettings.label.updateAchievementHeader") : ResourceUtil.getLabel("gamificationSettings.label.addAchievementHeader");
	}
	
	public String getAchievementDialogButtonLabel() {
		return updatingAchievement ? ResourceUtil.getLabel("general.labels.edit") : ResourceUtil.getLabel("general.labels.add");
	}
	
	public String getAchievementDialogButtonClass() {
		String baseClass = "submit-input grad-btn ln-tr";
		if(updatingAchievement) {
			baseClass += " edit-button";
		} else {
			baseClass += " add-button";
		}
		
		return baseClass;
	}
	
	public boolean isAchievementButtonsDisabled() {
		if(!courseSelected) {
			return true;
		} else if(selectedAchievement == null || (selectedAchievement != null && selectedAchievement.getId() == null)) {
			return true;
		}
		return false;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~ Other Operations ~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public void refreshCourses() {
		courses = CourseService.getAllSemesterCoursesForInstructor();
	}
	
	public void onCourseSelect() {
		courseSelected = true;
		
		if(selectedCourse != null) {
			generalSettings = GamificationService.getGeneralSettingsForSemesterCourse(selectedCourse.getId());
			
			if(generalSettings == null) {
				generalSettings = new GamificationSettings();
				generalSettings.setPointsName(ResourceUtil.getLabel(PortalConstants.DEFAULT_POINTS_NAME_KEY));
				generalSettings.setMaxConvertablePoints(PortalConstants.DEFAULT_MAX_CONVERTABLE_POINTS);
			}
			
			leaderboardSettings = GamificationService.getLeaderboardSettingsForSemesterCourse(selectedCourse.getId());
			
			if(leaderboardSettings == null) {
				leaderboardSettings = new LeaderboardSettings();
			}
			
			refreshBadges();
			refreshAchievements();
		}
	}
	
	public void onTabChange(TabChangeEvent event){
		TabView tabView = (TabView) event.getComponent();
        selectedTab = tabView.getChildren().indexOf(event.getTab());
    }
	
	public boolean isTopPercentileRendered() {
		if(leaderboardSettings != null && leaderboardSettings.getVisibility() != LeaderboardVisibility.ALL_VISIBLE.getIndex()) {
			return true;
		}
		return false;
	}
	
	public boolean isBottomPercentileRendered() {
		if(leaderboardSettings != null && leaderboardSettings.getVisibility() == LeaderboardVisibility.TOP_AND_BOTTOM.getIndex()) {
			return true;
		}
		return false;
	}

	public List<SemesterCourse> getCourses() {
		return courses;
	}

	public void setCourses(List<SemesterCourse> courses) {
		this.courses = courses;
	}

	public SemesterCourse getSelectedCourse() {
		return selectedCourse;
	}

	public void setSelectedCourse(SemesterCourse selectedCourse) {
		this.selectedCourse = selectedCourse;
	}

	public GamificationSettings getGeneralSettings() {
		return generalSettings;
	}

	public void setGeneralSettings(GamificationSettings generalSettings) {
		this.generalSettings = generalSettings;
	}

	public int getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(int selectedTab) {
		this.selectedTab = selectedTab;
	}

	public boolean isCourseSelected() {
		return courseSelected;
	}

	public void setCourseSelected(boolean courseSelected) {
		this.courseSelected = courseSelected;
	}

	public LeaderboardSettings getLeaderboardSettings() {
		return leaderboardSettings;
	}

	public void setLeaderboardSettings(LeaderboardSettings leaderboardSettings) {
		this.leaderboardSettings = leaderboardSettings;
	}

	public List<SelectItem> getVisibilityItems() {
		return visibilityItems;
	}

	public void setVisibilityItems(List<SelectItem> visibilityItems) {
		this.visibilityItems = visibilityItems;
	}

	public List<Badge> getBadges() {
		return badges;
	}

	public void setBadges(List<Badge> badges) {
		this.badges = badges;
	}

	public Badge getSelectedBadge() {
		return selectedBadge;
	}

	public void setSelectedBadge(Badge selectedBadge) {
		this.selectedBadge = selectedBadge;
	}

	public Blob getUploadedImage() {
		return uploadedImage;
	}

	public void setUploadedImage(Blob uploadedImage) {
		this.uploadedImage = uploadedImage;
	}

	public boolean isUpdatingBadge() {
		return updatingBadge;
	}

	public void setUpdatingBadge(boolean updatingBadge) {
		this.updatingBadge = updatingBadge;
	}

	public int getSelectedResourceFolder() {
		return selectedResourceFolder;
	}

	public void setSelectedResourceFolder(int selectedResourceFolder) {
		this.selectedResourceFolder = selectedResourceFolder;
	}

	public List<SelectItem> getResourceFolderItems() {
		return resourceFolderItems;
	}

	public void setResourceFolderItems(List<SelectItem> resourceFolderItems) {
		this.resourceFolderItems = resourceFolderItems;
	}

	public ResourceImageWrapper getSelectedResourceImage() {
		return selectedResourceImage;
	}

	public void setSelectedResourceImage(ResourceImageWrapper selectedResourceImage) {
		this.selectedResourceImage = selectedResourceImage;
	}

	public List<ResourceImageWrapper> getResourceImages() {
		return resourceImages;
	}

	public void setResourceImages(List<ResourceImageWrapper> resourceImages) {
		this.resourceImages = resourceImages;
	}

	public List<Achievement> getAchievements() {
		return achievements;
	}

	public void setAchievements(List<Achievement> achievements) {
		this.achievements = achievements;
	}

	public Achievement getSelectedAchievement() {
		return selectedAchievement;
	}

	public void setSelectedAchievement(Achievement selectedAchievement) {
		this.selectedAchievement = selectedAchievement;
	}

	public boolean isUpdatingAchievement() {
		return updatingAchievement;
	}

	public void setUpdatingAchievement(boolean updatingAchievement) {
		this.updatingAchievement = updatingAchievement;
	}

	public List<SelectItem> getAchievementTypeItems() {
		return achievementTypeItems;
	}

	public void setAchievementTypeItems(List<SelectItem> achievementTypeItems) {
		this.achievementTypeItems = achievementTypeItems;
	}

	public List<SelectItem> getComparisonTypeItems() {
		return comparisonTypeItems;
	}

	public void setComparisonTypeItems(List<SelectItem> comparisonTypeItems) {
		this.comparisonTypeItems = comparisonTypeItems;
	}

	public List<GradeCriteria> getAchievementGradeCriteria() {
		return achievementGradeCriteria;
	}

	public void setAchievementGradeCriteria(List<GradeCriteria> achievementGradeCriteria) {
		this.achievementGradeCriteria = achievementGradeCriteria;
	}

	public boolean isAchievementTypeSelected() {
		return achievementTypeSelected;
	}

	public void setAchievementTypeSelected(boolean achievementTypeSelected) {
		this.achievementTypeSelected = achievementTypeSelected;
	}

	public List<SelectItem> getAchievementGradingCriteriaItems() {
		return achievementGradingCriteriaItems;
	}

	public void setAchievementGradingCriteriaItems(List<SelectItem> achievementGradingCriteriaItems) {
		this.achievementGradingCriteriaItems = achievementGradingCriteriaItems;
	}

	public GradeCriteria getSelectedAchievementGradeCriteria() {
		return selectedAchievementGradeCriteria;
	}

	public void setSelectedAchievementGradeCriteria(GradeCriteria selectedAchievementGradeCriteria) {
		this.selectedAchievementGradeCriteria = selectedAchievementGradeCriteria;
	}

	public List<GradeCriteria> getAllGradeCriteria() {
		return allGradeCriteria;
	}

	public void setAllGradeCriteria(List<GradeCriteria> allGradeCriteria) {
		this.allGradeCriteria = allGradeCriteria;
	}

	public GradeCriteria getSelectedRewardGradeCriteria() {
		return selectedRewardGradeCriteria;
	}

	public void setSelectedRewardGradeCriteria(GradeCriteria selectedRewardGradeCriteria) {
		this.selectedRewardGradeCriteria = selectedRewardGradeCriteria;
	}

	public Badge getSelectedRewardBadge() {
		return selectedRewardBadge;
	}

	public void setSelectedRewardBadge(Badge selectedRewardBadge) {
		this.selectedRewardBadge = selectedRewardBadge;
	}

	public List<Assignment> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<Assignment> assignments) {
		this.assignments = assignments;
	}

	public Integer getSelectedWeek() {
		return selectedWeek;
	}

	public void setSelectedWeek(Integer selectedWeek) {
		this.selectedWeek = selectedWeek;
	}

	public List<SelectItem> getWeeks() {
		return weeks;
	}

	public void setWeeks(List<SelectItem> weeks) {
		this.weeks = weeks;
	}

	public Assignment getSelectedAssignmentReward() {
		return selectedAssignmentReward;
	}

	public void setSelectedAssignmentReward(Assignment selectedAssignmentReward) {
		this.selectedAssignmentReward = selectedAssignmentReward;
	}
}
