package com.genie.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Hibernate;

import com.genie.dao.CoursePlanDAO;
import com.genie.model.Assignment;
import com.genie.model.AssignmentSubmission;
import com.genie.model.CourseMaterial;
import com.genie.model.CoursePlan;
import com.genie.model.GradeCriteria;

/**
 * @author ccubukcu
 *
 */
@SuppressWarnings("unchecked")
public class CoursePlanDAOImpl extends BaseDAOImpl implements CoursePlanDAO {

	private static final long serialVersionUID = -6834397366448509323L;

	@Override
	public List<CoursePlan> getAll() {
		List<CoursePlan> items = null;

		try {
			items = entityManager
					.createQuery("select a from CoursePlan a where a.active = 1").getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	@Override
	public CoursePlan getById(long id) {
		return entityManager.find(CoursePlan.class, id);
	}

	@Override
	public boolean delete(CoursePlan item) {
		item = entityManager.getReference(CoursePlan.class, item.getId());
		if (item == null)
			return false;
		item.setActive(false);
		update(item);
		return true;
	}
	
	@Override
	public List<CoursePlan> getAllByYearAndSemester(Long schoolYearId, Long semesterId) {
		List<CoursePlan> items = null;

		try {
			Query q = entityManager
					.createQuery("select a from CoursePlan a where a.active = 1 AND a.semesterId = :semId AND a.semester.schoolYearId = :yearId ORDER BY a.weekOrder ASC");
			q.setParameter("yearId", schoolYearId);
			q.setParameter("semId", semesterId);
			
			items = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	@Override
	public CoursePlan getByIdWithSemesterAndCourse(Long id) {
		CoursePlan item = getById(id);

		Hibernate.initialize(item.getSemesterCourse());
		Hibernate.initialize(item.getSemesterCourse().getSemester());
		Hibernate.initialize(item.getSemesterCourse().getCourse());
		Hibernate.initialize(item.getSemesterCourse().getSemester().getSchoolYear());
		
		return item;
	}

	@Override
	public List<CoursePlan> getAllPlansBySemesterCourseId(Long id) {
		List<CoursePlan> items = null;

		try {
			Query q = entityManager
					.createQuery("select a from CoursePlan a where a.active = 1 AND a.semesterCourseId = :semCoId ORDER BY a.planOrder ASC");
			q.setParameter("semCoId", id);
			
			items = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	@Override
	public List<CoursePlan> getAllPlansBySemesterCourseIdWithMaterials(Long id) {
		List<CoursePlan> items = getAllPlansBySemesterCourseId(id);

		for (CoursePlan cp : items) {
			Hibernate.initialize(cp.getMaterials());
		}
		
		return items;
	}
	
	@Override
	public List<CoursePlan> getAllPlansBySemesterCourseIdWithMaterialDocuments(Long id) {
		List<CoursePlan> items = getAllPlansBySemesterCourseId(id);

		for (CoursePlan cp : items) {
			Hibernate.initialize(cp.getAssignments());
			Hibernate.initialize(cp.getMaterials());
			
			for (CourseMaterial cm : cp.getMaterials()) {
				Hibernate.initialize(cm.getDocument());
			}
		}
		
		return items;
	}

	@Override
	public List<GradeCriteria> getGradeCriteriaByTypeAndSemesterCourse(int type, Long semesterCourseId) {
		List<GradeCriteria> items = null;

		try {
			Query q = entityManager
					.createQuery("select a from GradeCriteria a where a.active = 1 AND a.semesterCourseId = :semCoId AND a.gradingCriteria = :type");
			q.setParameter("semCoId", semesterCourseId);
			q.setParameter("type", type);
			
			items = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	@Override
	public List<Assignment> getAllAssignmentsBySemesterCourse(Long id) {
		List<Assignment> items = null;

		try {
			Query q = entityManager
					.createQuery("select a from Assignment a where a.active = 1 AND a.plan.semesterCourseId = :semCoId");
			q.setParameter("semCoId", id);
			items = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	@Override
	public GradeCriteria getCriteriaById(Long id) {
		return entityManager.find(GradeCriteria.class, id);
	}

	@Override
	public List<GradeCriteria> getGradeCriteriaBySemesterCourse(Long semesterCourseId) {
		List<GradeCriteria> items = null;

		try {
			Query q = entityManager
					.createQuery("select a from GradeCriteria a where a.active = 1 AND a.semesterCourseId = :semCoId");
			q.setParameter("semCoId", semesterCourseId);
			
			items = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	@Override
	public List<AssignmentSubmission> getAllSubmissionsByAssignmentId(Long id) {
		List<AssignmentSubmission> items = null;

		try {
			Query q = entityManager
					.createQuery("select a from AssignmentSubmission a where a.active = 1 AND a.assignmentId = :id");
			q.setParameter("id", id);
			items = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

}