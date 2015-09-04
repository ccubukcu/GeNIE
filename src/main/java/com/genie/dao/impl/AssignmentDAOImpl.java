package com.genie.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.Hibernate;

import com.genie.dao.AssignmentDAO;
import com.genie.model.Assignment;
import com.genie.model.AssignmentSubmission;

/**
 * @author ccubukcu
 *
 */
@SuppressWarnings("unchecked")
public class AssignmentDAOImpl extends BaseDAOImpl implements AssignmentDAO {
	private static final long serialVersionUID = -8677651191866709092L;

	@Override
	public List<Assignment> getAll() {
		List<Assignment> assignments = null;

		try {
			assignments = entityManager
					.createQuery("select a from Assignment a").getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return assignments;
	}

	@Override
	public Assignment getById(long id) {
		return entityManager.find(Assignment.class, id);
	}

	@Override
	public boolean delete(Assignment a) {
		a = entityManager.getReference(Assignment.class, a.getId());
		if (a == null)
			return false;
		a.setActive(false);
		update(a);
		return true;
	}
	
	@Override
	public List<Assignment> getAllBySemesterCourseId(Long semCourseId) {
		List<Assignment> items = null;

		try {
			Query q = entityManager
					.createQuery("select a from Assignment a where a.active = 1 AND a.plan.semesterCourseId = :semCourseId");
			q.setParameter("semCourseId", semCourseId);
			items = q.getResultList();
			
			for (Assignment a : items) {
				Hibernate.initialize(a.getPlan());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	@Override
	public AssignmentSubmission getSubmissionWithDocument(String username, Long assignmentId) {
		AssignmentSubmission submission = null;

		try {
			Query q = entityManager
					.createQuery("select a from AssignmentSubmission a where a.active = 1"
							+ " AND a.assignmentId = :assignmentId AND a.studentName = :username");
			q.setParameter("assignmentId", assignmentId);
			q.setParameter("username", username);
			
			submission = (AssignmentSubmission) q.getSingleResult();
			
			Hibernate.initialize(submission.getDocument());
		} catch (NoResultException nre) {
			submission = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return submission;
	}

	@Override
	public boolean deleteSubmission(AssignmentSubmission a) {
		a = entityManager.getReference(AssignmentSubmission.class, a.getId());
		if (a == null)
			return false;
		a.setActive(false);
		update(a);
		return true;
		
	}
}
