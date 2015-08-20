package com.genie.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;

import com.genie.enums.AchievementType;
import com.genie.enums.ComparisonType;
import com.genie.enums.GradingCriteria;
import com.genie.enums.LeaderboardVisibility;
import com.genie.model.Achievement;
import com.genie.model.AchievementProgress;
import com.genie.model.AchievementProgressItem;
import com.genie.model.AssignmentSubmission;
import com.genie.model.Badge;
import com.genie.model.GamificationSettings;
import com.genie.model.LeaderboardSettings;
import com.genie.model.SemesterCourse;
import com.genie.model.StudentAttendance;
import com.genie.model.StudentBadge;
import com.genie.model.StudentGamificationSettings;
import com.genie.model.StudentGrade;
import com.genie.model.StudentPoint;
import com.genie.pojo.LeaderboardWrapper;
import com.genie.utils.DaoUtil;
import com.genie.utils.PortalConstants;
import com.genie.utils.ResourceUtil;

public class GamificationService {
	
	public static GamificationSettings getGeneralSettingsForSemesterCourse(Long semesterCourseId) {
		return DaoUtil.getGamificationDAO().getGeneralSettingsForSemesterCourse(semesterCourseId);
	}

	public static void updateGeneralSettings(GamificationSettings generalSettings) {
		DaoUtil.getGamificationDAO().update(generalSettings);
	}

	public static void saveGeneralSettings(GamificationSettings generalSettings) {
		DaoUtil.getGamificationDAO().save(generalSettings);
	}

	public static LeaderboardSettings getLeaderboardSettingsForSemesterCourse(Long semesterCourseId) {
		return DaoUtil.getGamificationDAO().getLeaderboardSettingsForSemesterCourse(semesterCourseId);
	}

	public static void updateLeaderboardSettings(LeaderboardSettings leaderboardSettings) {
		DaoUtil.getGamificationDAO().update(leaderboardSettings);
	}

	public static void saveLeaderboardSettings(LeaderboardSettings leaderboardSettings) {
		DaoUtil.getGamificationDAO().save(leaderboardSettings);
	}

	public static List<Badge> getAllBadgesForSemesterCourse(Long semesterCourseId) {
		return DaoUtil.getGamificationDAO().getBadgesForSemesterCourse(semesterCourseId);
	}

	public static void updateBadge(Badge selectedBadge) {
		DaoUtil.getGamificationDAO().update(selectedBadge);
	}
	
	public static void addBadge(Badge selectedBadge) {
		DaoUtil.getGamificationDAO().save(selectedBadge);
	}
	
	public static void deleteBadge(Badge selectedBadge) {
		DaoUtil.getGamificationDAO().deleteBadge(selectedBadge);
	}

	public static void updateAchievement(Achievement selectedAchievement) {
		DaoUtil.getGamificationDAO().update(selectedAchievement);
	}
	
	public static void addAchievement(Achievement selectedAchievement) {
		DaoUtil.getGamificationDAO().save(selectedAchievement);
	}
	
	public static void deleteAchievement(Achievement selectedAchievement) {
		DaoUtil.getGamificationDAO().deleteAchievement(selectedAchievement);
	}

	public static List<Achievement> getAllAchievementsForSemesterCourseWithEverything(Long semesterCourseId) {
		return DaoUtil.getGamificationDAO().getAchievementsForSemesterCourseWithEverything(semesterCourseId);
	}

	public static List<StudentPoint> getAllPointsForStudent(String username, Long semesterCourseId) {
		return DaoUtil.getGamificationDAO().getAllPointsByStudent(username, semesterCourseId);
	}

	public static List<Achievement> getAttendanceAchievements(Long semesterCourseId) {
		return DaoUtil.getGamificationDAO().getAttendanceAchievements(semesterCourseId);
	}
	
