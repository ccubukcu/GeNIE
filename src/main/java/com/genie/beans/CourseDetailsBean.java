package com.genie.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.genie.model.Achievement;
import com.genie.model.AchievementProgress;
import com.genie.model.Assignment;
import com.genie.model.Badge;
import com.genie.model.CoursePlan;
import com.genie.model.Document;
import com.genie.model.GamificationSettings;
import com.genie.model.GradeCriteria;
import com.genie.model.LeaderboardSettings;
import com.genie.model.SemesterCourse;
import com.genie.model.StudentBadge;
import com.genie.model.StudentGamificationSettings;
import com.genie.model.User;
import com.genie.pojo.AssignmentGradeWrapper;
import com.genie.pojo.AttendanceDetailsWrapper;
import com.genie.pojo.AttendanceWrapper;
import com.genie.pojo.GradeColumn;
import com.genie.pojo.GradeWrapper;
import com.genie.pojo.LeaderboardWrapper;
import com.genie.pojo.StudentGradeRowWrapper;
import com.genie.services.CoursePlanService;
import com.genie.services.CourseService;
import com.genie.services.GamificationService;
import com.genie.services.SessionService;
import com.genie.services.UserService;
import com.genie.utils.DataFormatter;
import com.genie.utils.JsfUtil;
import com.genie.utils.ResourceUtil;

@ManagedBean
@ViewScoped
public class CourseDetailsBean extends BaseBean {
	private static final long serialVersionUID = -7447236755286730413L;

	private static String semCourseId;
	private Long semesterCourseId;
	
	private List<CoursePlan> coursePlans;
	private SemesterCourse semesterCourse;

	private GamificationSettings generalSettings;
	private LeaderboardSettings leaderboardSettings;
	private StudentGamificationSettings settings;
	
	private List<Achievement> achievements;
	private List<Achievement> filteredAchievements;
	private List<AchievementProgress> progressedAchievements;
	
	private List<Badge> badges;
	private List<Badge> filteredBadges;
	private List<StudentBadge> studentBadges;

	private List<LeaderboardWrapper> leaderboardStudents;
	private LeaderboardWrapper currentStudent;
	private List<User> otherStudents;
	
	private List<StudentGradeRowWrapper> gradeStudents;
	private List<AssignmentGradeWrapper> assignmentGrades;
	private List<AttendanceDetailsWrapper> attendanceDetails;
	private List<GradeColumn> otherGradeColumns;
	private List<GradeColumn> examGradeColumns;
	private List<GradeColumn> assignmentColumns;
	private List<GradeColumn> attendanceColumns;
	
	@PostConstruct
	public void initBean() {
		try {
			semesterCourseId = Long.parseLong(semCourseId);
		} catch (Exception e) {
			semesterCourseId = 0l;
		}
		
		coursePlans = CoursePlanService.getAllPlansBySemesterCourseIdWithMaterialDocuments(semesterCourseId);
		semesterCourse = CourseService.getByIdWithEverything(semesterCourseId);
		
		generalSettings = GamificationService.getGeneralSettingsForSemesterCourse(semesterCourseId);
		settings = GamificationService.getUserSettings(semesterCourseId);
		leaderboardSettings = GamificationService.getLeaderboardSettingsForSemesterCourse(semesterCourseId);

		if(settings == null) {
			settings = new StudentGamificationSettings();
		}
		
		if(generalSettings.isAchievementsEnabled() && settings.isAchievementsEnabled()) {
			refreshAchievements();
		}
		
		if(generalSettings.isBadgesEnabled() && settings.isBadgesEnabled()) {
			refreshBadges();
		}
		
		if(generalSettings.isLeaderboardsEnabled() && settings.isLeaderboardsEnabled()) {
			refreshLeaderboards();
		}
		
		refreshGrades();
		refreshOtherStudents();
	}
	
	public void refreshOtherStudents() {
		otherStudents = UserService.getAllBySemesterCourseExceptUser(semesterCourse);
	}
	
