package com.genie.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.Hibernate;

import com.genie.dao.GamificationDAO;
import com.genie.enums.AchievementType;
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
@SuppressWarnings("unchecked")
public class GamificationDAOImpl extends BaseDAOImpl implements GamificationDAO {
	private static final long serialVersionUID = -6940380809375031828L;

	@Override
	public GamificationSettings getGeneralSettingsForSemesterCourse(Long semesterCourseId) {
		GamificationSettings settings = null;

		try {
			Query q = entityManager
					.createQuery("select a from GamificationSettings a where a.active = 1 AND a.semesterCourseId = :semCourseId");
			
			q.setParameter("semCourseId", semesterCourseId);
			
			settings = (GamificationSettings) q.getSingleResult();
		} catch (NoResultException nre) {
			settings = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return settings;
	}

	@Override
	public LeaderboardSettings getLeaderboardSettingsForSemesterCourse(Long semesterCourseId) {
		LeaderboardSettings settings = null;

		try {
			Query q = entityManager
					.createQuery("select a from LeaderboardSettings a where a.active = 1 AND a.semesterCourseId = :semCourseId");
			
			q.setParameter("semCourseId", semesterCourseId);
			
			settings = (LeaderboardSettings) q.getSingleResult();
		} catch (NoResultException nre) {
			settings = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return settings;
	}

	@Override
	public List<Badge> getBadgesForSemesterCourse(Long semesterCourseId) {
		List<Badge> badges = null;

		try {
			Query q = entityManager
					.createQuery("select a from Badge a where a.active = 1 AND a.semesterCourseId = :semCourseId");
			
			q.setParameter("semCourseId", semesterCourseId);
			
			badges = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return badges;
	}

	@Override
	public boolean deleteBadge(Badge item) {
		item = entityManager.getReference(Badge.class, item.getId());
		if (item == null)
			return false;
		item.setActive(false);
		update(item);
		return true;
	}

	@Override
	public boolean deleteAchievement(Achievement item) {
		item = entityManager.getReference(Achievement.class, item.getId());
		if (item == null)
			return false;
		item.setActive(false);
		update(item);
		return true;
	}

	@Override
	public List<Achievement> getAchievementsForSemesterCourseWithEverything(Long semesterCourseId) {
		List<Achievement> achievements = null;

		try {
			Query q = entityManager
					.createQuery("select a from Achievement a where a.active = 1 AND a.semesterCourseId = :semCourseId");
			
			q.setParameter("semCourseId", semesterCourseId);
			
			achievements = q.getResultList();
			
			if(achievements != null) {
				for (Achievement ach : achievements) {
					if(ach.getBadgeRewardId() != null)
						Hibernate.initialize(ach.getBadgeReward());

					if(ach.getTargetGradeCriteriaId() != null)
						Hibernate.initialize(ach.getTargetGradeCriteria());
					
					if(ach.getRewardGradeCriteriaId() != null)
						Hibernate.initialize(ach.getRewardGradeCriteria());
					
					Hibernate.initialize(ach.getSemesterCourse());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return achievements;
	}

	@Override
	public Badge getBadgeById(Long id) {
		return entityManager.find(Badge.class, id);
	}

	@Override
	public List<StudentPoint> getAllPointsByStudent(String username, Long semesterCourseId) {
		List<StudentPoint> points = null;

		try {
			Query q = entityManager
					.createQuery("select a from StudentPoint a where a.active = 1 AND a.username = :username"
							+ " AND a.semesterCourseId = :semesterCourseId");
			
			q.setParameter("username", username);
			q.setParameter("semesterCourseId", semesterCourseId);
			
			points = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return points;
	}

	@Override
	public List<Achievement> getAttendanceAchievements(Long semesterCourseId) {
		List<Achievement> achievements = null;

		try {
			Query q = entityManager
					.createQuery("select a from Achievement a where a.active = 1 AND a.semesterCourseId = :semCoId AND "
							+ " (a.achievementType = :type1 OR a.achievementType = :type2)");

			q.setParameter("semCoId", semesterCourseId);
			q.setParameter("type1", AchievementType.ATTENDANCE_CONSECUTIVE.getIndex());
			q.setParameter("type2", AchievementType.ATTENDANCE_OVERALL.getIndex());
			
			achievements = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return achievements;
	}

	@Override
	public AchievementProgress getAchievementProgress(Long id, String studentName) {
		AchievementProgress ap = null;

		try {
			Query q = entityManager
					.createQuery("select a from AchievementProgress a where a.active = 1 AND a.username = :username"
							+ " AND a.achievementId = :id");

			q.setParameter("username", studentName);
			q.setParameter("id", id);
			
			ap = (AchievementProgress) q.getSingleResult();
			
			Hibernate.initialize(ap.getProgressItems());
		} catch (NoResultException nre) { 
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ap;
	}

	@Override
	public List<Achievement> getGradeAchievements(Long semesterCourseId) {
		List<Achievement> achievements = null;

		try {
			Query q = entityManager
					.createQuery("select a from Achievement a where a.active = 1 AND a.semesterCourseId = :semCoId AND "
							+ " a.achievementType = :type");

			q.setParameter("semCoId", semesterCourseId);
			q.setParameter("type", AchievementType.GRADE.getIndex());
			
			achievements = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return achievements;
	}

	@Override
	public List<Achievement> getSubmissionAchievements(Long assignmentId) {
		List<Achievement> achievements = null;

		try {
			Query q = entityManager
					.createQuery("select ach from Achievement ach, Assignment asgn where ach.active = 1 AND ach.semesterCourseId = asgn.plan.semesterCourseId AND "
							+ " asgn.id = :id AND (ach.achievementType = :type1 OR ach.achievementType = :type2)");

			q.setParameter("id", assignmentId);
			q.setParameter("type1", AchievementType.SUBMISSION_EARLY.getIndex());
			q.setParameter("type2", AchievementType.SUBMISSION_OVERALL.getIndex());
			
			achievements = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return achievements;
	}

	@Override
	public StudentGamificationSettings getUserSettings(String username, Long semesterCourseId) {
		StudentGamificationSettings sgs = null;

		try {
			Query q = entityManager
					.createQuery("select a from StudentGamificationSettings a where a.active = 1 AND a.studentName = :username"
							+ " AND a.semesterCourseId = :id");

			q.setParameter("username", username);
			q.setParameter("id", semesterCourseId);
			
			sgs = (StudentGamificationSettings) q.getSingleResult();
		} catch (NoResultException nre) { 
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sgs;
	}

	@Override
	public List<AchievementProgress> getAllAchievementProgresses(String username, Long semesterCourseId) {
		List<AchievementProgress> progresses = null;

		try {
			Query q = entityManager
					.createQuery("select a from AchievementProgress a WHERE a.active = 1 AND a.achievement.semesterCourseId = :semCoId "
							+ " AND a.username = :username");

			q.setParameter("semCoId", semesterCourseId);
			q.setParameter("username", username);
			
			progresses = q.getResultList();
			
			for (AchievementProgress ap : progresses) {
				Hibernate.initialize(ap.getAchievement());	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return progresses;
	}

	@Override
	public List<StudentBadge> getAllBadgesForStudent(String username, Long semesterCourseId) {
		List<StudentBadge> badges = null;

		try {
			Query q = entityManager
					.createQuery("select a from StudentBadge a WHERE a.active = 1 AND a.badge.semesterCourseId = :semCoId "
							+ " AND a.username = :username");

			q.setParameter("semCoId", semesterCourseId);
			q.setParameter("username", username);
			
			badges = q.getResultList();
			
			for (StudentBadge sb : badges) {
				Hibernate.initialize(sb.getBadge());
				Hibernate.initialize(sb.getUser());	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return badges;
	}

	@Override
	public List<LeaderboardWrapper> getAllPointsForSemesterCourse(SemesterCourse semCourse) {
		List<LeaderboardWrapper> wrappers = null;

		try {
			String sql  = "select new com.genie.pojo.LeaderboardWrapper(auth.user.username, sum(sp.points)) from Authority auth, StudentPoint sp "
					+ " WHERE auth.semesterId = :semId AND auth.courseId = :courseId AND auth.username = sp.username AND auth.active = 1 "
					+ " AND sp.semesterCourseId = :semCourseId GROUP BY sp.username";
			
			Query q = entityManager.createQuery(sql);
			q.setParameter("semId", semCourse.getSemesterId());
			q.setParameter("courseId", semCourse.getCourseId());
			q.setParameter("semCourseId", semCourse.getId());
			
			wrappers = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return wrappers;
	}

	@Override
	public List<Badge> getBadgesForStudent(String username) {
		List<Badge> badges = null;

		try {
			Query q = entityManager
					.createQuery("select b from Badge b, StudentBadge sb, SemesterCourse sc, StudentGamificationSettings sgs "
							+ " WHERE b.active = 1 AND sc.active = 1 AND b.semesterCourseId = sc.id "
							+ " AND sgs.semesterCourseId = sc.id AND b.id = sb.badgeId "
							+ " AND sgs.badgesEnabled = 1 "
							+ " AND sb.username = :username");

			q.setParameter("username", username);
			
			badges = q.getResultList();
			
			for (Badge badge : badges) {
				Hibernate.initialize(badge.getSemesterCourse());
				Hibernate.initialize(badge.getSemesterCourse().getSemester());
				Hibernate.initialize(badge.getSemesterCourse().getCourse());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return badges;
	}
}
