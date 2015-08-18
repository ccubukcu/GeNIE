package com.genie.dao;

import java.util.List;

import com.genie.model.AbstractBaseEntity;
import com.genie.model.Achievement;
import com.genie.model.AchievementProgress;
import com.genie.model.Badge;
import com.genie.model.GamificationSettings;
import com.genie.model.LeaderboardSettings;
import com.genie.model.SemesterCourse;
import com.genie.model.StudentBadge;
import com.genie.model.StudentGamificationSettings;
import com.genie.model.StudentPoint;
import com.genie.pojo.LeaderboardWrapper;

/**
 * @author ccubukcu
 *
 */
public interface GamificationDAO {
	public boolean save(AbstractBaseEntity entity);
	public boolean update(AbstractBaseEntity entity);
	public GamificationSettings getGeneralSettingsForSemesterCourse(Long semesterCourseId);
	public LeaderboardSettings getLeaderboardSettingsForSemesterCourse(Long semesterCourseId);
	public List<Badge> getBadgesForSemesterCourse(Long semesterCourseId);
	public boolean deleteBadge(Badge selectedBadge);
	public boolean deleteAchievement(Achievement selectedAchievement);
	public List<Achievement> getAchievementsForSemesterCourseWithEverything(Long semesterCourseId);
	public Badge getBadgeById(Long long1);
	
	public List<StudentPoint> getAllPointsByStudent(String username, Long semesterCourseId);
	public List<Achievement> getAttendanceAchievements(Long semesterCourseId);
	public AchievementProgress getAchievementProgress(Long id, String studentName);
	public List<Achievement> getGradeAchievements(Long id);
	public List<Achievement> getSubmissionAchievements(Long assignmentId);
	public StudentGamificationSettings getUserSettings(String username, Long semesterCourseId);
	public List<AchievementProgress> getAllAchievementProgresses(String username, Long semesterCourseId);
	public List<StudentBadge> getAllBadgesForStudent(String username, Long semesterCourseId);
	public List<LeaderboardWrapper> getAllPointsForSemesterCourse(SemesterCourse semCourse);
	public List<Badge> getBadgesForStudent(String username);
}