	public void refreshGrades() {
		List<GradeCriteria> criteria = CoursePlanService.getGradeCriteriaBySemesterCourse(semesterCourseId);
		
		User currentStudent = UserService.getByUsernameWithEverything(SessionService.getUsername()); 
		
		List<User> students = new ArrayList<User>();
		students.add(currentStudent);

		gradeStudents = CourseService.calculateStudentGrades(students, criteria, semesterCourseId, false, true);
		gradeStudents.addAll(CourseService.calculateStudentGrades(students, criteria, semesterCourseId, true, true));

		examGradeColumns = new ArrayList<GradeColumn>();
		if(gradeStudents != null && gradeStudents.size() > 0) {
			int i = 0;
			if(gradeStudents.get(0).getExamGrades() != null) {
				for (GradeWrapper gw : gradeStudents.get(0).getExamGrades()) {
					examGradeColumns.add(new GradeColumn(gw.getLabel(), i));
					i++;
				}
			}
		}
		
		otherGradeColumns = new ArrayList<GradeColumn>();
		if(gradeStudents != null && gradeStudents.size() > 0) {
			int i = 0;
			if(gradeStudents.get(0).getOtherGrades() != null) {
				for (GradeWrapper gw : gradeStudents.get(0).getOtherGrades()) {
					otherGradeColumns.add(new GradeColumn(gw.getLabel(), i));
					i++;
				}
			}
		}
		
		attendanceDetails = CoursePlanService.getAttendanceDetails(currentStudent, semesterCourseId);
		attendanceColumns = new ArrayList<GradeColumn>();

		int i = 0;
		for (AttendanceWrapper adw : attendanceDetails.get(0).getAttendances()) {
			attendanceColumns.add(new GradeColumn(Integer.toString(adw.getWeek()), i));
			i++;
		}
		
		assignmentGrades = CoursePlanService.getAssignmentGrades(currentStudent, semesterCourseId);
		assignmentColumns = new ArrayList<GradeColumn>();

		int k = 0;
		for (GradeWrapper gw : assignmentGrades.get(0).getGrades()) {
			assignmentColumns.add(new GradeColumn(gw.getLabel(), k));
			k++;
		}
	}
	
	public void refreshAchievements() {
		achievements = GamificationService.getAllAchievementsForSemesterCourseWithEverything(semesterCourseId);
		progressedAchievements = GamificationService.getAllAchievementProgressesForStudent(semesterCourseId);
		
		filteredAchievements = new ArrayList<Achievement>();
		
		for (Achievement ach : achievements) {
			boolean found = false;
			if(progressedAchievements != null) {
				for (AchievementProgress ap : progressedAchievements) {
					if(ap.getAchievement().equals(ach))
						found = true;
				}
			}
			if(!found)
				filteredAchievements.add(ach);
		}
	}
	
	public void refreshBadges() {
		badges = GamificationService.getAllBadgesForSemesterCourse(semesterCourseId);
		studentBadges = GamificationService.getAllBadgesForStudent(semesterCourseId);
		
		filteredBadges = new ArrayList<Badge>();
		
		for (Badge ach : badges) {
			boolean found = false;
			if(studentBadges != null) {
				for (StudentBadge ap : studentBadges) {
					if(ap.getBadge().equals(ach))
						found = true;
				}
			}
			if(!found)
				filteredBadges.add(ach);
		}
	}
	
	public void refreshLeaderboards() {
		List<Object> tuple = GamificationService.getLeaderboardUnsorted(semesterCourse, leaderboardSettings);
		
		if(tuple.get(0) != null)
			currentStudent = (LeaderboardWrapper) tuple.get(0);
		
		leaderboardStudents = (List<LeaderboardWrapper>) tuple.get(1);
	}
	
	public StreamedContent downloadFile(Document doc) {
		StreamedContent file = null;
		if(doc != null && doc.getDocumentId() > 0) {
			String docType = doc.getDocumentType();
			String filename = doc.getFilename() + doc.getFiletype();
			
			try {
				byte[] fileBytes = doc.getFileData().getBytes(1, (int)doc.getFileData().length());
				InputStream is = new ByteArrayInputStream(fileBytes);
				file = new DefaultStreamedContent(is, docType, filename);
			} catch (Exception e) {
				e.printStackTrace();
				JsfUtil.addErrorMessage("growl.assignment.noSubmittedFileForDownloading");
			}
		}
		
		if(file == null) {
			JsfUtil.addErrorMessage("growl.assignment.noSubmittedFileForDownloading");
		}
		
		return file;
	}
	