	public static List<Achievement> recordGradeProgress(List<Achievement> gradeAchievements, StudentGrade sg, Long gradeCriteriaId) {
		List<Achievement> completed = new ArrayList<Achievement>();
		if(gradeAchievements != null) {
			for (Achievement ach : gradeAchievements) {
				if(ach.getTargetGradingCriteria() == null || ach.getTargetGradeCriteriaId() == null ||
						ach.getTargetGradeCriteriaId() == sg.getGradeCriteriaId()) {
					AchievementProgress ap = DaoUtil.getGamificationDAO().getAchievementProgress(ach.getId(), sg.getStudentName());
					
					if(ap == null) {
						ap = new AchievementProgress();
						ap.setAchievementId(ach.getId());
						ap.setUsername(sg.getStudentName());
						ap.setComplete(false);
						ap.setCurrentValue(0);
					}
	
					if(!ap.isComplete()) {
						AchievementProgressItem progressItem = null;
						boolean progressItemFound = false;
						
						if(ap.getProgressItems() != null) {
							for (AchievementProgressItem api : ap.getProgressItems()) {
								if(api.getGradeCriteriaId().equals(gradeCriteriaId)) {
									progressItem = api;
									progressItemFound = true;
								}
							}
						}
						
						if(!progressItemFound) {
							int curVal = ap.getCurrentValue();
							progressItem = new AchievementProgressItem();
							progressItem.setGradeCriteriaId(gradeCriteriaId);
							
							if(ComparisonType.compare(ach.getComparisonType(), sg.getGrade(), ach.getThresholdValue())) {
								curVal++;
								ap.setCurrentValue(curVal);
								
								if(ach.getTargetCount() == ap.getCurrentValue()) {
									ap.setComplete(true);
									completed.add(ach);
									achievementCompleted(sg.getStudentName(), ach);
								}
								
								recordAchievementProgress(ap);
								progressItem.setAchievementProgressId(ap.getId());
								recordAchievementProgressItem(progressItem);
							}
						}
					}
					
				}
			}
		}
		return completed;
	}
	
	public static List<Achievement> recordSubmissionProgress(List<Achievement> submissionAchievements, AssignmentSubmission assignmentSubmission) {
		List<Achievement> completed = new ArrayList<Achievement>();
		if(submissionAchievements != null) {
			for (Achievement ach : submissionAchievements) {
				AchievementProgress ap = DaoUtil.getGamificationDAO().getAchievementProgress(ach.getId(), assignmentSubmission.getStudentName());
				
				if(ap == null) {
					ap = new AchievementProgress();
					ap.setAchievementId(ach.getId());
					ap.setUsername(assignmentSubmission.getStudentName());
					ap.setComplete(false);
					ap.setCurrentValue(0);
				}
				
				if(!ap.isComplete()) {
					AchievementProgressItem progressItem = null;
					boolean progressItemFound = false;
					
					if(ap.getProgressItems() != null) {
						for (AchievementProgressItem api : ap.getProgressItems()) {
							if(api.getAssignmentId().equals(assignmentSubmission.getAssignmentId())) {
								progressItem = api;
								progressItemFound = true;
							}
						}
					}
					
					if(!progressItemFound) {
						int curVal = ap.getCurrentValue();
						Integer comparisonType = ach.getComparisonType();
						
						progressItem = new AchievementProgressItem();
						progressItem.setAssignmentId(assignmentSubmission.getAssignmentId());
						
						if(ach.getAchievementType() == AchievementType.SUBMISSION_OVERALL.getIndex()) {
							comparisonType = ach.getComparisonType();
						} else {
							comparisonType = ComparisonType.SMALLER.getIndex();
						}
						
						DateTime dueDate = new DateTime(assignmentSubmission.getAssignment().getDueDate());
						DateTime checkDate = dueDate.minusDays(ach.getThresholdValue() != null ? ach.getThresholdValue() : 0);
						
						if(ComparisonType.compare(comparisonType, new DateTime(assignmentSubmission.getCreationTime()), checkDate)) {
							curVal++;
							ap.setCurrentValue(curVal);
							
							if(ach.getTargetCount() == ap.getCurrentValue()) {
								ap.setComplete(true);
								completed.add(ach);
								achievementCompleted(assignmentSubmission.getStudentName(), ach);
							}
							
							recordAchievementProgress(ap);
							progressItem.setAchievementProgressId(ap.getId());
							recordAchievementProgressItem(progressItem);
						}
					}
				}
			}
		}
		
		return completed;
	}
	
	public static List<Achievement> recordAttendanceProgress(List<Achievement> attendanceAchievements, String studentName, long weekOrder, long totalWeeks) {
		List<Achievement> completed = new ArrayList<Achievement>();
		if(attendanceAchievements != null) {
			for (Achievement ach : attendanceAchievements) {
				AchievementProgress ap = DaoUtil.getGamificationDAO().getAchievementProgress(ach.getId(), studentName);

				if(ap == null) {
					ap = new AchievementProgress();
					ap.setAchievementId(ach.getId());
					ap.setUsername(studentName);
					ap.setComplete(false);
					ap.setCurrentValue(0);
				}
				
				if(!ap.isComplete()) {
					AchievementProgressItem progressItem = null;
					boolean progressItemFound = false;
					boolean[] attendances = new boolean[(int)totalWeeks];
					for (AchievementProgressItem api : ap.getProgressItems()) {
						if(api.getAttendanceWeek() == weekOrder) {
							progressItem = api;
							progressItemFound = true;
						}
						attendances[api.getAttendanceWeek()] = true;
					}
					
					attendances[(int)weekOrder] = true;
					
					if(!progressItemFound) {
						progressItem = new AchievementProgressItem();
						progressItem.setAttendanceWeek((int) weekOrder);
						
						int curVal = ap.getCurrentValue();
						
						boolean conditionMet = false;
						if(ach.getAchievementType() == AchievementType.ATTENDANCE_OVERALL.getIndex()) {
							curVal++;
							ap.setCurrentValue(curVal);
							conditionMet = true;
						} else {
							int consecutive = 0;
							for (boolean att : attendances) {
								if(att)
									consecutive++;
								else
									consecutive = 0;
								
								if(consecutive == ach.getTargetCount()) {
									conditionMet = true;
									break;
								}
							}
							if(conditionMet)
								ap.setCurrentValue(consecutive);
						}
						
						if(conditionMet) {
							if(ach.getTargetCount() == ap.getCurrentValue()) {
								ap.setComplete(true);
								completed.add(ach);
								achievementCompleted(studentName, ach);
							}
							recordAchievementProgress(ap);
							progressItem.setAchievementProgressId(ap.getId());
							recordAchievementProgressItem(progressItem);
						}
					}
				}
			}
		}
		
		return completed;
	}
	
	public static void achievementCompleted(String username, Achievement ach) {
		if(ach.getPointReward() > 0) {
			StudentPoint sp = new StudentPoint();
			sp.setSemesterCourseId(ach.getSemesterCourseId());
			sp.setPoints(ach.getPointReward());
			sp.setUsername(username);
			addStudentPoints(sp);
		}
		
		if(ach.getBadgeRewardId() != null) {
			StudentBadge sb = new StudentBadge();
			sb.setBadgeId(ach.getBadgeRewardId());
			sb.setUsername(username);
			addStudentBadge(sb);
		}
		
		if(ach.getRewardGradeCriteriaId() != null && ach.getGradeReward() != null) {
			if(ach.getRewardGradeCriteria().getGradingCriteria() == GradingCriteria.ATTENDANCE.getIndex()) {
				StudentAttendance att = new StudentAttendance();
				
				att.setAttended(true);
				att.setStudentName(username);
				att.setSemesterCourseId(ach.getSemesterCourseId());
				att.setWeek(ach.getWeekReward());
				CoursePlanService.addAttendance(att);
			} else {
				StudentGrade sg = new StudentGrade();
				sg.setGrade(ach.getGradeReward().floatValue());
				sg.setGradeCriteriaId(ach.getRewardGradeCriteriaId());
				sg.setStudentName(username);
				sg.setSemesterCourseId(ach.getSemesterCourseId());
				
				if(ach.getRewardGradeCriteria().getGradingCriteria() == GradingCriteria.ASSIGNMENT.getIndex()) {
					sg.setAssignmentId(ach.getRewardAssignmentId());
				}
				
				CoursePlanService.addGrade(sg);
			}
		}
	}
	