	public void goToAssignment(Assignment assignment) {
		if(assignment != null && assignment.getId() != null) {
			JsfUtil.redirect("assignment-details?asgnid=" + assignment.getId());
		}
	}
	
	public boolean isFilesRendered(CoursePlan cp) {
		if(cp.getMaterials() != null && cp.getMaterials().size() > 0) {
			return true;
		}
		return false;
	}
	
	public boolean isAssignmentsRendered(CoursePlan cp) {
		if(cp.getAssignments() != null && cp.getAssignments().size() > 0) {
			return true;
		}
		return false;
	}
	
	public boolean isStudentBadgesRendered() {
		return studentBadges != null && studentBadges.size() > 0;
	}
	
	public int getProgress(AchievementProgress ap) {
		if(ap.getAchievement().getTargetCount() == null || ap.getAchievement().getTargetCount() == 0)
			return 0;
		
		if(ap.getCurrentValue() == null || ap.getCurrentValue() == 0)
			return 0;
		
		Float result = ap.getCurrentValue().floatValue() / ap.getAchievement().getTargetCount().floatValue() * 100;
		
		return result.intValue();
	}
	
	public String getProgressStyle(AchievementProgress ap) {
		int progress = getProgress(ap);
		String style = "width: " + progress + "%;";
		if(progress == 0) {
			style += " background-color:#909090;";
		}
		
		return style;
	}
	
	public void prepareGamificationSettings() {
		settings = GamificationService.getUserSettings(semesterCourse.getId());
		
		if(settings == null) {
			settings = new StudentGamificationSettings();
		}
	}
	
	public String getImageClass(AchievementProgress ap) {
		String cls = "img-responsive wh-128";
		if(!ap.isComplete()) {
			cls += " incomplete-objective";
		}
		return cls;
	}
	
	public String stringifyObjective(Achievement ach) {
		return GamificationService.stringifyAchievementObjective(ach);
	}
	
	public String getProgressString(AchievementProgress ap) {
		return DataFormatter.formatPercent((double)getProgress(ap) / 100.0);
	}
	
	public void addOrUpdateGamificationSettings() {
		try {
			settings.setSemesterCourseId(semesterCourse.getId());
			settings.setStudentName(SessionService.getUsername());
			
			if(settings.getId() != null) {
				GamificationService.updateUserSettings(settings);
				updateSuccessful();
			} else {
				GamificationService.saveUserSettings(settings);
				saveSuccessful();
			}
		} catch (Exception e) {
			e.printStackTrace();
			saveOrUpdateFailed();
		}
	}
	
	public String getPointRewardString(Achievement ach) {
		return generalSettings.getPointsName() + ": " + (ach.getPointReward() == null ? "-" : ach.getPointReward() );
	}
	
	public String getBadgeRewardString(Achievement ach) {
		String badgeName = "-";
		if(ach.getBadgeReward() != null)
			badgeName =  ach.getBadgeReward().getName();
		
		return ResourceUtil.getLabel("badge.label.badge") + ": " + badgeName;
	}
	
	public String getGradeRewardString(Achievement ach) {
		if(ach.getRewardGradeCriteria() == null || ach.getGradeReward() == null)
			return ResourceUtil.getLabel("gamificationSettings.label.noGradeReward");
		else {
			return ach.getRewardGradeCriteria().getName() + ": " + ach.getGradeReward(); 
		}
	}
	
	public static void loadUrlParameters(String scid) {
		semCourseId = scid;
	}

	public static String getSemCourseId() {
		return semCourseId;
	}

	public static void setSemCourseId(String semCourseId) {
		CourseDetailsBean.semCourseId = semCourseId;
	}

	public Long getSemesterCourseId() {
		return semesterCourseId;
	}

	public void setSemesterCourseId(Long semesterCourseId) {
		this.semesterCourseId = semesterCourseId;
	}

	public List<CoursePlan> getCoursePlans() {
		return coursePlans;
	}

	public void setCoursePlans(List<CoursePlan> coursePlans) {
		this.coursePlans = coursePlans;
	}

	public SemesterCourse getSemesterCourse() {
		return semesterCourse;
	}

	public void setSemesterCourse(SemesterCourse semesterCourse) {
		this.semesterCourse = semesterCourse;
	}

	public StudentGamificationSettings getSettings() {
		return settings;
	}

	public void setSettings(StudentGamificationSettings settings) {
		this.settings = settings;
	}

	public List<Achievement> getAchievements() {
		return achievements;
	}

	public void setAchievements(List<Achievement> achievements) {
		this.achievements = achievements;
	}

	public GamificationSettings getGeneralSettings() {
		return generalSettings;
	}

	public void setGeneralSettings(GamificationSettings generalSettings) {
		this.generalSettings = generalSettings;
	}

	public List<AchievementProgress> getProgressedAchievements() {
		return progressedAchievements;
	}

	public void setProgressedAchievements(List<AchievementProgress> progressedAchievements) {
		this.progressedAchievements = progressedAchievements;
	}

	public List<Achievement> getFilteredAchievements() {
		return filteredAchievements;
	}

	public void setFilteredAchievements(List<Achievement> filteredAchievements) {
		this.filteredAchievements = filteredAchievements;
	}

	public List<Badge> getBadges() {
		return badges;
	}

	public void setBadges(List<Badge> badges) {
		this.badges = badges;
	}

	public List<Badge> getFilteredBadges() {
		return filteredBadges;
	}

	public void setFilteredBadges(List<Badge> filteredBadges) {
		this.filteredBadges = filteredBadges;
	}

	public List<StudentBadge> getStudentBadges() {
		return studentBadges;
	}

	public void setStudentBadges(List<StudentBadge> studentBadges) {
		this.studentBadges = studentBadges;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public LeaderboardSettings getLeaderboardSettings() {
		return leaderboardSettings;
	}

	public void setLeaderboardSettings(LeaderboardSettings leaderboardSettings) {
		this.leaderboardSettings = leaderboardSettings;
	}

	public List<LeaderboardWrapper> getLeaderboardStudents() {
		return leaderboardStudents;
	}

	public void setLeaderboardStudents(List<LeaderboardWrapper> leaderboardStudents) {
		this.leaderboardStudents = leaderboardStudents;
	}

	public LeaderboardWrapper getCurrentStudent() {
		return currentStudent;
	}

	public void setCurrentStudent(LeaderboardWrapper currentStudent) {
		this.currentStudent = currentStudent;
	}

	public List<StudentGradeRowWrapper> getGradeStudents() {
		return gradeStudents;
	}

	public void setGradeStudents(List<StudentGradeRowWrapper> gradeStudents) {
		this.gradeStudents = gradeStudents;
	}

	public List<GradeColumn> getOtherGradeColumns() {
		return otherGradeColumns;
	}

	public void setOtherGradeColumns(List<GradeColumn> otherGradeColumns) {
		this.otherGradeColumns = otherGradeColumns;
	}

	public List<GradeColumn> getExamGradeColumns() {
		return examGradeColumns;
	}

	public void setExamGradeColumns(List<GradeColumn> examGradeColumns) {
		this.examGradeColumns = examGradeColumns;
	}

	public List<AssignmentGradeWrapper> getAssignmentGrades() {
		return assignmentGrades;
	}

	public void setAssignmentGrades(List<AssignmentGradeWrapper> assignmentGrades) {
		this.assignmentGrades = assignmentGrades;
	}

	public List<AttendanceDetailsWrapper> getAttendanceDetails() {
		return attendanceDetails;
	}

	public void setAttendanceDetails(List<AttendanceDetailsWrapper> attendanceDetails) {
		this.attendanceDetails = attendanceDetails;
	}

	public List<GradeColumn> getAssignmentColumns() {
		return assignmentColumns;
	}

	public void setAssignmentColumns(List<GradeColumn> assignmentColumns) {
		this.assignmentColumns = assignmentColumns;
	}

	public List<GradeColumn> getAttendanceColumns() {
		return attendanceColumns;
	}

	public void setAttendanceColumns(List<GradeColumn> attendanceColumns) {
		this.attendanceColumns = attendanceColumns;
	}

	public List<User> getOtherStudents() {
		return otherStudents;
	}

	public void setOtherStudents(List<User> otherStudents) {
		this.otherStudents = otherStudents;
	}
}