	public static void addStudentPoints(StudentPoint sp) {
		DaoUtil.getGamificationDAO().save(sp);
	}
	
	public static void addStudentBadge(StudentBadge sb) {
		DaoUtil.getGamificationDAO().save(sb);
	}
	
	public static void recordAchievementProgress(AchievementProgress ap) {
		if(ap.getId() == null)
			DaoUtil.getGamificationDAO().save(ap);
		else
			DaoUtil.getGamificationDAO().update(ap);
	}
	
	public static void recordAchievementProgressItem(AchievementProgressItem api) {
		if(api.getId() == null)
			DaoUtil.getGamificationDAO().save(api);
		else
			DaoUtil.getGamificationDAO().update(api);
	}

	public static List<Achievement> getGradeAchievements(Long id) {
		return DaoUtil.getGamificationDAO().getGradeAchievements(id);
	}

	public static List<Achievement> getSubmissionAchievements(Long assignmentId) {
		return DaoUtil.getGamificationDAO().getSubmissionAchievements(assignmentId);
	}

	public static StudentGamificationSettings getUserSettings(Long semesterCourseId) {
		return DaoUtil.getGamificationDAO().getUserSettings(SessionService.getUsername(), semesterCourseId);
	}

	public static void updateUserSettings(StudentGamificationSettings settings) {
		DaoUtil.getGamificationDAO().update(settings);
	}

	public static void saveUserSettings(StudentGamificationSettings settings) {
		DaoUtil.getGamificationDAO().save(settings);
	}

	public static List<AchievementProgress> getAllAchievementProgressesForStudent(Long semesterCourseId) {
		return DaoUtil.getGamificationDAO().getAllAchievementProgresses(SessionService.getUsername(), semesterCourseId);
	}

	public static String stringifyAchievementObjective(Achievement ach) {
		String objective = "";
		
		if(ach != null) {
			if (ach.getAchievementType().equals(AchievementType.ATTENDANCE_OVERALL.getIndex())) {
				objective = ResourceUtil.getLabel("achievement.label.objectiveAttendance", ach.getTargetCount());
			} else if (ach.getAchievementType().equals(AchievementType.ATTENDANCE_CONSECUTIVE.getIndex())) {
				objective = ResourceUtil.getLabel("achievement.label.objectiveAttendanceConsecutive", ach.getTargetCount());
			} else if (ach.getAchievementType().equals(AchievementType.SUBMISSION_OVERALL.getIndex())) {
				objective = ResourceUtil.getLabel("achievement.label.objectiveSubmissionOverall", ach.getTargetCount());
			} else if (ach.getAchievementType().equals(AchievementType.SUBMISSION_EARLY.getIndex())) {
				objective = ResourceUtil.getLabel("achievement.label.objectiveSubmissionEarly", ach.getTargetCount(), ach.getThresholdValue());
			} else if (ach.getAchievementType().equals(AchievementType.GRADE.getIndex())) {
				String comparisonType = ComparisonType.getTextLabel(ach.getComparisonType());
				
				if(ach.getTargetGradingCriteria() == null || ach.getTargetGradingCriteria() == 0) {
					objective = ResourceUtil.getLabel("achievement.label.objectiveGradeAll", comparisonType, ach.getThresholdValue(), ach.getTargetCount());
				} else if ((ach.getTargetGradeCriteriaId() == null || ach.getTargetGradeCriteriaId() == 0) 
						&& ach.getTargetGradingCriteria().equals(GradingCriteria.ASSIGNMENT.getIndex())) {
					String gradeType = GradingCriteria.getTextLabel(ach.getTargetGradingCriteria());
					objective = ResourceUtil.getLabel("achievement.label.objectiveGradeGeneral",
							comparisonType, ach.getThresholdValue(), ach.getTargetCount(), gradeType);
				} else {
					objective = ResourceUtil.getLabel("achievement.label.objectiveGradeSpecific",
							comparisonType, ach.getThresholdValue(), ach.getTargetCount(), ach.getTargetGradeCriteria().getName());
				}
			}
		}
		
		return objective;
	}

	public static List<StudentBadge> getAllBadgesForStudent(Long semesterCourseId) {
		return DaoUtil.getGamificationDAO().getAllBadgesForStudent(SessionService.getUsername(), semesterCourseId);
	}

	public static List<Object> getLeaderboardUnsorted(SemesterCourse semesterCourse, LeaderboardSettings leaderboardSettings) {
		List<Object> objects = new ArrayList<Object>();
		List<LeaderboardWrapper> leaderboardData = DaoUtil.getGamificationDAO().getAllPointsForSemesterCourse(semesterCourse);
		
		Collections.sort(leaderboardData);
		
		if(leaderboardSettings == null)
			leaderboardSettings = new LeaderboardSettings();
		
		String anonymous = null;
		if(leaderboardSettings.isAnonymous())
			anonymous = ResourceUtil.getLabel("gamificationSettings.label.anonymous");
		
		int i=1;
		boolean studentFound = false;
		for (LeaderboardWrapper lw : leaderboardData) {
			lw.setStanding(i);
			
			if(lw.getUsername().equals(SessionService.getUsername())) {
				objects.add(0, lw);
				studentFound = true;
			} else if (anonymous != null) {
				lw.setUsername(anonymous);
			}
			
			i++;
		}
		
		if(!studentFound) {
			LeaderboardWrapper lw = new LeaderboardWrapper();
			lw.setUsername(SessionService.getUsername());
			lw.setPoint(0L);
			lw.setStanding(0);
			objects.add(0, lw);
		}
		
		List<LeaderboardWrapper> filteredData = new ArrayList<LeaderboardWrapper>();
		
		if((leaderboardSettings.getVisibility() == LeaderboardVisibility.TOP.getIndex() && leaderboardData.size() <= leaderboardSettings.getTopStudents())
			|| (leaderboardSettings.getVisibility() == LeaderboardVisibility.TOP_AND_BOTTOM.getIndex() 
					&& leaderboardData.size() <= (leaderboardSettings.getTopStudents() + leaderboardSettings.getBottomStudents()))
			|| leaderboardSettings.getVisibility() == LeaderboardVisibility.ALL_VISIBLE.getIndex()) {
			filteredData = leaderboardData;
		} else {
			if (leaderboardSettings.getVisibility() == LeaderboardVisibility.TOP.getIndex()) {
				filteredData = leaderboardData.subList(0, leaderboardSettings.getTopStudents());
			} else if (leaderboardSettings.getVisibility() == LeaderboardVisibility.TOP_AND_BOTTOM.getIndex()) {
				filteredData = new ArrayList<LeaderboardWrapper>();
				int size = leaderboardData.size();
				
				filteredData.addAll(leaderboardData.subList(0, leaderboardSettings.getTopStudents()));
				filteredData.addAll(GamificationService.getFillerLeaderboardItems());
				filteredData.addAll(leaderboardData.subList(size - leaderboardSettings.getBottomStudents(), size));
			}
		}

		objects.add(1, filteredData);
		
		return objects;
	}

	public static List<LeaderboardWrapper> getFillerLeaderboardItems() {
		List<LeaderboardWrapper> fillerItems = new ArrayList<LeaderboardWrapper>();
		
		for(int i=0; i<PortalConstants.LEADERBOARD_FILLER_COUNT; i++) {
			LeaderboardWrapper filler = new LeaderboardWrapper();
			filler.setUsername("...");
			fillerItems.add(filler);
		}
		return fillerItems;
	}

	public static List<Badge> getAllBadgesForStudent(String username) {
		return DaoUtil.getGamificationDAO().getBadgesForStudent(username);
	}

}
